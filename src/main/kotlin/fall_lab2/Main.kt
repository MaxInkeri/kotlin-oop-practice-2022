package fall_lab2

import java.io.File
import java.io.FileReader
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@ExperimentalTime
fun main() {
    while(true) {
        print("Введите адрес файла (CSV/XML) или нажмите Enter, чтобы выйти: ")
        val input = readln()
        if (input.isEmpty()) break
        val file = File(input)
        if (!listOf("csv", "xml").contains(file.extension)) {
            println("Неподдерживаемое расширение файла")
            continue
        }
        if (!file.exists()) {
            println("Указанного файла не существует\n")
            continue
        }

        val address = hashMapOf<String, Int>()
        val cities = hashMapOf<String, IntArray>()
        measureTime {
            val regex = Regex(when (file.extension) {
                "csv" -> "^\"([А-Яа-я -]+)\";[^;]+;[0-9]+;([1-5])$"
                "xml" -> "^<item city=\"([А-Яа-я -]+)\" street=\"[А-Яа-я0-9 -]+\" house=\"[0-9]+\" floor=\"([1-5])\" />$"
                else -> ""
            })
            FileReader(file).buffered().forEachLine { str ->
                if (!str.matches(regex)) {
                    return@forEachLine
                }
                address[str] = address.getOrDefault(str, 0) + 1
                if (address[str] == 1) { // if no same entry before
                    val (_, city, floorsStr) = regex.find(str)!!.groupValues
                    val floors = floorsStr.toInt() - 1
                    cities.putIfAbsent(city, intArrayOf(0, 0, 0, 0, 0))
                    cities[city]!![floors] += 1
                }
            }
        }.also {
            address.forEach { (str, entries) ->
                if (entries > 1) println("$str [$entries entries]")
            }
            cities.forEach { (city, data) ->
                print("\n$city\n" +
                        "\t1 floor:  ${data[0]}\n" +
                        "\t2 floors: ${data[1]}\n" +
                        "\t3 floors: ${data[2]}\n" +
                        "\t4 floors: ${data[3]}\n" +
                        "\t5 floors: ${data[4]}\n")
            }
            println("\n$it")
        }
    }
}