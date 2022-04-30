package lab2

import kotlin.math.sin

@kotlinx.serialization.Serializable
data class Triangle(
    val a: Double,
    val b: Double,
    val angle: Double,
    override val borderColor: Color = Color(0, 0, 0),
    override val fillColor: Color = Color(255, 255, 255)
) : ColoredShape2d {
    init {
        if (a <= 0 || b <= 0) {
            error("Side lengths must be positive")
        }
        if (angle <= 0 || angle >= Math.PI) {
            error("Angle must be between 0 and PI radians (0..180 degrees)")
        }
    }

    override fun calcArea(): Double {
        return a * b * sin(angle) / 2
    }
}