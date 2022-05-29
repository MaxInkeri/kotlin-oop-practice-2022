package shapes

import java.awt.Polygon

// isosceles triangle with (x, y) apex
class Triangle(x: Int, y: Int, halfWidth: Int, height: Int): Polygon() {
    init {
        addPoint(x, y)
        addPoint(x + halfWidth, y + height)
        addPoint(x - halfWidth, y + height)
    }
}