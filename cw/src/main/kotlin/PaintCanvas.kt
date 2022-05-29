import PaintUI.ResizeMode.*
import enums.ColorButtonInfo
import enums.PaintAction
import enums.PropertySpinnerInfo
import enums.PaintShape
import shapes.Rhombus
import shapes.RightTriangle
import shapes.Triangle
import utilities.PaintUtilities
import java.awt.*
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.lang.Integer.min
import java.util.*
import javax.swing.JOptionPane
import javax.swing.JPanel
import kotlin.math.abs

private const val DEFAULT_WIDTH = 800
private const val DEFAULT_HEIGHT = 600

private const val DEFAULT_FONT_SIZE = 20f

class PaintCanvas: JPanel(true) {

    /***********************/
    /*** SCALING & SHIFT ***/
    /***********************/

    var scale: Double = 1.0
        set(value) {
            newScaleFactor = value / scale
            field = value
            updateSize(image.width, image.height, true)
        }
    private var newScaleFactor: Double = 1.0
    val currentShift = Point(0, 0)

    init {
        updateSize(DEFAULT_WIDTH, DEFAULT_HEIGHT)
    }

    private fun updateSize(w: Int, h: Int, scaling: Boolean = false) {
        val wScaled = (w * scale).toInt()
        val hScaled = (h * scale).toInt()
        preferredSize = Dimension(wScaled, hScaled)
        size = preferredSize
        if (scaling) {
            val pos = mousePosition ?: Point(wScaled / 2, hScaled / 2)
            currentShift.apply {
                x += (pos.x * (1.0 - newScaleFactor)).toInt()
                y += (pos.y * (1.0 - newScaleFactor)).toInt()
            }
        }
    }

    fun updateLocation() {
        if (location != currentShift) location = currentShift
    }


    /****************/
    /*** GRAPHICS ***/
    /****************/

    var image: PaintImage = PaintImage(width, height)
    private var gfx: Graphics2D = newGfx(true)
        get() = field.apply {
            stroke = BasicStroke(thickness.toFloat(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
        }

    private fun newGfx(clear: Boolean = false): Graphics2D {
        return image.createGraphics().apply {
            background = Color.WHITE // background color
            if (clear) clearRect(0, 0, image.width, image.height) // fill background with white color
            font = textFont
            setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        }
    }

    fun resizeImage(w: Int, h: Int) {
        storeToHistory()
        val oldImage = image
        val oldW = image.width
        val oldH = image.height
        updateSize(w, h)
        image = PaintImage(w, h)
        gfx = newGfx(true).apply {
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

    override fun paintComponent(g: Graphics) {
        updateLocation()
        g.drawImage(image, 0, 0, width, height, null)
    }

    fun updateCanvas() {
        paintComponent(graphics)
    }


    /********************/
    /*** DRAWING DATA ***/
    /********************/

    private val currentAction
        get() = PaintUI.currentAction

    private val currentShape
        get() = PaintUI.currentShape

    var thickness = 1
    private val borderColor: Color
        get() = ColorButtonInfo.BORDER_COLOR_BUTTON.button!!.background
    private val fillingColor: Color
        get() = ColorButtonInfo.FILLING_COLOR_BUTTON.button!!.background

    var textFont = PaintUtilities.createFont(DEFAULT_FONT_SIZE)
        private set

    fun updateFont(newFont: Font) {
        textFont = newFont
        gfx.font = newFont
    }

    var drawBorder = true
    var drawFilling = false

    private fun useBorderColor() {
        gfx.paint = borderColor
    }
    private fun useFillingColor() {
        gfx.paint = fillingColor
    }


    /***************/
    /*** DRAWING ***/
    /***************/

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

    private inner class CanvasMouseListener: MouseAdapter() {
        override fun mousePressed(e: MouseEvent?) {
            if (e == null) return
            storeToHistory()
            val p = realPosition(e)
            when (currentAction) {
                PaintAction.PENCIL -> {
                    useBorderColor()
                    gfx.drawLine(p.x, p.y, p.x, p.y)
                    fixPosition(p)
                }
                PaintAction.SHAPE -> fixPosition(p)
                PaintAction.TEXT -> {
                    useBorderColor()
                    try {
                        gfx.drawString(
                            JOptionPane.showInputDialog(PaintUI, "", "Input Text", JOptionPane.PLAIN_MESSAGE),
                            p.x, p.y
                        )
                    }
                    catch (_: java.lang.NullPointerException) {}
                }
                PaintAction.FILL -> image.fillArea(p.x, p.y, fillingColor)
                else -> {}
            }
            updateCanvas()
        }
    }

    private inner class CanvasMouseMotionListener: MouseMotionAdapter() {
        override fun mouseDragged(e: MouseEvent?) {
            if (e == null) return
            val p = realPosition(e)
            when (currentAction) {
                PaintAction.PENCIL -> {
                    useBorderColor()
                    gfx.drawLine(p.x, p.y, from.x, from.y)
                    fixPosition(p)
                }
                PaintAction.SHAPE -> {
                    gfx.drawImage(history.peek(), 0, 0, null) // redraw last saved state to erase preliminary shape trace
                    val x = min(from.x, p.x)
                    val y = min(from.y, p.y)
                    val w = abs(from.x - p.x)
                    val h = abs(from.y - p.y)
                    when (currentShape) {
                        PaintShape.LINE -> {
                            useBorderColor()
                            gfx.drawLine(from.x, from.y, p.x, p.y)
                        }
                        PaintShape.RECTANGLE -> {
                            drawShape(Rectangle(x, y, w, h))
                        }
                        PaintShape.OVAL -> {
                            drawShape({ gfx.drawOval(x, y, w, h) }, { gfx.fillOval(x, y, w, h) })
                        }
                        PaintShape.RHOMBUS -> {
                            drawShape(Rhombus(x, y, w, h))
                        }
                        PaintShape.TRIANGLE -> {
                            drawShape(Triangle(from.x, from.y, p.x - from.x, p.y - from.y))
                        }
                        PaintShape.RIGHT_TRIANGLE -> {
                            drawShape(RightTriangle(from.x, from.y, p.x - from.x, p.y - from.y))
                        }
                    }
                }
                else -> {}
            }
            updateCanvas()
        }
    }

    init {
        addMouseListener(CanvasMouseListener())
        addMouseMotionListener(CanvasMouseMotionListener())
    }


    /***************/
    /*** HISTORY ***/
    /***************/

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
        PropertySpinnerInfo.WIDTH.spinner!!.setValue(image.width)
        PropertySpinnerInfo.HEIGHT.spinner!!.setValue(image.height)
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


    /*****************/
    /*** CLIPBOARD ***/
    /*****************/

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
}