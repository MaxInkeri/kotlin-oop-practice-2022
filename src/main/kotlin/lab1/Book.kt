package lab1

import java.util.Calendar

data class Book(val title: String, val authors: List<String>, val year: Int) {

    init {
        if (year > Calendar.getInstance().get(Calendar.YEAR)) error("Books from future are not accepted")
    }
}