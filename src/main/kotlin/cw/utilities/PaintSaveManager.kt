package cw.utilities

import cw.PaintUI
import java.io.File
import java.io.IOException
import java.lang.RuntimeException
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter

object PaintSaveManager {
    private val filters = listOf(
        FileNameExtensionFilter("JPEG Image", "jpg", "jpeg"),
        FileNameExtensionFilter("PNG Image", "png"),
        FileNameExtensionFilter("BMP Image", "bmp")
    )
    private val acceptedExtensions: List<String> = mutableListOf<String>().apply {
        filters.forEach { addAll(it.extensions) }
    }
    private val fileChooser = JFileChooser().apply {
        filters.forEach {
            addChoosableFileFilter(it)
        }
        isAcceptAllFileFilterUsed = false
    }

    fun save() {
        when (fileChooser.showSaveDialog(PaintUI)) {
            JFileChooser.APPROVE_OPTION -> {
                var file = fileChooser.selectedFile
                if (!acceptedExtensions.contains(file.extension)) {
                    file = File(file.path + when (fileChooser.fileFilter) {
                        filters[0] -> ".jpg"
                        filters[1] -> ".bmp"
                        else -> throw RuntimeException()
                    })
                }
                try {
                    ImageIO.write(PaintUI.canvas.image, file.extension, file)
                }
                catch (e: IOException) {
                    JOptionPane.showMessageDialog(PaintUI, "Failed to save the image", "Error", JOptionPane.ERROR_MESSAGE)
                }
            }
            else -> {}
        }
    }
}