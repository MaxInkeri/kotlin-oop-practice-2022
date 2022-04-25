package lab4.controller

import lab4.model.LabyrinthModel
import lab4.model.LabyrinthModel.Direction
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class LabyrinthController(private val model: LabyrinthModel): KeyListener {
    override fun keyTyped(e: KeyEvent?) {
        when(e!!.keyChar) {
            'w' -> model.move(Direction.UP)
            'a' -> model.move(Direction.LEFT)
            's' -> model.move(Direction.DOWN)
            'd' -> model.move(Direction.RIGHT)
        }
    }

    override fun keyPressed(e: KeyEvent?) {

    }

    override fun keyReleased(e: KeyEvent?) {

    }
}