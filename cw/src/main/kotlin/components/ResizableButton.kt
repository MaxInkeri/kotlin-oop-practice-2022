package components

import javax.swing.JButton

abstract class ResizableButton(val sizeFactor: Double): JButton() {
    abstract fun resizeIcon(size: Int)
}