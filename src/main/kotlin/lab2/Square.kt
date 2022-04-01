package lab2

class Square(val a: Double, override val borderColor: Color = Color(0, 0, 0), override val fillColor: Color = Color(255, 255, 255)) : ColoredShape2d {
    override fun calcArea(): Double {
        return a * a
    }
}