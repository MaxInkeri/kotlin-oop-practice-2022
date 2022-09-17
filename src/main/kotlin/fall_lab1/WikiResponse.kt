package fall_lab1

@kotlinx.serialization.Serializable
data class WikiResponse(
    val batchcomplete: String,
    val `continue`: WikiContinue,
    val query: WikiQuery
)