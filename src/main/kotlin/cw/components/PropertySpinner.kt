package cw.components

import cw.PaintCanvas
import cw.PaintUI
import cw.listeners.resizer.FontResizer
import javax.swing.JSpinner

const val FONT_RELATIVE_SIZE = 0.013

enum class Property(val prop: String, var spinner: PropertySpinner? = null) {
    WIDTH("Width"), HEIGHT("Height"), THICKNESS("Thickness");

    companion object {
        fun forEach(lambda: (Property) -> Unit) {
            values().forEach {
                lambda(it)
            }
        }
    }
}

class PropertySpinner(private val canvas: PaintCanvas, private val type: Property): JSpinner() {
    private var listenerEnabled = true

    init {
        PaintUI.addComponentListener(FontResizer(this, FONT_RELATIVE_SIZE))
        //(editor as DefaultEditor).textField.columns = 5
        value = canvas.run { when (type) {
            Property.WIDTH -> width
            Property.HEIGHT -> height
            Property.THICKNESS -> thickness
        }}
        addChangeListener {
            if (!listenerEnabled) return@addChangeListener
            val newVal = value as Int
            canvas.apply {
                if (newVal > 0) {
                    when (type) {
                        Property.WIDTH -> resizeImage(newVal, image.height)
                        Property.HEIGHT -> resizeImage(image.width, newVal)
                        Property.THICKNESS -> thickness = newVal
                    }
                } else {
                    value = when (type) {
                        Property.WIDTH -> image.width
                        Property.HEIGHT -> image.height
                        Property.THICKNESS -> thickness
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