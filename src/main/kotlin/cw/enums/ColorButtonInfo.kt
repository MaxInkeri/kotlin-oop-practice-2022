package cw.enums

import cw.components.ColorButton
import cw.utilities.PaintUtilities
import java.awt.Color
import javax.swing.ImageIcon

enum class ColorButtonInfo(val title: String, val defaultColor: Color, val darkIcon: ImageIcon, val brightIcon: ImageIcon, var button: ColorButton? = null) {
    BORDER_COLOR_BUTTON("Border Color", Color.BLACK, PaintUtilities.createIcon("pencil.png"), PaintUtilities.createIcon("pencil2.png")),
    FILLING_COLOR_BUTTON("Filling Color", Color.LIGHT_GRAY, PaintUtilities.createIcon("fill.png"), PaintUtilities.createIcon("fill2.png"));

    companion object {
        fun forEach(lambda: (ColorButtonInfo) -> Unit) {
            values().forEach {
                lambda(it)
            }
        }
    }
}