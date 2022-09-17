package lab3

import java.time.LocalDateTime

sealed class Note(open val title: String, open val content: String) {
    val date: LocalDateTime? = LocalDateTime.now()

    data class TextNote(override val title: String, override val content: String): Note(title, content)

    data class Task(override val title: String, override val content: String, val deadline: LocalDateTime): Note(title, content) {
        constructor(title: String, content: String, year: Int, month: Int, day: Int, hour: Int, minute: Int):
                this(title, content, LocalDateTime.of(year, month, day, hour, minute))
        constructor(title: String, content: String, year: Int, month: Int, day: Int):
                this(title, content, year, month, day, 0, 0)
        init {
            if (deadline <= date) {
                error("Deadline must be later than the current date")
            }
        }
    }

    data class Link(override val title: String, override val content: String): Note(title, content) {
        init {
            if (!content.matches(Regex("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]"))) {
                error("Incorrect URL")
            }
        }
    }
}
