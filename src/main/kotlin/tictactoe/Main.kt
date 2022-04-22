package tictactoe

import tictactoe.controller.Controller
import tictactoe.model.Model
import tictactoe.view.ConsoleUi
import tictactoe.view.TicTacToeUi
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val ticTacToeUi = TicTacToeUi()
        ticTacToeUi.isVisible = true
    }
}