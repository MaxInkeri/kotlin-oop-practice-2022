package lab5

import lab2.Color
import lab2.ColoredShape2d
import kotlin.reflect.KClass

class ShapeCollector<T: ColoredShape2d>(initialShapes: List<T> = emptyList()) {
    private val shapesList: MutableList<T> = initialShapes.toMutableList()
    val shapes: List<T>
        get() = shapesList.toList()

    constructor(shape: T) : this(listOf(shape)) // construct from only one shape

    fun add(shape: T) { // add one shape
        shapesList.add(shape)
    }

    fun add(shapesToAdd: Collection<T>) { // add all shapes from collection
        shapesList.addAll(shapesToAdd)
    }

    fun add(shapesToAdd: ShapeCollector<T>) { // add all shapes from another collector
        shapesList.addAll(shapesToAdd.shapesList)
    }

    val size: Int
        get() = shapesList.size

    val totalArea: Double
        get() {
            var area = 0.0
            for (shape in shapesList) {
                area += shape.calcArea()
            }
            return area
        }

    fun find(shapeClass: KClass<out T>): List<T> { // find shapes by class
        val list = mutableListOf<T>()
        for (shape in shapesList) {
            if (shape::class == shapeClass)
                list.add(shape)
        }
        return list.toList()
    }

    enum class AreaMode {
        SMALLEST_AREA, BIGGEST_AREA
    }
    fun findByArea(mode: AreaMode): T? {
        var best: T? = null
        for (shape in shapesList) {
            if (mode == AreaMode.SMALLEST_AREA && shape.calcArea() < (best?.calcArea() ?: Double.MAX_VALUE)
                || mode == AreaMode.BIGGEST_AREA && shape.calcArea() > (best?.calcArea() ?: 0.0)
            ) best = shape
        }
        return best
    }

    enum class ColorMode {
        BORDER_COLOR, FILL_COLOR
    }
    fun findByColor(mode: ColorMode, color: Color): List<T> {
        val list = emptyList<T>().toMutableList()
        for (shape in shapesList) {
            if (mode == ColorMode.BORDER_COLOR && shape.borderColor == color || mode == ColorMode.FILL_COLOR && shape.fillColor == color)
                list.add(shape)
        }
        return list
    }

    fun groupByColor(mode: ColorMode): Map<Color, List<T>> {
        val map = emptyMap<Color, MutableList<T>>().toMutableMap()
        for (shape in shapesList) {
            val color = if (mode == ColorMode.BORDER_COLOR) shape.borderColor else shape.fillColor
            if (map.containsKey(color)) map[color]?.add(shape)
            else map[color] = listOf(shape).toMutableList()
        }
        return map
    }

    fun getSorted(comparator: Comparator<in T>): List<T> {
        return shapesList.sortedWith(comparator)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShapeCollector<*>

        return shapesList.sortedBy { it.hashCode() } == other.shapesList.sortedBy { it.hashCode() } // compare sorted lists
    }

    override fun hashCode(): Int {
        return shapesList.sortedBy { it.hashCode() }.hashCode() // hash code of sorted list
    }
}