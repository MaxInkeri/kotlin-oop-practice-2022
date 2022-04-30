package lab2

@kotlinx.serialization.Serializable
data class Color(val r: Int, val g: Int, val b: Int, val a: Double = 1.0) { // 0 <= r, g, b <= 255; 0 <= a <= 1

    init {
        val range = 0..255
        if (r !in range || g !in range || b !in range) error("Incorrect RGB color")
        if (a < 0.0 || a > 1.0) error("Incorrect opacity")
    }
}