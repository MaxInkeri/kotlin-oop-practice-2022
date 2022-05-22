package cw

import cw.components.DynamicColorButton
import cw.components.PropertySpinner
import cw.components.Property
import cw.components.ToolbarButton
import cw.listeners.resizer.FontResizer
import cw.listeners.resizer.ButtonResizer
import cw.listeners.PaintKeyListener
import cw.listeners.PaintWheelListener
import cw.listeners.resizer.CustomResizer
import cw.utilities.PaintUtilities.createFont
import cw.utilities.PaintUtilities.createIcon
import java.awt.*
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.JSpinner.NumberEditor

private const val MIN_WIDTH = 600
private const val MIN_HEIGHT = 600
private const val GAP = 10
const val SMALL_FONT = 12f
const val BIG_FONT = 20f
const val MIN_SCALE = 0.1
const val MAX_SCALE = 8.0
private const val SCALING_STEP = 0.1

object PaintUI: JFrame("is not Paint") {
    internal var canvas: PaintCanvas = PaintCanvas()

    var currentAction = Action.PENCIL
        internal set
    var currentShape = Shape.LINE
        internal set

    private val borderColorButton = createColorButton(ColorButton.BORDER_COLOR_BUTTON)
    private val fillingColorButton = createColorButton(ColorButton.FILLING_COLOR_BUTTON)

    private val scaleSpinner = createScaleSpinner()
    private var colorPicker = createColorPicker()
    private val toolbar = createToolbar()

    val toolbarIsVertical
        get() = (toolbar.orientation == JToolBar.VERTICAL)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(MIN_WIDTH, MIN_HEIGHT)
        Toolkit.getDefaultToolkit().screenSize.apply {
            preferredSize = Dimension((width * 0.6).toInt(), (height * 0.8).toInt())
        }
        size = preferredSize
        extendedState = MAXIMIZED_BOTH
        rootPane.contentPane = JPanel(BorderLayout(GAP, GAP)).apply {
            border = BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP)
            add(toolbar, BorderLayout.NORTH)
            add(JPanel().apply {
                add(canvas)
            }, BorderLayout.CENTER)
            add(createBottomToolbar(), BorderLayout.SOUTH)
        }
        addKeyListener(PaintKeyListener(canvas))
        addMouseWheelListener(PaintWheelListener(canvas, scaleSpinner))
        isFocusable = true
        requestFocus()
    }

    private fun createToolbar(): JToolBar {
        return JToolBar(JToolBar.HORIZONTAL).apply {
            add(createButton(Action.PENCIL))
            add(createButton(Action.SHAPE))
            add(createButton(Action.FILL))
            add(Box.createHorizontalStrut(30))
            add(createSpinners().also { hideIfToolbarIsVertical(this, it) })
            add(Box.createGlue())
            add(createColorAndSizeProperties(this))
            add(colorPicker.also { hideIfToolbarIsVertical(this, it) })
            add(Box.createGlue())
            add(createButton(Action.UNDO))
            add(createButton(Action.REDO))
            add(createButton(Action.SAVE))
            addPropertyChangeListener { event ->
                if (event.propertyName == "orientation") {
                    Action.forEach {
                        ButtonResizer.newSize(it.button!!)
                    }
                    ButtonResizer.newSize(borderColorButton)
                    ButtonResizer.newSize(fillingColorButton)
                }
            }
        }
    }

    private fun createButton(type: Action): ToolbarButton {
        return ToolbarButton(type).also {
            type.button = it
        }
    }

    private fun hideIfToolbarIsVertical(bar: JToolBar, component: JComponent) {
        bar.addPropertyChangeListener {
            if (it.propertyName == "orientation") {
                when (it.newValue) {
                    JToolBar.HORIZONTAL -> component.isVisible = true
                    JToolBar.VERTICAL -> component.isVisible = false
                }
            }
        }
    }

    private fun createBottomToolbar(): JToolBar {
        return JToolBar(JToolBar.HORIZONTAL).apply {
            isFloatable = false
            add(Box.createHorizontalGlue())
            add(JPanel().apply {
                add(JLabel("Scale: "))
                add(scaleSpinner)
            })
            add(createResetButton("CENTER") {
                val center = canvas.parent.run { Point(width / 2, height / 2) }
                val imgHalfWidth = canvas.image.width / 2
                val imgHalfHeight = canvas.image.height / 2
                it.x = center.x - imgHalfWidth
                it.y = center.y - imgHalfHeight
            })
            add(createResetButton("RESET") { it.x = 0; it.y = 0 })
        }
    }

    private fun createScaleSpinner(): JSpinner {
        return JSpinner(SpinnerNumberModel(1.0, MIN_SCALE, MAX_SCALE, SCALING_STEP)).apply {
            font = createFont(SMALL_FONT)
            editor = NumberEditor(this, "%").apply {
                textField.columns = 4
            }
            addChangeListener {
                canvas.apply {
                    scale = value as Double
                    updateSize()
                }
                this@PaintUI.requestFocus()
            }
        }
    }

    private fun createResetButton(title: String, lambda: (Point) -> Unit): JButton {
        return JButton().apply {
            text = title
            font = createFont(SMALL_FONT)
            isFocusable = false
            addActionListener {
                canvas.currentShift.apply {
                    lambda(this)
                }
                canvas.updateLocation()
            }
        }
    }

    enum class Action(val icon: ImageIcon, val tooltip: String, val instant: Boolean, val buttonSizeFactor: Double, var button: ToolbarButton? = null) {
        PENCIL(createIcon("pencil.png"), "Draw with pencil", false, 0.06),
        SHAPE(createIcon("shape.png"), "Draw shape", false, 0.06),
        FILL(createIcon("fill.png"), "Fill with color", false, 0.06),
        UNDO(createIcon("undo.png"), "Undo (Ctrl+Z)", true, 0.06),
        REDO(createIcon("redo.png"), "Redo (Ctrl+Y)", true, 0.06),
        SAVE(createIcon("save.png"), "Save (Ctrl+S)", true, 0.06),
        RESIZE_MODE(createIcon("properties.png"), "Resize mode", true, 0.02);

        companion object {
            fun forEach(lambda: (Action) -> Unit) {
                values().forEach {
                    lambda(it)
                }
            }
        }
    }

    enum class Shape(val title: String) {
        LINE("Line"),
        RECTANGLE("Rectangle"),
        OVAL("Oval"),
        RHOMBUS("Rhombus"),
        TRIANGLE("Isosceles Triangle"),
        RIGHT_TRIANGLE("Right Triangle");

        companion object {
            private val map = values().associateBy(Shape::title)
            fun find(title: String): Shape? = map[title]
        }
    }

    var undoButtonEnabled: Boolean = false
        set(value) {
            field = value
            Action.UNDO.button!!.isEnabled = value
        }

    var redoButtonEnabled: Boolean = false
        set(value) {
            field = value
            Action.REDO.button!!.isEnabled = value
        }

    private fun createSpinners(): JPanel {
        return JPanel().apply {
            layout = GridLayout().apply {
                columns = 2
                rows = 3
                vgap = 5
                hgap = 10
            }
            Property.forEach {
                it.spinner = PropertySpinner(canvas, it)
                add(it.spinner)
                add(JLabel(it.prop).apply {
                    PaintUI.addComponentListener(FontResizer(this, 0.015))
                })
            }
        }
    }

    private enum class ColorButton(val darkIcon: ImageIcon, val brightIcon: ImageIcon) {
        BORDER_COLOR_BUTTON(createIcon("pencil.png"), createIcon("pencil2.png")),
        FILLING_COLOR_BUTTON(createIcon("fill.png"), createIcon("fill2.png"))
    }

    private var colorButtonActive = ColorButton.BORDER_COLOR_BUTTON
    private fun createColorButton(type: ColorButton): DynamicColorButton {
        val button = DynamicColorButton(type.darkIcon, type.brightIcon).apply {
            addActionListener {
                updateColorButton(type)
                colorButtonActive = type
            }
            PaintUI.addComponentListener(ButtonResizer(this))
        }
        return button
    }

    private fun updateColorButton(type: ColorButton) {
        val typeIsBorder = (type == ColorButton.BORDER_COLOR_BUTTON)
        (if (typeIsBorder) borderColorButton else fillingColorButton).apply {
            updateIcon(if (typeIsBorder) canvas.borderColor else canvas.fillingColor)
        }
    }

    private fun createBorderCheckbox(): JCheckBox {
        return JCheckBox("Border").apply {
            isSelected = canvas.drawBorder
            addItemListener {
                canvas.drawBorder = (it.stateChange == ItemEvent.SELECTED)
            }
        }
    }

    private fun createFillingCheckbox(): JCheckBox {
        return JCheckBox("Filling").apply {
            isSelected = canvas.drawFilling
            addItemListener {
                canvas.drawFilling = (it.stateChange == ItemEvent.SELECTED)
            }
        }
    }

    enum class ResizeMode(val title: String) {
        EXTENSION("Image extension/compression"),
        ADD_SPACE_UP_RIGHT("Add/remove space up&right"),
        ADD_SPACE_UP_LEFT("Add/remove space up&left"),
        ADD_SPACE_DOWN_RIGHT("Add/remove space down&right"),
        ADD_SPACE_DOWN_LEFT("Add/remove space down&left");

        companion object {
            private val map = values().associateBy(ResizeMode::title)
            fun find(title: String): ResizeMode? = map[title]
        }
    }

    var resizeMode = ResizeMode.EXTENSION
        private set

    private fun createColorAndSizeProperties(bar: JToolBar): JPanel {
        return JPanel().apply {
            add(createButton(Action.RESIZE_MODE).apply {
                addActionListener {
                    try {
                        resizeMode = ResizeMode.find(
                            JOptionPane.showInputDialog(
                                this,
                                "",
                                "Resize Mode",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                ResizeMode.values().map { it.title }.toTypedArray(),
                                resizeMode.title
                            ) as String
                        )!!
                    }
                    catch (_: java.lang.NullPointerException) {}
                }
                hideIfToolbarIsVertical(bar, this)
            })
            add(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(borderColorButton)
                add(createBorderCheckbox())
            })
            add(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(fillingColorButton)
                add(createFillingCheckbox())
            })
        }.also {
            updateColorButton(ColorButton.BORDER_COLOR_BUTTON)
            updateColorButton(ColorButton.FILLING_COLOR_BUTTON)
        }
    }

    private fun createColorPicker(): JColorChooser {
        /*val swatchSize = (width * 0.005).toInt()
        val dimension = Dimension(swatchSize, swatchSize)
        UIManager.put("ColorChooser.swatchesSwatchSize", dimension)
        UIManager.put("ColorChooser.swatchesRecentSwatchSize", dimension)*/
        val colorChooser = JColorChooser().apply {
            preferredSize = Dimension((this@PaintUI.width * 0.25).toInt(), (this@PaintUI.height * 0.15).toInt())
            size = preferredSize
            chooserPanels = Array(1) { chooserPanels[0] }
            previewPanel = JPanel()
            selectionModel.addChangeListener {
                when (colorButtonActive) {
                    ColorButton.BORDER_COLOR_BUTTON -> canvas.borderColor = color
                    ColorButton.FILLING_COLOR_BUTTON -> canvas.fillingColor = color
                }
                updateColorButton(colorButtonActive)
            }
            isFocusable = false
            //setUI!!!
        }
        addComponentListener(CustomResizer {
            //colorChooser.parent.remove(colorChooser)
            //colorPicker = createColorPicker()
            //colorChooser.parent.add(colorPicker)
        })
        return colorChooser
    }

        /*fun createIcon(icon: String, size: Int): ImageIcon {
            return ImageIcon(ImageIcon(ICON_DIR + icon).image.getScaledInstance(size, size, Image.SCALE_DEFAULT))
        }*/
}