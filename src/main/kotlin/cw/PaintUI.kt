package cw

import cw.components.*
import cw.enums.ColorButtonInfo
import cw.enums.PaintAction
import cw.enums.PropertySpinnerInfo
import cw.enums.PaintShape
import cw.listeners.resizer.FontResizer
import cw.listeners.resizer.ButtonResizer
import cw.listeners.PaintKeyListener
import cw.listeners.PaintWheelListener
import cw.listeners.resizer.CustomResizer
import cw.utilities.PaintUtilities.createFont
import java.awt.*
import java.awt.event.ItemEvent
import javax.swing.*
import javax.swing.JSpinner.NumberEditor

private const val MIN_WIDTH = 640
private const val MIN_HEIGHT = 500
private const val GAP = 10

private const val DEFAULT_WIDTH_RELATIVE = 0.6
private const val DEFAULT_HEIGHT_RELATIVE = 0.8

private const val FONT_SIZE_RELATIVE = 0.014
private const val BOTTOM_TOOLBAR_FONT = 12f

internal const val BIG_BUTTON_SIZE_FACTOR = 0.06
internal const val SMALL_BUTTON_SIZE_FACTOR = 0.035

internal const val MIN_SCALE = 0.1
internal const val MAX_SCALE = 8.0
private const val SCALING_STEP = 0.1


object PaintUI: JFrame("is not Paint") {
    internal var canvas: PaintCanvas = PaintCanvas()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        minimumSize = Dimension(MIN_WIDTH, MIN_HEIGHT)
        Toolkit.getDefaultToolkit().screenSize.apply {
            preferredSize = Dimension(
                (width * DEFAULT_WIDTH_RELATIVE).toInt(),
                (height * DEFAULT_HEIGHT_RELATIVE).toInt()
            )
        }
        size = preferredSize
        extendedState = MAXIMIZED_BOTH
    }


    /***************/
    /*** TOOLBAR ***/
    /***************/

    private val toolbar = createToolbar()

    val toolbarIsVertical
        get() = (toolbar.orientation == JToolBar.VERTICAL)

    private fun createToolbar(): JToolBar {
        return JToolBar(JToolBar.HORIZONTAL).apply {
            add(createButton(PaintAction.PENCIL))
            add(createButton(PaintAction.SHAPE))
            add(createButton(PaintAction.TEXT))
            add(createButton(PaintAction.FILL))
            add(createColorButtons())
            add(JPanel().apply {
                add(createResizeModeButton())
                layout = FlowLayout(FlowLayout.RIGHT)
            })
            add(createSpinners().apply { hideIfToolbarIsVertical(this) })
            add(createButton(PaintAction.UNDO).apply { hideIfWindowSmall(this, 750,640) })
            add(createButton(PaintAction.REDO).apply { hideIfWindowSmall(this, 860, 860) })
            add(createButton(PaintAction.SAVE))
            addPropertyChangeListener { event ->
                if (event.propertyName == "orientation") {
                    PaintAction.forEach {
                        ButtonResizer.newSize(it.button!!)
                    }
                    ColorButtonInfo.forEach {
                        ButtonResizer.newSize(it.button!!)
                    }
                }
            }
        }
    }

    private fun createButton(type: PaintAction): ToolbarButton {
        return ToolbarButton(type).also {
            type.button = it
        }
    }

    private fun hideIfToolbarIsVertical(component: JComponent) {
        SwingUtilities.invokeLater {
            toolbar.addPropertyChangeListener {
                if (it.propertyName == "orientation") {
                    when (it.newValue) {
                        JToolBar.HORIZONTAL -> component.isVisible = true
                        JToolBar.VERTICAL -> component.isVisible = false
                    }
                }
            }
        }
    }

    private fun hideIfWindowSmall(component: JComponent, minWidth: Int, minHeight: Int) {
        addComponentListener(CustomResizer {
            component.isVisible = if (toolbarIsVertical) (height >= minHeight) else (width >= minWidth)
        })
    }

    /** ACTION BUTTONS **/

    var currentAction = PaintAction.PENCIL
        internal set
    var currentShape = PaintShape.LINE
        internal set

    var undoButtonEnabled: Boolean = false
        set(value) {
            field = value
            PaintAction.UNDO.button!!.isEnabled = value
        }

    var redoButtonEnabled: Boolean = false
        set(value) {
            field = value
            PaintAction.REDO.button!!.isEnabled = value
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

    private fun createResizeModeButton(): ToolbarButton {
        return createButton(PaintAction.RESIZE_MODE).apply {
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
            hideIfToolbarIsVertical(this)
        }
    }

    /** SPINNERS **/

    private fun createSpinners(): JPanel {
        return JPanel().apply {
            layout = GridLayout().apply {
                columns = 2
                rows = 3
                vgap = 5
                hgap = 10
            }
            PropertySpinnerInfo.forEach {
                it.spinner = PropertySpinner(canvas, it)
                add(it.spinner)
                add(JLabel(it.prop).apply {
                    PaintUI.addComponentListener(FontResizer(this, FONT_SIZE_RELATIVE))
                })
            }
        }
    }

    /** COLOR BUTTONS **/

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

    private fun createColorButtons(): JPanel {
        return JPanel().apply {
            add(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(ColorButtonInfo.BORDER_COLOR_BUTTON.run { button = ColorButton(this); button })
                add(createBorderCheckbox())
            })
            add(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(ColorButtonInfo.FILLING_COLOR_BUTTON.run { button = ColorButton(this); button })
                add(createFillingCheckbox())
            })
        }
    }


    /**********************/
    /*** BOTTOM TOOLBAR ***/
    /**********************/

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
                val imgHalfWidth = (canvas.image.width * canvas.scale / 2).toInt()
                val imgHalfHeight = (canvas.image.height * canvas.scale / 2).toInt()
                it.x = center.x - imgHalfWidth
                it.y = center.y - imgHalfHeight
            })
            add(createResetButton("RESET") { it.x = 0; it.y = 0 })
        }
    }

    private val scaleSpinner = createScaleSpinner()

    private fun createScaleSpinner(): JSpinner {
        return JSpinner(SpinnerNumberModel(1.0, MIN_SCALE, MAX_SCALE, SCALING_STEP)).apply {
            font = createFont(BOTTOM_TOOLBAR_FONT)
            editor = NumberEditor(this, "%").apply {
                textField.columns = 3
            }
            addChangeListener {
                canvas.apply {
                    scale = value as Double
                }
                PaintUI.requestFocus()
            }
        }
    }

    private fun createResetButton(title: String, lambda: (Point) -> Unit): JButton {
        return JButton().apply {
            text = title
            font = createFont(BOTTOM_TOOLBAR_FONT)
            isFocusable = false
            addActionListener {
                canvas.currentShift.apply {
                    lambda(this)
                }
                canvas.updateLocation()
            }
        }
    }


    /*****************/
    /*** CREATE UI ***/
    /*****************/

    init {
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
}