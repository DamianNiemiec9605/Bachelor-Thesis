package damianniemiec.mushroomapp.identification

import android.content.res.AssetManager
import android.graphics.Bitmap
import damianniemiec.mushroomapp.models.RecognitionModel
import java.io.BufferedReader
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Float.compare
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*

class Classifier(
        var interpreter: Interpreter? = null,
        var inputSize: Int = 0,
        var labelList: List<String> = emptyList()
) : IClassifier {

    companion object {
        private const val MAX_RESULTS = 3
        private const val BATCH_SIZE = 1
        private const val PIXEL_SIZE = 3
        private const val THRESHOLD = 50f

        @Throws(IOException::class)
        fun create(assetManager: AssetManager,
                   modelPath: String,
                   labelPath: String,
                   inputSize: Int): Classifier {

            val classifier = Classifier()
            classifier.interpreter = Interpreter(classifier.loadModelFile(assetManager, modelPath))
            classifier.labelList = classifier.loadLabelList(assetManager, labelPath)
            classifier.inputSize = inputSize

            return classifier
        }
    }

    lateinit var recognitions: ArrayList<RecognitionModel>

    override fun recognizeImage(bitmap: Bitmap): List<RecognitionModel> {
        val byteBuffer = convertBitmapToByteBuffer(bitmap)
        val result = Array(1) { FloatArray(labelList.size) }
        interpreter!!.run(byteBuffer, result)
        return getSortedResult(result)
    }

    override fun close() {
        interpreter!!.close()
        interpreter = null
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    @Throws(IOException::class)
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        val labelList = ArrayList<String>()
        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
        while (true) {
            val line = reader.readLine() ?: break
            labelList.add(line)
        }
        reader.close()
        return labelList
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * BATCH_SIZE * inputSize * inputSize * PIXEL_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val `val` = intValues[pixel++]
                //Manually changing image pixel values in the same way that keras model would - by subtracting mean value for each channel
                byteBuffer.putFloat((`val` and 0xFF) - 123.68f)
                byteBuffer.putFloat((`val` shr 8 and 0xFF) - 116.779f)
                byteBuffer.putFloat((`val` shr 16 and 0xFF) - 103.939f)
            }
        }
        return byteBuffer
    }

    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<RecognitionModel> {
        val pq = PriorityQueue(
                MAX_RESULTS,
                Comparator<RecognitionModel> { (_, _, confidence1), (_, _, confidence2) -> compare(confidence1, confidence2) })

        for (i in labelList.indices) {
            val confidence = labelProbArray[0][i] * 100
            if (confidence > THRESHOLD) {
                pq.add(RecognitionModel("" + i,
                        if (labelList.size > i) labelList[i] else "Unknown",
                        confidence))
            }
        }

        recognitions = ArrayList()
        val recognitionModelsSize = Math.min(pq.size, MAX_RESULTS)
        for (i in 0 until recognitionModelsSize) {
            recognitions.add(pq.poll())
        }

        return recognitions
    }
}