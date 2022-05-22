package cw.listeners.resizer

import cw.PaintUI
import cw.components.ResizableButton

const val FACTOR_TOOLBAR_VERTICAL = 1.5

// should be added as listener to JFrame
class ButtonResizer(private val button: ResizableButton): Resizer() {
    override fun resize() {
        newSize(button)
    }

    companion object {
        fun newSize(button: ResizableButton) {
            button.resizeIcon((PaintUI.run { if (toolbarIsVertical) (height * FACTOR_TOOLBAR_VERTICAL).toInt() else width } * button.sizeFactor).toInt())
        }
    }
}