package cw.shapes

import java.awt.Polygon

class Rhombus(x: Int, y: Int, width: Int, height: Int): Polygon() {
    init {
        addPoint(x + width / 2, y)
        addPoint(x + width, y + height / 2)
        addPoint(x + width / 2, y + height)
        addPoint(x, y + height / 2)
    }
}