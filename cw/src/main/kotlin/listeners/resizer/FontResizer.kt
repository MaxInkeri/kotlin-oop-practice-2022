package listeners.resizer

import PaintUI
import javax.swing.JComponent

// should be added as listener to JFrame
class FontResizer(private val comp: JComponent, private val factor: Double): Resizer() {
    override fun resize() {
        comp.apply {
            font = font.deriveFont((PaintUI.width * factor).toFloat())
        }
    }
}