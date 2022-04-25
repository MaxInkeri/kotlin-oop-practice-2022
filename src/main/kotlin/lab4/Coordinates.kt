package lab4

data class Coordinates(var x: Int, var y: Int) {
    operator fun plus(increment: Coordinates): Coordinates {
        return Coordinates(x + increment.x, y + increment.y)
    }

    operator fun plusAssign(increment: Coordinates) {
        x += increment.x
        y += increment.y
    }

    fun isValidLocation(dimensions: Coordinates): Boolean {
        if (x < 0 || y < 0) return false
        if (x >= dimensions.x || y >= dimensions.y) return false
        return true
    }

    fun copy(): Coordinates {
        return Coordinates(x, y)
    }


}