package lab3

import org.junit.jupiter.api.Test

import kotlin.test.assertEquals

internal class NoteServiceTest {
    private val textNote1 = Note.TextNote("Note", "Text Note 1")
    private val textNote2 = Note.TextNote("Important Note", "Text Note 2")
    private val task1 = Note.Task("Task", "Task 1", 2022, 12, 12)
    private val task2 = Note.Task("Important Task", "Task 2", 2023, 3, 23)
    private val link1 = Note.Link("Link", "https://example.com")
    private val link2 = Note.Link("Important Link", "https://example.net")

    @Test
    fun createTextNote() {
        val note = NoteServiceInstance.createTextNote("Note", "Text Note 1")
        assertEquals(textNote1, note)
    }

    @Test
    fun createTask() {
        val task = NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        assertEquals(task1, task)
    }

    @Test
    fun createLink() {
        val link = NoteServiceInstance.createLink("Link", "https://example.com")
        assertEquals(link1, link)
    }

    @Test
    fun getAllNotes() {
        NoteServiceInstance.clear()
        NoteServiceInstance.createTextNote("Note", "Text Note 1")
        NoteServiceInstance.createTextNote("Important Note", "Text Note 2")
        NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        NoteServiceInstance.createTask("Important Task", "Task 2", 2023, 3, 23)
        NoteServiceInstance.createLink("Link", "https://example.com")
        NoteServiceInstance.createLink("Important Link", "https://example.net")
        assertEquals(listOf(textNote1, textNote2, task1, task2, link1, link2), NoteServiceInstance.getAllNotes())
    }

    @Test
    fun getAllTextNotes() {
        NoteServiceInstance.clear()
        NoteServiceInstance.createTextNote("Note", "Text Note 1")
        NoteServiceInstance.createTextNote("Important Note", "Text Note 2")
        NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        NoteServiceInstance.createTask("Important Task", "Task 2", 2023, 3, 23)
        NoteServiceInstance.createLink("Link", "https://example.com")
        NoteServiceInstance.createLink("Important Link", "https://example.net")
        assertEquals(listOf(textNote1, textNote2), NoteServiceInstance.getAllTextNotes())
    }

    @Test
    fun getAllTasks() {
        NoteServiceInstance.clear()
        NoteServiceInstance.createTextNote("Note", "Text Note 1")
        NoteServiceInstance.createTextNote("Important Note", "Text Note 2")
        NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        NoteServiceInstance.createTask("Important Task", "Task 2", 2023, 3, 23)
        NoteServiceInstance.createLink("Link", "https://example.com")
        NoteServiceInstance.createLink("Important Link", "https://example.net")
        assertEquals(listOf(task1, task2), NoteServiceInstance.getAllTasks())
    }

    @Test
    fun getAllLinks() {
        NoteServiceInstance.clear()
        NoteServiceInstance.createTextNote("Note", "Text Note 1")
        NoteServiceInstance.createTextNote("Important Note", "Text Note 2")
        NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        NoteServiceInstance.createTask("Important Task", "Task 2", 2023, 3, 23)
        NoteServiceInstance.createLink("Link", "https://example.com")
        NoteServiceInstance.createLink("Important Link", "https://example.net")
        assertEquals(listOf(link1, link2), NoteServiceInstance.getAllLinks())
    }

    @Test
    fun removeNote() {
        NoteServiceInstance.clear()
        NoteServiceInstance.createTextNote("Note", "Text Note 1")
        val noteToDelete1 = NoteServiceInstance.createTextNote("Important Note", "Text Note 2")
        val noteToDelete2 = NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        NoteServiceInstance.createTask("Important Task", "Task 2", 2023, 3, 23)
        NoteServiceInstance.createLink("Link", "https://example.com")
        NoteServiceInstance.createLink("Important Link", "https://example.net")
        NoteServiceInstance.removeNote(noteToDelete1)
        NoteServiceInstance.removeNote(noteToDelete2)
        assertEquals(listOf(textNote1, task2, link1, link2), NoteServiceInstance.getAllNotes())
    }

    @Test
    fun find() {
        NoteServiceInstance.clear()
        NoteServiceInstance.createTextNote("Note", "Text Note 1")
        NoteServiceInstance.createTextNote("Important Note", "Text Note 2")
        NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        NoteServiceInstance.createTask("Important Task", "Task 2", 2023, 3, 23)
        NoteServiceInstance.createLink("Link", "https://example.com")
        NoteServiceInstance.createLink("Important Link", "https://example.net")
        assertEquals(listOf(textNote2, task2, link2), NoteServiceInstance.find("Important"))
    }

    @Test
    fun getSortedByTitle() {
        NoteServiceInstance.clear()
        NoteServiceInstance.createTextNote("Note", "Text Note 1")
        NoteServiceInstance.createTextNote("Important Note", "Text Note 2")
        NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        NoteServiceInstance.createTask("Important Task", "Task 2", 2023, 3, 23)
        NoteServiceInstance.createLink("Link", "https://example.com")
        NoteServiceInstance.createLink("Important Link", "https://example.net")
        assertEquals(listOf(link2, textNote2, task2, link1, textNote1, task1), NoteServiceInstance.getSortedBy(NoteServiceInstance.sortedByTitle))
    }

    @Test
    fun getSortedByDate() {
        NoteServiceInstance.clear()
        NoteServiceInstance.createTextNote("Note", "Text Note 1")
        NoteServiceInstance.createTextNote("Important Note", "Text Note 2")
        NoteServiceInstance.createTask("Task", "Task 1", 2022, 12, 12)
        NoteServiceInstance.createTask("Important Task", "Task 2", 2023, 3, 23)
        NoteServiceInstance.createLink("Link", "https://example.com")
        NoteServiceInstance.createLink("Important Link", "https://example.net")
        assertEquals(listOf(textNote1, textNote2, task1, task2, link1, link2), NoteServiceInstance.getSortedBy(NoteServiceInstance.sortedByDate))
    }
}