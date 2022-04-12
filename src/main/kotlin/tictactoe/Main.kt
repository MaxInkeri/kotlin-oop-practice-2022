package tictactoe

import tictactoe.controller.Controller
import tictactoe.model.Model
import tictactoe.view.ConsoleUi

fun main() {
    val model = Model()
    ConsoleUi(model)
    Controller(model)
}