package cw.listeners

import cw.PaintCanvas
import cw.utilities.PaintSaveManager
import java.awt.Point
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

private const val SHIFT_VELOCITY = 5

class PaintKeyListener(private val canvas: PaintCanvas): KeyListener {

    override fun keyTyped(e: KeyEvent?) {}

    override fun keyPressed(e: KeyEvent?) {
        if (e == null) return
        val shift = when (e.keyCode) {
            KeyEvent.VK_RIGHT -> Point(-SHIFT_VELOCITY, 0)
            KeyEvent.VK_UP -> Point(0, SHIFT_VELOCITY)
            KeyEvent.VK_LEFT -> Point(SHIFT_VELOCITY, 0)
            KeyEvent.VK_DOWN -> Point(0, -SHIFT_VELOCITY)
            else -> null
        }
        if (shift != null) {
            canvas.currentShift.apply {
                x += shift.x
                y += shift.y
            }
            canvas.updateLocation()
            return
        }
        if (e.isControlDown) {
            when (e.keyCode) {
                KeyEvent.VK_S -> PaintSaveManager.save()
                KeyEvent.VK_C -> canvas.copyImage()
                KeyEvent.VK_V -> canvas.pasteImage()
                KeyEvent.VK_Z -> canvas.undo()
                KeyEvent.VK_Y -> canvas.redo()
                else -> {}
            }
        }
    }

    override fun keyReleased(e: KeyEvent?) {}
}
