package cw.components

import cw.utilities.PaintUtilities
import java.awt.Color
import javax.swing.ImageIcon



class DynamicColorButton(private var darkIconOriginal: ImageIcon, private var brightIconOriginal: ImageIcon): ResizableButton(0.02) {

    private var darkIcon = darkIconOriginal
    private var brightIcon = brightIconOriginal

    init {
        isRolloverEnabled = false
        isFocusable = false
        isRequestFocusEnabled = false
    }

    private val useBrightIcon: Boolean
        get() = (background.red + background.green + background.blue) < 127 * 3

    private fun updateIcon() {
        icon = if (useBrightIcon) brightIcon else darkIcon
    }

    fun updateIcon(color: Color) {
        background = color
        updateIcon()
    }

    override fun resizeIcon(size: Int) {
        darkIcon = PaintUtilities.resizeIcon(darkIconOriginal, size)
        brightIcon = PaintUtilities.resizeIcon(brightIconOriginal, size)
        updateIcon()
    }
}