package cw.listeners.resizer

import cw.PaintUI
import cw.components.ResizableButton

const val FACTOR_TOOLBAR_VERTICAL = 1.5

// should be added as listener to JFrame
class ButtonResizer(private val button: ResizableButton): Resizer() {
    override fun resize() {
        //button.icon = PaintUI.createIcon(icon, (frame.width * FACTOR).toInt())
        newSize(button)
        /*if (button is DynamicColorButton) {
            button.resizeIcon(size)
        }
        else {
            button.icon = PaintUI.resizeIcon(button.icon as ImageIcon, size)
        }*/
        //button.icon = ImageIcon((button.icon as ImageIcon).image.getScaledInstance(size, size, Image.SCALE_DEFAULT))
        /*val img = (button.icon as ImageIcon).image
        val newScale = (frame.width * FACTOR) / img.getWidth(null)
        (img.graphics as Graphics2D).apply {
            scale(newScale, newScale)
        }*/
    }

    companion object {
        fun newSize(button: ResizableButton) {
            button.resizeIcon((PaintUI.run { if (toolbarIsVertical) (height * FACTOR_TOOLBAR_VERTICAL).toInt() else width } * button.sizeFactor).toInt())
        }
    }
}