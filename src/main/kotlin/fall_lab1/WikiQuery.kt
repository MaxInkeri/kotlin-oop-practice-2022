package fall_lab1

@kotlinx.serialization.Serializable
data class WikiQuery(
    val searchinfo: WikiSearchInfo,
    val search: List<WikiSearch>
)
