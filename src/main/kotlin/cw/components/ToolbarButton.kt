package cw.components

import cw.PaintUI
import cw.enums.PaintAction
import cw.enums.PaintShape
import cw.listeners.resizer.ButtonResizer
import cw.utilities.PaintSaveManager
import cw.utilities.PaintUtilities
import org.drjekyll.fontchooser.FontDialog
import java.awt.Color
import javax.swing.ImageIcon
import javax.swing.JOptionPane
import javax.swing.WindowConstants

class ToolbarButton(private val type: PaintAction): ResizableButton(type.buttonSizeFactor) {

    private val iconOriginal: ImageIcon

    init {
        toolTipText = type.tooltip
        isFocusable = false
        iconOriginal = type.icon
        icon = iconOriginal
        if (type == PaintUI.currentAction) {
            background = Color.LIGHT_GRAY
        }
        if (type == PaintAction.UNDO || type == PaintAction.REDO) {
            isEnabled = false
        }

        PaintUI.addComponentListener(ButtonResizer(this))

        addActionListener {
            if (!type.instant) {
                PaintUI.currentAction = type
                PaintAction.forEach {
                    it.button!!.background = null
                }
                background = Color.LIGHT_GRAY
            }
            when (type) {
                PaintAction.SHAPE -> {
                    try {
                        PaintUI.currentShape = PaintShape.find(
                            JOptionPane.showInputDialog(
                            this,
                            "",
                            "Shape",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            PaintShape.values().map { it.title }.toTypedArray(),
                            PaintUI.currentShape.title
                        ) as String)!!
                    }
                    catch (_: java.lang.NullPointerException) {}
                }
                PaintAction.TEXT -> {
                    FontDialog(PaintUI, "Font", true).apply {
                        selectedFont = PaintUI.canvas.textFont
                        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
                        isVisible = true
                        if (!isCancelSelected) {
                            PaintUI.canvas.updateFont(selectedFont)
                        }
                    }
                }
                PaintAction.UNDO -> PaintUI.canvas.undo()
                PaintAction.REDO -> PaintUI.canvas.redo()
                PaintAction.SAVE -> PaintSaveManager.save()
                else -> {}
            }
        }
    }

    override fun resizeIcon(size: Int) {
        icon = PaintUtilities.resizeIcon(iconOriginal, size)
    }
}