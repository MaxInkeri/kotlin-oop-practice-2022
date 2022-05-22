package cw.enums

import cw.BIG_BUTTON_SIZE_FACTOR
import cw.SMALL_BUTTON_SIZE_FACTOR
import cw.components.ToolbarButton
import cw.utilities.PaintUtilities
import javax.swing.ImageIcon

enum class PaintAction(val icon: ImageIcon, val tooltip: String, val instant: Boolean, val buttonSizeFactor: Double, var button: ToolbarButton? = null) {
    PENCIL(PaintUtilities.createIcon("pencil.png"), "Draw with pencil", false, BIG_BUTTON_SIZE_FACTOR),
    SHAPE(PaintUtilities.createIcon("shape.png"), "Draw shape", false, BIG_BUTTON_SIZE_FACTOR),
    TEXT(PaintUtilities.createIcon("text.png"), "Print text", false, BIG_BUTTON_SIZE_FACTOR),
    FILL(PaintUtilities.createIcon("fill.png"), "Fill with color", false, BIG_BUTTON_SIZE_FACTOR),
    UNDO(PaintUtilities.createIcon("undo.png"), "Undo (Ctrl+Z)", true, BIG_BUTTON_SIZE_FACTOR),
    REDO(PaintUtilities.createIcon("redo.png"), "Redo (Ctrl+Y)", true, BIG_BUTTON_SIZE_FACTOR),
    SAVE(PaintUtilities.createIcon("save.png"), "Save (Ctrl+S)", true, BIG_BUTTON_SIZE_FACTOR),
    RESIZE_MODE(PaintUtilities.createIcon("properties.png"), "Resize mode", true, SMALL_BUTTON_SIZE_FACTOR);

    companion object {
        fun forEach(lambda: (PaintAction) -> Unit) {
            values().forEach {
                lambda(it)
            }
        }
    }
}