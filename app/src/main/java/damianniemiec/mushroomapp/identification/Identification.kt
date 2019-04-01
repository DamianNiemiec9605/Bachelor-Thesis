package damianniemiec.mushroomapp.identification

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.wonderkiln.camerakit.*
import damianniemiec.mushroomapp.R
import damianniemiec.mushroomapp.atlas.Atlas
import java.util.concurrent.Executors


class Identification : Fragment() {

    companion object {
        private const val MODEL_PATH = "converted_new.tflite"
        private const val LABEL_PATH = "labels.txt"
        private const val INPUT_SIZE = 224

        private val LABEL_NUMBERS = hashMapOf(
                        "borowik szlachetny" to 1,
                        "czubajka kania" to 2,
                        "kozlarz babka" to 3,
                        "maslak zwyczajny" to 4,
                        "mleczaj rydz" to 5,
                        "muchomor czerwony" to 6,
                        "muchomor sromotnikowy" to 7,
                        "opienka" to 8,
                        "pieczarka" to 9,
                        "pieprznik" to 10,
                        "podgrzybek" to 11,
                        "szatan" to 12 )

        fun newInstance(): Identification = Identification()
    }

    lateinit var classifier: Classifier

    private val executor = Executors.newSingleThreadExecutor()
    lateinit var btnDetectObject: Button
    lateinit var cameraView: CameraView
    var lastRecognitionLabel: String = R.string.unknown_text.toString()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.identification_layout, container, false)
        cameraView = view.findViewById(R.id.camera_view)

        btnDetectObject = view.findViewById(R.id.b_picture)

        val tvTextResults = view.findViewById<TextView>(R.id.res)
        val btnGoToDetails = view.findViewById<Button>(R.id.gotoatlas)

        cameraView.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) { }

            override fun onError(cameraKitError: CameraKitError) { }

            override fun onImage(cameraKitImage: CameraKitImage) {

                var bitmap = cameraKitImage.bitmap
                val resizedbitmap1 = Bitmap.createBitmap(bitmap, 0, 840, 2160, 2160)

                bitmap = Bitmap.createScaledBitmap(resizedbitmap1, INPUT_SIZE, INPUT_SIZE, false)

                val results = classifier.recognizeImage(bitmap)

                tvTextResults.text = if (results.isNotEmpty())
                    "Wykryty gatunek grzyba to ${results[0].title} z pewnością %.2f".format(results[0].confidence)+" %."
                else "Nie wykryto grzyba na zdjęciu"

                tvTextResults.visibility = View.VISIBLE
                if (results.isNotEmpty())
                    btnGoToDetails.visibility = View.VISIBLE
            }
            override fun onVideo(cameraKitVideo: CameraKitVideo) {
            }
        })

        btnDetectObject.setOnClickListener {
            cameraView.captureImage()
        }

        btnGoToDetails.setOnClickListener {
            val atlasFragment = Atlas.newInstance()
//            val itemView = atlasFragment.getRecyclerView().findViewHolderForAdapterPosition(LABEL_NUMBERS[lastRecognitionLabel] ?: -1).itemView
//            itemView.performClick()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container, atlasFragment)
            transaction?.commit()
        }
        initTensorFlowAndLoadModel()

        return view
    }


    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        cameraView.stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.execute { classifier.close() }
    }

    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            try {
                classifier = Classifier.create(
                        activity!!.assets,
                        MODEL_PATH,
                        LABEL_PATH,
                        INPUT_SIZE)
                makeButtonVisible()
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

    private fun makeButtonVisible() {
        activity!!.runOnUiThread { btnDetectObject.visibility = View.VISIBLE }
    }
}
