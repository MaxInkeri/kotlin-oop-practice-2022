package lab6

import lab2.ShapeCollector
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

object ShapeFiles {
    fun read(file: String): ShapeCollector? {
        return try {
            val string = FileReader(file).buffered().readText()
            ShapeJSON.deserialize(string)
        } catch (e: IOException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    fun write(file: String, shapes: ShapeCollector) {
        val string = ShapeJSON.serialize(shapes)
        try {
            FileWriter(file).buffered().use { writer ->
                writer.write(string)
            }
        }
        catch (_: IOException) {

        }
    }
}