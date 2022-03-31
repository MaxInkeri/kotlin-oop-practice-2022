package lab1

fun main() {
    val books = parseBooks("1. Short Title // Author 1 // 2020\n2. Medium Title // Author 1, Author 2, Author 3 // 2022\n3. Book with Long Title // Author 1, Author 2 // 2021")
    val shortest = findBook(books, BookFinder.SHORTEST_TITLE)?.title
    val longest = findBook(books, BookFinder.LONGEST_TITLE)?.title
    val newest = findBook(books, BookFinder.NEWEST)?.title
    val oldest = findBook(books, BookFinder.OLDEST)?.title
    println("Shortest Title: $shortest\nLongest Title: $longest\nNewest: $newest\nOldest: $oldest")
}