package lab6

import lab2.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.math.PI

internal class ShapeJSONTest {

    private val red = Color(255, 0, 0)
    private val green = Color(0, 255, 0)
    private val blue = Color(0, 0, 255)
    private val circle1 = Circle(5.0, red, red) // 78.5398163397
    private val circle2 = Circle(6.0, red, green) // 113.097335529
    private val rectangle1 = Rectangle(11.0, 6.0, red, blue) // 66
    private val rectangle2 = Rectangle(12.0, 8.0, green, red) // 96
    private val square1 = Square(8.0, green, green) // 64
    private val square2 = Square(9.0, green, blue) // 81
    private val triangle1 = Triangle(10.0, 12.0, PI /2, blue, red) // 60
    private val triangle2 = Triangle(20.0, 20.0, PI /6, blue, green) // 100
    private val shapesList = listOf(circle1, circle2, rectangle1, rectangle2, square1, square2, triangle1, triangle2)
    private val collector = ShapeCollector(shapesList)

    @Test
    fun serializeAndDeserialize() {
        val str = ShapeJSON.serializeCollector(collector)
        val newCollector = ShapeJSON.deserializeAsCollector(str)
        assertEquals(collector, newCollector)
    }

    @Test
    fun serializeAndDeserializeAsList() {
        val str = ShapeJSON.serialize(collector.shapes)
        val newList = ShapeJSON.deserialize(str)
        assertEquals(collector.shapes, newList)
    }

    @Test
    fun writeAndRead() {
        val file = "shapes.json"
        ShapeFiles.writeCollector(file, collector)
        val newCollector = ShapeFiles.readAsCollector(file)
        assertEquals(collector, newCollector)
    }

    @Test
    fun writeAndReadAsList() {
        val file = "shapes.json"
        ShapeFiles.write(file, collector.shapes)
        val newList = ShapeFiles.read(file)
        assertEquals(collector.shapes, newList)
    }
}