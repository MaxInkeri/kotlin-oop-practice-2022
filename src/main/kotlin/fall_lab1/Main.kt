package fall_lab1

import java.net.URLEncoder
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.awt.Desktop
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URI
import java.net.URL

fun main() {
    print("Введите запрос: ")
    val query = URLEncoder.encode(readln(), "UTF-8")
    val url = URL("https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=\"$query\"")
    val search = BufferedReader(InputStreamReader(url.openConnection().getInputStream())).use {
        val response: WikiResponse = Json.decodeFromString(it.readLine())
        response.query.search
    }
    for (i in search.indices) {
        val snippet = search[i].snippet
            .replace("<span class=\"searchmatch\">", "")
            .replace("</span>", "")
        println("[$i] ${search[i].title}\n$snippet\n")
    }

    var num: Int? = null
    while(num == null || num !in search.indices) {
        print("Номер страницы: ")
        num = readln().toIntOrNull()
    }
    Desktop.getDesktop().browse(URI("https://ru.wikipedia.org/w/index.php?curid=${search[num].pageid}"))
}