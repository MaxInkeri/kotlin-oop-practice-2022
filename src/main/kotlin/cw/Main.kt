package cw

import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        /*val ui = PaintUI()
        ui.isVisible = true*/
        PaintUI.isVisible = true
    }
}