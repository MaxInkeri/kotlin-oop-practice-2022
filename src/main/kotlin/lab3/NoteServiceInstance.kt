package lab3

import java.time.LocalDateTime

object NoteServiceInstance: NoteService {
    private val notes = emptyList<Note>().toMutableList()

    override fun getAllNotes(): List<Note> {
        return notes.toList()
    }

    override fun getAllTextNotes(): List<Note.TextNote> {
        val textNotes = emptyList<Note.TextNote>().toMutableList()
        notes.forEach { if (it is Note.TextNote) textNotes.add(it) }
        return textNotes.toList()
    }

    override fun getAllTasks(): List<Note.Task> {
        val tasks = emptyList<Note.Task>().toMutableList()
        notes.forEach { if (it is Note.Task) tasks.add(it) }
        return tasks.toList()
    }

    override fun getAllLinks(): List<Note.Link> {
        val links = emptyList<Note.Link>().toMutableList()
        notes.forEach { if (it is Note.Link) links.add(it) }
        return links.toList()
    }

    override fun createTextNote(title: String, content: String): Note.TextNote {
        val note = Note.TextNote(title, content)
        notes.add(note)
        return note
    }

    override fun createTask(title: String, content: String, deadline: LocalDateTime): Note.Task {
        val note = Note.Task(title, content, deadline)
        notes.add(note)
        return note
    }

    override fun createTask(title: String, content: String, year: Int, month: Int, day: Int, hour: Int, minute: Int): Note.Task {
        val note = Note.Task(title, content, year, month, day, hour, minute)
        notes.add(note)
        return note
    }

    override fun createTask(title: String, content: String, year: Int, month: Int, day: Int): Note.Task {
        val note = Note.Task(title, content, year, month, day)
        notes.add(note)
        return note
    }

    override fun createLink(title: String, content: String): Note.Link {
        val note = Note.Link(title, content)
        notes.add(note)
        return note
    }

    override fun removeNote(note: Note) {
        notes.remove(note)
    }

    override fun find(str: String): List<Note> {
        return notes.filter { it.title.contains(str) }
    }

    const val sortedByTitle = false
    const val sortedByDate = true
    override fun getSortedBy(prop: Boolean): List<Note> {
        if (prop == sortedByTitle) {
            return notes.sortedBy { it.title }
        }
        return notes.sortedBy { it.date }
    }

    override fun clear() {
        notes.clear()
    }
}