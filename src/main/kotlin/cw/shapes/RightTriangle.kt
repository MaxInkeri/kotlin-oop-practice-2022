package cw.shapes

import java.awt.Polygon

// right triangle with (x, y) right-angled vertex
class RightTriangle(x: Int, y: Int, width: Int, height: Int): Polygon() {
    init {
        addPoint(x, y)
        addPoint(x + width, y)
        addPoint(x, y + height)
    }
}