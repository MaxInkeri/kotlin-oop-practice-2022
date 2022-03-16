package lab1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class BookTest {

    private val book = Book("Title", "Author1, Author2", 2020)

    @Test
    fun getAuthors() {
        assertEquals("Author1", book.authors[0])
        assertEquals("Author2", book.authors[1])
    }

    @Test
    fun getTitle() {
        assertEquals("Title", book.title)
    }

    @Test
    fun getAuthorsList() {
        assertEquals("Author1, Author2", book.authorsList)
    }

    @Test
    fun getYear() {
        assertEquals(2020, book.year)
    }
}