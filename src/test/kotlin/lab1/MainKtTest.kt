package lab1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MainKtTest {

    private val books = listOf(Book("Short Title", "Author1", 2021), Book("Book with Long Title", "Author2, Author3", 2022))

    @Test
    fun parseBooks() {
        val parsedBooks = parseBooks("1. Short Title // Author1 // 2021\n2. Book with Long Title // Author2, Author3 // 2022")
        assertArrayEquals(books.toTypedArray(), parsedBooks.toTypedArray())
    }

    @Test
    fun parseBookWithIncorrectYear() {
        assertThrows(IllegalStateException::class.java) {
            parseBooks("1. Title // Author // 2200")
        }
    }

    @Test
    fun findShortestTitle() {
        val book = findBook(books, BookFinder.SHORTEST_TITLE)
        assertEquals(books[0], book)
    }

    @Test
    fun findLongestTitle() {
        val book = findBook(books, BookFinder.LONGEST_TITLE)
        assertEquals(books[1], book)
    }

    @Test
    fun findOldest() {
        val book = findBook(books, BookFinder.OLDEST)
        assertEquals(books[0], book)
    }

    @Test
    fun findNewest() {
        val book = findBook(books, BookFinder.NEWEST)
        assertEquals(books[1], book)
    }
}