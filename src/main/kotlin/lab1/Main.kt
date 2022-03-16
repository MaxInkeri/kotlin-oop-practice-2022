package lab1

import java.util.regex.Pattern

fun parseBooks(books: String): List<Book> {
    val booksList = books.split("\n")
    val list = mutableListOf<Book>()
    val pattern = Pattern.compile("[0-9]+\\. ?([\\w ]+?) ?// ?([\\w ,]+?) ?// ?([\\d]+)")
    for (item in booksList) {
        val matcher = pattern.matcher(item)
        matcher.find()
        val title = matcher.group(1)
        val authors = matcher.group(2)
        val year = matcher.group(3).toInt()
        val book = Book(title, authors, year)
        list.add(book)
    }
    return list
}

enum class BookFinder {
    NEWEST, OLDEST, LONGEST_TITLE, SHORTEST_TITLE
}

fun findBook(books: List<Book>, property: BookFinder): Book {
    var bestValue: Int = if (property == BookFinder.NEWEST || property == BookFinder.LONGEST_TITLE) 0 else Int.MAX_VALUE
    var bestBook: Book = books[0]
    for (book in books) {
        if ((property == BookFinder.NEWEST && book.year > bestValue) || (property == BookFinder.OLDEST && book.year < bestValue)) {
            bestValue = book.year
            bestBook = book
        } else if ((property == BookFinder.LONGEST_TITLE && book.title.length > bestValue) || (property == BookFinder.SHORTEST_TITLE && book.year < bestValue)) {
            bestValue = book.title.length
            bestBook = book
        }
    }
    return bestBook
}

fun main() {
    val books = parseBooks("1. Short Title // Author 1 // 2020\n2. Medium Title // Author 1, Author 2, Author 3 // 2022\n3. Book with Long Title // Author 1, Author 2 // 2021")
    val shortest = findBook(books, BookFinder.SHORTEST_TITLE).title
    val longest = findBook(books, BookFinder.LONGEST_TITLE).title
    val newest = findBook(books, BookFinder.NEWEST).title
    val oldest = findBook(books, BookFinder.OLDEST).title
    println("Shortest Title: $shortest\nLongest Title: $longest\nNewest: $newest\nOldest: $oldest")
}