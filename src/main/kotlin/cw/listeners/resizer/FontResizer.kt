package cw.listeners.resizer

import cw.PaintUI
import cw.utilities.PaintUtilities
import javax.swing.JComponent

// should be added as listener to JFrame
class FontResizer(private val comp: JComponent, private val factor: Double): Resizer() {
    override fun resize() {
        comp.font = PaintUtilities.createFont((PaintUI.width * factor).toFloat())
    }
}