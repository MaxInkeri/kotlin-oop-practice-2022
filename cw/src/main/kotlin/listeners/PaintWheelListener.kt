package listeners

import MAX_SCALE
import MIN_SCALE
import PaintCanvas
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
                var newScale = scale - SCALING_VELOCITY * rotation
                if (newScale > MAX_SCALE) newScale = MAX_SCALE
                if (newScale < MIN_SCALE) newScale = MIN_SCALE
                scaleSpinner.value = newScale
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