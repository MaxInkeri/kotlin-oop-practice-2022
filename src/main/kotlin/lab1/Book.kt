package lab1

import java.util.Calendar

data class Book(val title: String, val authorsList: String, val year: Int) {
    private val _authors: MutableList<String>
    val authors: List<String>
    get() = _authors

    init {
        if (year > Calendar.getInstance().get(Calendar.YEAR)) error("Books from future are not accepted")

        _authors = authorsList.split(",").toMutableList()
        for (i in _authors.indices) {
            _authors[i] = _authors[i].trim()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (title != other.title) return false
        if (authorsList != other.authorsList) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + authorsList.hashCode()
        result = 31 * result + year
        return result
    }

}