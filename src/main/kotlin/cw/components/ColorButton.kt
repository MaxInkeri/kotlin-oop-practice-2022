package cw.components

import cw.PaintUI
import cw.SMALL_BUTTON_SIZE_FACTOR
import cw.enums.ColorButtonInfo
import cw.listeners.resizer.ButtonResizer
import cw.utilities.PaintUtilities
import java.awt.Color
import javax.swing.JColorChooser

class ColorButton(private val type: ColorButtonInfo): ResizableButton(SMALL_BUTTON_SIZE_FACTOR) {

    private var darkIcon = type.darkIcon
    private var brightIcon = type.brightIcon

    init {
        isRolloverEnabled = false
        isFocusable = false
        isRequestFocusEnabled = false
        background = type.defaultColor
        addActionListener {
            try {
                updateIcon(JColorChooser.showDialog(PaintUI, type.title, background))
            }
            catch (_: java.lang.NullPointerException) {}
        }
        PaintUI.addComponentListener(ButtonResizer(this))
    }

    private val useBrightIcon: Boolean
        get() = (background.red + background.green + background.blue) < 127 * 3

    private fun updateIcon() {
        icon = if (useBrightIcon) brightIcon else darkIcon
    }

    private fun updateIcon(color: Color) {
        background = color
        updateIcon()
    }

    override fun resizeIcon(size: Int) {
        darkIcon = PaintUtilities.resizeIcon(type.darkIcon, size)
        brightIcon = PaintUtilities.resizeIcon(type.brightIcon, size)
        updateIcon()
    }
}