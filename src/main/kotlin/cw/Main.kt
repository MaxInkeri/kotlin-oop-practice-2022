package cw

import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        PaintUI.isVisible = true
    }
}