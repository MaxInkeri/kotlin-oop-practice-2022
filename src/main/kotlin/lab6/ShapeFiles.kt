package lab6

import lab2.ColoredShape2d
import lab2.ShapeCollector
import java.io.FileReader
import java.io.FileWriter

object ShapeFiles {
    private fun readFile(file: String): String {
        return FileReader(file).buffered().readText()
    }

    fun read(file: String): List<ColoredShape2d> {
        return ShapeJSON.deserialize(readFile(file))
    }

    fun readAsCollector(file: String): ShapeCollector {
        return ShapeJSON.deserializeAsCollector(readFile(file))
    }

    private fun writeFile(file: String, str: String) {
        FileWriter(file).buffered().use { writer ->
            writer.write(str)
        }
    }

    fun write(file: String, shapes: List<ColoredShape2d>) {
        writeFile(file, ShapeJSON.serialize(shapes))
    }

    fun writeCollector(file: String, shapes: ShapeCollector) {
        writeFile(file, ShapeJSON.serializeCollector(shapes))
    }
}