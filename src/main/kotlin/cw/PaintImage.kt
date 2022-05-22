package cw

import java.awt.Color
import java.awt.Point
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.Transferable
import java.awt.image.BufferedImage
import java.util.Stack

class PaintImage(width: Int, height: Int) : BufferedImage(width, height, TYPE_INT_RGB), Transferable {

    private fun clonePoint(point: Point, block: (Point.() -> Unit)? = null): Point {
        val cloned = Point(point)
        if (block == null) return cloned
        return cloned.apply(block)
    }

    fun fillArea(pxlX: Int, pxlY: Int, color: Color) {
        val point = Point(pxlX, pxlY)
        val rgb = getRGB(point)
        val rgbNew = color.rgb
        val points: Stack<Point> = Stack()
        points.add(point)
        val processed: List<MutableList<Boolean>> = (1..width).map { (1..height).map { false }.toMutableList() }

        while (points.size > 0) {
            val p = points.pop()
            if (rgb != getRGB(p.x, p.y)) continue
            if (processed[p.x][p.y]) continue

            setRGB(p.x, p.y, rgbNew)
            processed[p.x][p.y] = true

            if (p.x < width - 1) points.add(clonePoint(p) { x += 1 })
            if (p.x > 0) points.add(clonePoint(p) { x -= 1 })
            if (p.y < height - 1) points.add(clonePoint(p) { y += 1 })
            if (p.y > 0) points.add(clonePoint(p) { y -= 1 })
            if (p.x < width - 1 && p.y < height - 1) points.add(clonePoint(p) { x += 1; y += 1 })
            if (p.x < width - 1 && p.y > 0) points.add(clonePoint(p) { x += 1; y -= 1 })
            if (p.x > 0 && p.y < height - 1) points.add(clonePoint(p) { x -= 1; y += 1 })
            if (p.x > 0 && p.y > 0) points.add(clonePoint(p) { x -= 1; y -= 1 })
        }
    }

    private fun getRGB(point: Point): Int {
        return getRGB(point.x, point.y)
    }

    /** TRANSFERABLE IMPLEMENTATION **/

    override fun getTransferDataFlavors(): Array<DataFlavor> {
        return arrayOf(DataFlavor.imageFlavor)
    }

    override fun isDataFlavorSupported(flavor: DataFlavor?): Boolean {
        return (flavor == DataFlavor.imageFlavor)
    }

    override fun getTransferData(flavor: DataFlavor?): Any {
        return this
    }
}
