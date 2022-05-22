package cw.enums

enum class PaintShape(val title: String) {
    LINE("Line"),
    RECTANGLE("Rectangle"),
    OVAL("Oval"),
    RHOMBUS("Rhombus"),
    TRIANGLE("Isosceles Triangle"),
    RIGHT_TRIANGLE("Right Triangle");

    companion object {
        private val map = values().associateBy(PaintShape::title)
        fun find(title: String): PaintShape? = map[title]
    }
}