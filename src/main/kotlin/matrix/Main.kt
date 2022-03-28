package matrix.demo

import matrix.Matrix

fun main() {
    val A = Matrix(
        arrayOf(
            doubleArrayOf(1.1, 2.0),
            doubleArrayOf(3.0, 4.0),
        )
    )

    val B = Matrix(
        arrayOf(
            doubleArrayOf(1.0, 2.0),
            doubleArrayOf(3.0, 4.0),
        )
    )

    val C = A + B
    val D = A - B

    val h = A.height
    val w = A.width

    val E = A / 3.333
    val H = A * 3.333

    val F = A * B

    val G = -A

    val J = +A

    println(J == A)

    println(A)

    val matrix3x3 = Matrix(3, 3, 0.0)

}