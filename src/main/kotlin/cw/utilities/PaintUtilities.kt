package cw.utilities

import java.awt.Font
import java.awt.Image
import java.awt.font.TextAttribute
import javax.swing.ImageIcon

private const val ICON_DIR = "./src/main/resources/icons/"

object PaintUtilities {

    fun createFont(fontSize: Float): Font {
        return Font(mapOf(
            TextAttribute.FAMILY to Font.MONOSPACED,
            TextAttribute.WEIGHT to TextAttribute.WEIGHT_BOLD,
            TextAttribute.SIZE to fontSize
        ))
    }

    fun createIcon(icon: String): ImageIcon {
        return ImageIcon(ICON_DIR + icon)
    }

    fun resizeIcon(icon: ImageIcon, size: Int): ImageIcon {
        return ImageIcon(icon.image.getScaledInstance(size, size, Image.SCALE_DEFAULT))
    }
}