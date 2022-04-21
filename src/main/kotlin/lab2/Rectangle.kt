package lab2

data class Rectangle(
    val a: Double,
    val b: Double,
    override val borderColor: Color = Color(0, 0, 0),
    override val fillColor: Color = Color(255, 255, 255)
) : ColoredShape2d {
    init {
        if (a <= 0 || b <= 0) {
            error("Side lengths must be positive")
        }
    }

    override fun calcArea(): Double {
        return a * b
    }
}