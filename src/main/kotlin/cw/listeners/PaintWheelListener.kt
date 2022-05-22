package cw.listeners

import cw.MAX_SCALE
import cw.MIN_SCALE
import cw.PaintCanvas
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import javax.swing.JSpinner

private const val SHIFT_VELOCITY = 10
private const val SCALING_VELOCITY = 0.05

class PaintWheelListener(private val canvas: PaintCanvas, private val scaleSpinner: JSpinner): MouseWheelListener {
    override fun mouseWheelMoved(e: MouseWheelEvent?) {
        if (e == null) return
        val rotation = e.wheelRotation
        canvas.apply {
            if (e.isControlDown) {
                scale -= SCALING_VELOCITY * rotation
                if (scale > MAX_SCALE) scale = MAX_SCALE
                if (scale < MIN_SCALE) scale = MIN_SCALE
                scaleSpinner.value = scale
            } else if (e.isShiftDown) {
                currentShift.x -= SHIFT_VELOCITY * rotation
                updateLocation()
            } else {
                currentShift.y -= SHIFT_VELOCITY * rotation
                updateLocation()
            }
        }
    }
}