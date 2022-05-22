package cw.components

import cw.PaintUI
import cw.listeners.resizer.ButtonResizer
import cw.utilities.PaintSaveManager
import cw.utilities.PaintUtilities
import java.awt.Color
import javax.swing.ImageIcon
import javax.swing.JOptionPane

class ToolbarButton(private val type: PaintUI.Action): ResizableButton(type.buttonSizeFactor) {

    private val iconOriginal: ImageIcon

    init {
        toolTipText = type.tooltip
        isFocusable = false
        iconOriginal = type.icon
        icon = iconOriginal
        if (type == PaintUI.currentAction) {
            background = Color.LIGHT_GRAY
        }
        if (type == PaintUI.Action.UNDO || type == PaintUI.Action.REDO) {
            isEnabled = false
        }

        PaintUI.addComponentListener(ButtonResizer(this))

        addActionListener {
            if (!type.instant) {
                PaintUI.currentAction = type
                PaintUI.Action.forEach {
                    it.button!!.background = null
                }
                background = Color.LIGHT_GRAY
            }
            when (type) {
                PaintUI.Action.SHAPE -> {
                    try {
                        PaintUI.currentShape = PaintUI.Shape.find(
                            JOptionPane.showInputDialog(
                            this,
                            "",
                            "Shape",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            PaintUI.Shape.values().map { it.title }.toTypedArray(),
                            PaintUI.currentShape.title
                        ) as String)!!
                    }
                    catch (_: java.lang.NullPointerException) {}
                }
                PaintUI.Action.UNDO -> PaintUI.canvas.undo()
                PaintUI.Action.REDO -> PaintUI.canvas.redo()
                PaintUI.Action.SAVE -> PaintSaveManager.save()
                else -> {}
            }
        }
    }

    override fun resizeIcon(size: Int) {
        icon = PaintUtilities.resizeIcon(iconOriginal, size)
    }
}