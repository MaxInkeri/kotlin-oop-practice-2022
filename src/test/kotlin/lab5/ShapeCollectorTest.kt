package lab5

import lab2.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.math.PI

internal class ShapeCollectorTest {

    private val red = Color(255, 0, 0)
    private val green = Color(0, 255, 0)
    private val blue = Color(0, 0, 255)
    private val circle1 = Circle(5.0, red, red) // 78.5398163397
    private val circle2 = Circle(6.0, red, green) // 113.097335529
    private val rectangle1 = Rectangle(11.0, 6.0, red, blue) // 66
    private val rectangle2 = Rectangle(12.0, 8.0, green, red) // 96
    private val square1 = Square(8.0, green, green) // 64
    private val square2 = Square(9.0, green, blue) // 81
    private val triangle1 = Triangle(10.0, 12.0, PI/2, blue, red) // 60
    private val triangle2 = Triangle(20.0, 20.0, PI/6, blue, green) // 100
    private val shapesList = listOf(circle1, circle2, rectangle1, rectangle2, square1, square2, triangle1, triangle2)
    private val collector = ShapeCollector(shapesList)

    @Test
    fun equals() {
        val theSameList = listOf(square1, square2, triangle1, triangle2, circle1, circle2, rectangle1, rectangle2)
        val newCollector = ShapeCollector(theSameList)
        assert(newCollector == collector)
    }

    @Test
    fun notEquals() {
        val anotherList = listOf(square1, square2, triangle1, triangle2, circle1, circle2, rectangle1)
        val newCollector = ShapeCollector(anotherList)
        assert(newCollector != collector)
    }

    @Test
    fun getSize() {
        assertEquals(8, collector.size)
    }

    @Test
    fun getShapes() {
        assertEquals(shapesList, collector.shapes)
    }

    @Test
    fun secondaryConstructor() {
        val std = ShapeCollector(listOf(circle1))
        val secondary = ShapeCollector(circle1)
        assertEquals(std, secondary)
    }

    @Test
    fun getTotalArea() {
        // pi*5^2 + pi*6^2 + 11*6 + 12*8 + 8^2 + 9^2 + 10*12*sin(pi/2)/2 + 20*20*sin(pi/6)/2
        assert(658.637151869 - collector.totalArea < 0.00000001)
    }

    @Test
    fun findCircles() {
        val circles = collector.find(Circle::class)
        assertEquals(listOf(circle1, circle2), circles)
    }

    @Test
    fun findRectangles() {
        val rectangles = collector.find(Rectangle::class)
        assertEquals(listOf(rectangle1, rectangle2), rectangles)
    }

    @Test
    fun findSquares() {
        val squares = collector.find(Square::class)
        assertEquals(listOf(square1, square2), squares)
    }

    @Test
    fun findTriangles() {
        val triangles = collector.find(Triangle::class)
        assertEquals(listOf(triangle1, triangle2), triangles)
    }

    @Test
    fun findSmallest() {
        val smallest = collector.findByArea(ShapeCollector.AreaMode.SMALLEST_AREA)
        assertEquals(triangle1, smallest)
    }

    @Test
    fun findBiggest() {
        val biggest = collector.findByArea(ShapeCollector.AreaMode.BIGGEST_AREA)
        assertEquals(circle2, biggest)
    }

    @Test
    fun findBorderRed() {
        val list = collector.findByColor(ShapeCollector.ColorMode.BORDER_COLOR, red)
        assertEquals(listOf(circle1, circle2, rectangle1), list)
    }

    @Test
    fun findBorderGreen() {
        val list = collector.findByColor(ShapeCollector.ColorMode.BORDER_COLOR, green)
        assertEquals(listOf(rectangle2, square1, square2), list)
    }

    @Test
    fun findBorderBlue() {
        val list = collector.findByColor(ShapeCollector.ColorMode.BORDER_COLOR, blue)
        assertEquals(listOf(triangle1, triangle2), list)
    }

    @Test
    fun findFillRed() {
        val list = collector.findByColor(ShapeCollector.ColorMode.FILL_COLOR, red)
        assertEquals(listOf(circle1, rectangle2, triangle1), list)
    }

    @Test
    fun findFillGreen() {
        val list = collector.findByColor(ShapeCollector.ColorMode.FILL_COLOR, green)
        assertEquals(listOf(circle2, square1, triangle2), list)
    }

    @Test
    fun findFillBlue() {
        val list = collector.findByColor(ShapeCollector.ColorMode.FILL_COLOR, blue)
        assertEquals(listOf(rectangle1, square2), list)
    }

    @Test
    fun groupByBorderColor() {
        val map = collector.groupByColor(ShapeCollector.ColorMode.BORDER_COLOR)
        assertEquals(mapOf(red to listOf(circle1, circle2, rectangle1), green to listOf(rectangle2, square1, square2), blue to listOf(triangle1, triangle2)), map)
    }

    @Test
    fun groupByFillColor() {
        val map = collector.groupByColor(ShapeCollector.ColorMode.FILL_COLOR)
        assertEquals(mapOf(red to listOf(circle1, rectangle2, triangle1), green to listOf(circle2, square1, triangle2), blue to listOf(rectangle1, square2)), map)
    }

    @Test
    fun addOneShape() {
        val initList = listOf(circle1, circle2, rectangle1, rectangle2, square1, square2, triangle2)
        val newCollector = ShapeCollector(initList)
        newCollector.add(triangle1)
        assertEquals(collector, newCollector)
    }

    @Test
    fun addFromList() {
        val initList = listOf(circle1, circle2, triangle1, triangle2)
        val addList = listOf(rectangle1, rectangle2, square1, square2)
        val newCollector = ShapeCollector(initList)
        newCollector.add(addList)
        assertEquals(collector, newCollector)
    }

    @Test
    fun addFromCollector() {
        val newCollector = ShapeCollector<ColoredShape2d>()
        newCollector.add(collector)
        assertEquals(collector, newCollector)
    }

    @Test
    fun getSortedByAreaDescending() {
        val sorted = collector.getSorted(compareByDescending { it.calcArea() })
        assertEquals(listOf(circle2, triangle2, rectangle2, square2, circle1, rectangle1, square1, triangle1), sorted)
    }

    @Test
    fun onlyCircles() {
        val circlesList = listOf(circle1, circle2)
        val circlesCollector = ShapeCollector(circlesList)
        assertEquals(circlesCollector.shapes, circlesCollector.find(Circle::class))
    }
}