package cw.listeners.resizer

class CustomResizer(private val lambda: () -> Unit): Resizer() {
    override fun resize() {
        lambda()
    }
}
