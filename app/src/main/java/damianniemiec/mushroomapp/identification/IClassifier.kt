package damianniemiec.mushroomapp.identification

import android.graphics.Bitmap
import damianniemiec.mushroomapp.models.RecognitionModel

interface IClassifier {
    fun recognizeImage(bitmap: Bitmap): List<RecognitionModel>
    fun close()
}