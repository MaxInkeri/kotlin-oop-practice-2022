package cw.listeners.resizer

import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.SwingUtilities

abstract class Resizer: ComponentAdapter() {
    override fun componentResized(e: ComponentEvent?) {
        super.componentResized(e)
        resize()
    }

    protected abstract fun resize()

    init {
        SwingUtilities.invokeLater {
            resize()
        }
    }
}