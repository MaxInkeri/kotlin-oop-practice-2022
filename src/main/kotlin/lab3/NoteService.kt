package lab3

import java.time.LocalDateTime

interface NoteService {
    fun getAllNotes(): List<Note>
    fun getAllTextNotes(): List<Note.TextNote>
    fun getAllTasks(): List<Note.Task>
    fun getAllLinks(): List<Note.Link>

    fun createTextNote(title: String, content: String): Note.TextNote
    fun createTask(title: String, content: String, deadline: LocalDateTime): Note.Task
    fun createTask(title: String, content: String, year: Int, month: Int, day: Int, hour: Int, minute: Int): Note.Task
    fun createTask(title: String, content: String, year: Int, month: Int, day: Int): Note.Task
    fun createLink(title: String, content: String): Note.Link

    fun removeNote(note: Note)

    fun find(str: String): List<Note>

    enum class SortedBy {
        TITLE, DATE
    }
    fun getSortedBy(prop: SortedBy): List<Note>

    fun clear()
}