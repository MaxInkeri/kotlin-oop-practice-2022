package lab2

import kotlin.math.PI

data class Circle(
    val r: Double,
    override val borderColor: Color = Color(0, 0, 0),
    override val fillColor: Color = Color(255, 255, 255)
) : ColoredShape2d {
    init {
        if (r <= 0) {
            error("Radius must be positive")
        }
    }

    override fun calcArea(): Double {
        return PI * r * r
    }
}