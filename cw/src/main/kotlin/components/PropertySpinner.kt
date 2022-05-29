package components

import PaintCanvas
import PaintUI
import enums.PropertySpinnerInfo
import listeners.resizer.FontResizer
import javax.swing.JSpinner

const val FONT_RELATIVE_SIZE = 0.013

class PropertySpinner(private val canvas: PaintCanvas, private val type: PropertySpinnerInfo): JSpinner() {
    private var listenerEnabled = true

    init {
        PaintUI.addComponentListener(FontResizer(this, FONT_RELATIVE_SIZE))
        value = canvas.run { when (type) {
            PropertySpinnerInfo.WIDTH -> width
            PropertySpinnerInfo.HEIGHT -> height
            PropertySpinnerInfo.THICKNESS -> thickness
        }}
        addChangeListener {
            if (!listenerEnabled) return@addChangeListener
            val newVal = value as Int
            canvas.apply {
                if (newVal > 0) {
                    when (type) {
                        PropertySpinnerInfo.WIDTH -> resizeImage(newVal, image.height)
                        PropertySpinnerInfo.HEIGHT -> resizeImage(image.width, newVal)
                        PropertySpinnerInfo.THICKNESS -> thickness = newVal
                    }
                } else {
                    value = when (type) {
                        PropertySpinnerInfo.WIDTH -> image.width
                        PropertySpinnerInfo.HEIGHT -> image.height
                        PropertySpinnerInfo.THICKNESS -> thickness
                    }
                }
            }
            PaintUI.requestFocus()
            PaintUI.rootPane.contentPane.revalidate()
        }
    }

    fun setValue(newValue: Int) { // correct way to change value from code bypassing the listener
        listenerEnabled = false
        value = newValue
        listenerEnabled = true
    }
}