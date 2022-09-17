package fall_lab1

@kotlinx.serialization.Serializable
data class WikiSearch(
    val ns: Int,
    val title: String,
    val pageid: Int,
    val size: Int,
    val wordcount: Int,
    val snippet: String,
    val timestamp: String
)
