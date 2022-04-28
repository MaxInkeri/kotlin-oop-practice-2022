package lab2

import kotlin.reflect.KClass

class ShapeCollector(initialShapes: List<ColoredShape2d> = emptyList()) {
    private val shapesList: MutableList<ColoredShape2d> = initialShapes.toMutableList()
    val shapes: List<ColoredShape2d>
        get() = shapesList.toList()

    constructor(shape: ColoredShape2d) : this(listOf(shape)) // construct from only one shape

    fun add(shape: ColoredShape2d) { // add one shape
        shapesList.add(shape)
    }

    fun add(shapesToAdd: List<ColoredShape2d>) { // add all shapes from list
        shapesList.addAll(shapesToAdd)
    }

    fun add(shapesToAdd: ShapeCollector) { // add all shapes from another collector
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

    fun find(shapeClass: KClass<out ColoredShape2d>): List<ColoredShape2d> { // find shapes by class
        val list = mutableListOf<ColoredShape2d>()
        for (shape in shapesList) {
            if (shape::class == shapeClass)
                list.add(shape)
        }
        return list.toList()
    }

    enum class Mode {
        SMALLEST_AREA, BIGGEST_AREA
    }
    fun findByArea(mode: Mode): ColoredShape2d? {
        var best: ColoredShape2d? = null
        for (shape in shapesList) {
            if (mode == Mode.SMALLEST_AREA && shape.calcArea() < (best?.calcArea() ?: Double.MAX_VALUE)
                || mode == Mode.BIGGEST_AREA && shape.calcArea() > (best?.calcArea() ?: 0.0)
            ) best = shape
        }
        return best
    }

    enum class ColorMode {
        BORDER_COLOR, FILL_COLOR
    }
    fun findByColor(mode: ColorMode, color: Color): List<ColoredShape2d> {
        val list = emptyList<ColoredShape2d>().toMutableList()
        for (shape in shapesList) {
            if (mode == ColorMode.BORDER_COLOR && shape.borderColor == color || mode == ColorMode.FILL_COLOR && shape.fillColor == color)
                list.add(shape)
        }
        return list
    }

    fun groupByColor(mode: ColorMode): Map<Color, List<ColoredShape2d>> {
        val map = emptyMap<Color, MutableList<ColoredShape2d>>().toMutableMap()
        for (shape in shapesList) {
            val color = if (mode == ColorMode.BORDER_COLOR) shape.borderColor else shape.fillColor
            if (map.containsKey(color)) map[color]?.add(shape)
            else map[color] = listOf(shape).toMutableList()
        }
        return map
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShapeCollector

        return shapesList.sortedBy { it.hashCode() } == other.shapesList.sortedBy { it.hashCode() } // compare sorted lists
    }

    override fun hashCode(): Int {
        return shapesList.sortedBy { it.hashCode() }.hashCode() // hash code of sorted list
    }
}