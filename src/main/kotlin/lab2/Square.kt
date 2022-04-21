package lab2

data class Square(
    val a: Double,
    override val borderColor: Color = Color(0, 0, 0),
    override val fillColor: Color = Color(255, 255, 255)
) : ColoredShape2d {
    init {
        if (a <= 0) {
            error("Side length must be positive")
        }
    }

    override fun calcArea(): Double {
        return a * a
    }
}