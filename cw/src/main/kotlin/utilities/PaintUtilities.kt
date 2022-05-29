package utilities

import PaintUI
import java.awt.Font
import java.awt.font.TextAttribute
import java.awt.image.BufferedImage
import javax.swing.ImageIcon

private const val ICON_DIR = "/icons/"

object PaintUtilities {

    fun createFont(fontSize: Float): Font {
        return Font(mapOf(
            TextAttribute.WEIGHT to TextAttribute.WEIGHT_BOLD,
            TextAttribute.SIZE to fontSize
        ))
    }

    fun createIcon(icon: String): ImageIcon {
        return ImageIcon(PaintUI::class.java.getResource("$ICON_DIR$icon"))
    }

    fun resizeIcon(icon: ImageIcon, size: Int): ImageIcon {
        val newIcon = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
        newIcon.createGraphics().apply {
            drawImage(icon.image, 0, 0, size, size, null)
        }
        return ImageIcon(newIcon)
    }
}