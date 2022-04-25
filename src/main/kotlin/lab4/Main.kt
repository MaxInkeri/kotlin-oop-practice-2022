package lab4

import lab4.view.LabyrinthUI
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val labyrinthUI = LabyrinthUI()
        labyrinthUI.isVisible = true
    }
}