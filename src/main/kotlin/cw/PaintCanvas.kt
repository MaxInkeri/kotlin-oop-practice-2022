package cw

import cw.PaintUI.ResizeMode.*
import cw.components.Property
import cw.shapes.Rhombus
import cw.shapes.RightTriangle
import cw.shapes.Triangle
import cw.utilities.PaintUtilities
import java.awt.*
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener
import java.lang.Integer.min
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JPanel
import kotlin.math.abs

private const val DEFAULT_WIDTH = 800
private const val DEFAULT_HEIGHT = 600

class PaintCanvas: JPanel(true) {

    var scale: Double = 1.0
    val currentShift = Point(0, 0)

    init {
        updateSize(DEFAULT_WIDTH, DEFAULT_HEIGHT)
    }

    private fun updateSize(w: Int, h: Int) {
        preferredSize = Dimension((w * scale).toInt(), (h * scale).toInt())
        size = preferredSize
    }

    fun updateSize() {
        updateSize(image.width, image.height)
    }

    fun updateLocation() {
        if (location != currentShift) location = currentShift
    }

    private val currentAction
        get() = PaintUI.currentAction

    private val currentShape
        get() = PaintUI.currentShape

    var thickness = 1
    var borderColor: Color = Color.BLACK
    var fillingColor: Color = Color.WHITE

    var drawBorder = true
    var drawFilling = false

    private fun useBorderColor() {
        gfx.paint = borderColor
    }
    private fun useFillingColor() {
        gfx.paint = fillingColor
    }

    var image: PaintImage = PaintImage(width, height)
    private var gfx: Graphics2D = newGfx(true)
        get() = field.apply {
            stroke = BasicStroke(thickness.toFloat(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        }

    private fun newGfx(clear: Boolean = false): Graphics2D {
        return image.createGraphics().apply {
            background = Color.WHITE // background color
            if (clear) clearRect(0, 0, width, height) // fill background with white color
            font = PaintUtilities.createFont(BIG_FONT)
            setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        }
    }

    override fun paintComponent(g: Graphics) {
        updateLocation()
        g.drawImage(image, 0, 0, width, height, null)
    }

    fun updateCanvas() {
        paintComponent(graphics)
    }

    private val history = Stack<PaintImage>()
    private val undoHistory = Stack<PaintImage>()
    private fun storeToHistory(toUndoHistory: Boolean = false, clearUndoHistory: Boolean = true) {
        if (clearUndoHistory) undoHistory.clear()
        val img = PaintImage(image.width, image.height)
        img.createGraphics().apply {
            drawImage(image, 0, 0, null)
            dispose()
        }
        (if (toUndoHistory) undoHistory else history).add(img)
        updateUIButtons()
    }

    private fun loadFromHistory(fromUndoHistory: Boolean) {
        storeToHistory(!fromUndoHistory, false)
        image = (if (fromUndoHistory) undoHistory else history).pop()
        gfx = newGfx()
        updateSize(image.width, image.height)
        updateUIButtons()
        updateCanvas()
        Property.WIDTH.spinner!!.setValue(image.width)
        Property.HEIGHT.spinner!!.setValue(image.height)
    }

    private fun updateUIButtons() {
        PaintUI.undoButtonEnabled = history.size > 0
        PaintUI.redoButtonEnabled = undoHistory.size > 0
    }

    fun undo() {
        if (history.size > 0) loadFromHistory(false)
    }

    fun redo() {
        if (undoHistory.size > 0) loadFromHistory(true)
    }

    fun resizeImage(w: Int, h: Int) {
        storeToHistory()
        val oldImage = image
        val oldW = image.width
        val oldH = image.height
        updateSize(w, h)
        image = PaintImage(w, h)
        gfx = newGfx().apply {
            when (PaintUI.resizeMode) {
                EXTENSION -> drawImage(oldImage, 0, 0, w, h, null)
                ADD_SPACE_UP_RIGHT -> drawImage(oldImage, 0, h - oldH, oldW, oldH, null)
                ADD_SPACE_UP_LEFT -> drawImage(oldImage, w - oldW, h - oldH, oldW, oldH, null)
                ADD_SPACE_DOWN_RIGHT -> drawImage(oldImage, 0, 0, oldW, oldH, null)
                ADD_SPACE_DOWN_LEFT -> drawImage(oldImage, w - oldW, 0, oldW, oldH, null)
            }
        }
        updateCanvas()
    }

    private val clipboard
        get() = Toolkit.getDefaultToolkit().systemClipboard

    fun copyImage() {
        clipboard.setContents(image, null)
    }

    fun pasteImage() {
        clipboard.apply {
            storeToHistory()
            try {
                val img = getData(DataFlavor.imageFlavor) as Image
                if (mousePosition == null) {
                    gfx.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null)
                } else {
                    gfx.drawImage(img, mousePosition.x, mousePosition.y, img.getWidth(null), img.getHeight(null), null)
                }
                updateCanvas()
            }
            catch (_: UnsupportedFlavorException) {
                try {
                    val str = getData(DataFlavor.stringFlavor) as String
                    useBorderColor()
                    if (mousePosition == null) {
                        gfx.drawString(str, 0, gfx.font.size)
                    } else {
                        gfx.drawString(str, mousePosition.x, mousePosition.y)
                    }
                    updateCanvas()
                }
                catch (_: UnsupportedFlavorException) {
                    history.pop()
                    JOptionPane.showMessageDialog(this@PaintCanvas, "Can't paste content from clipboard", "Error", JOptionPane.ERROR_MESSAGE)
                }
            }
        }
    }

    private val from = Point(0, 0) // start point for drawing shape or last point for pencil
    private fun fixPosition(p: Point) {
        from.x = p.x
        from.y = p.y
    }

    private fun realPosition(e: MouseEvent): Point {
        return Point((e.x / scale).toInt(), (e.y / scale).toInt())
    }

    private fun drawShape(shape: Shape) {
        if (drawFilling) {
            useFillingColor()
            gfx.fill(shape)
        }
        if (drawBorder) {
            useBorderColor()
            gfx.draw(shape)
        }
    }

    private fun drawShape(drawBorderFunction: () -> Unit, drawFillingFunction: () -> Unit) {
        if (drawFilling) {
            useFillingColor()
            drawFillingFunction()
        }
        if (drawBorder) {
            useBorderColor()
            drawBorderFunction()
        }
    }

    private inner class CanvasMouseListener: MouseListener {
        override fun mouseClicked(e: MouseEvent?) {}

        override fun mousePressed(e: MouseEvent?) {
            if (e == null) return
            storeToHistory()
            val p = realPosition(e)
            when (currentAction) {
                PaintUI.Action.PENCIL -> {
                    useBorderColor()
                    gfx.drawLine(p.x, p.y, p.x, p.y)
                    fixPosition(p)
                }
                PaintUI.Action.SHAPE -> {
                    fixPosition(p)
                }
                PaintUI.Action.FILL -> {
                    image.fillArea(p.x, p.y, fillingColor)
                }
                else -> {}
            }
            updateCanvas()
        }

        override fun mouseReleased(e: MouseEvent?) {}

        override fun mouseEntered(e: MouseEvent?) {}

        override fun mouseExited(e: MouseEvent?) {}
    }

    private inner class CanvasMouseMotionListener: MouseMotionListener {

        override fun mouseDragged(e: MouseEvent?) {
            if (e == null) return
            val p = realPosition(e)
            when (currentAction) {
                PaintUI.Action.PENCIL -> {
                    useBorderColor()
                    gfx.drawLine(p.x, p.y, from.x, from.y)
                    fixPosition(p)
                }
                PaintUI.Action.SHAPE -> {
                    gfx.drawImage(history.peek(), 0, 0, null) // redraw last saved state to erase preliminary shape trace
                    val x = min(from.x, p.x)
                    val y = min(from.y, p.y)
                    val w = abs(from.x - p.x)
                    val h = abs(from.y - p.y)
                    when (currentShape) {
                        PaintUI.Shape.LINE -> {
                            useBorderColor()
                            gfx.drawLine(from.x, from.y, p.x, p.y)
                        }
                        PaintUI.Shape.RECTANGLE -> {
                            drawShape(Rectangle(x, y, w, h))
                        }
                        PaintUI.Shape.OVAL -> {
                            drawShape({ gfx.drawOval(x, y, w, h) }, { gfx.fillOval(x, y, w, h) })
                        }
                        PaintUI.Shape.RHOMBUS -> {
                            drawShape(Rhombus(x, y, w, h))
                        }
                        PaintUI.Shape.TRIANGLE -> {
                            drawShape(Triangle(from.x, from.y, p.x - from.x, p.y - from.y))
                        }
                        PaintUI.Shape.RIGHT_TRIANGLE -> {
                            drawShape(RightTriangle(from.x, from.y, p.x - from.x, p.y - from.y))
                        }
                    }
                }
                else -> {}
            }
            updateCanvas()
            mouseMoved(e)
        }

        override fun mouseMoved(e: MouseEvent?) {}
    }

    init {
        addMouseListener(CanvasMouseListener())
        addMouseMotionListener(CanvasMouseMotionListener())
    }
}