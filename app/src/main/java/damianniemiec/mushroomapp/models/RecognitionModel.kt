package damianniemiec.mushroomapp.models

data class RecognitionModel(
        var id: String = "", // A unique identifier for what has been recognized. Specific to the class, not the instance of the object.
        var title: String = "", // Display name for the RecognitionModel.
        var confidence: Float = 0F // A sortable score for how good the RecognitionModel is relative to others. Higher should be better.
)  {
    override fun toString(): String {
        return "$title, with confidence of %.2f".format(confidence)
    }
}