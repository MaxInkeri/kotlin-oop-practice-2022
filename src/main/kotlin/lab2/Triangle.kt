package lab2

import kotlin.math.sin

class Triangle(val a: Double, val b: Double, val angle: Double, override val borderColor: Color = Color(0, 0, 0), override val fillColor: Color = Color(255, 255, 255)) : ColoredShape2d {
    override fun calcArea(): Double {
        return a * b * sin(angle) / 2
    }
}