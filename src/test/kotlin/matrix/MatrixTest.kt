package matrix

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

internal class MatrixTest {
    @Test
    fun constructorInitArrayExceptions() {
        val badArray1 = arrayOf(
            doubleArrayOf(1.0, 22.0),
            doubleArrayOf(1.0)
        )

        val badArray2 = arrayOf(doubleArrayOf())

        val badArray3 = arrayOf<DoubleArray>()

        assertFailsWith(IllegalStateException::class)  {
            Matrix(badArray1)
        }

        assertFailsWith(IllegalStateException::class) {
            Matrix(badArray2)
        }

        assertFailsWith(IllegalStateException::class) {
            Matrix(badArray3)
        }
    }

    @Test
    fun constructorEncapsulation() {
        val array = arrayOf(
            doubleArrayOf(1.1, 2.0),
            doubleArrayOf(3.0, 4.0),
        )

        val B = Matrix(array)
        array[0][0] = 3.3
        assertEquals(1.1, B[0, 0])
    }

    @Test
    fun constructor() {
        val B = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
                doubleArrayOf(3.0, 4.0),
            )
        )
        assertEquals(1.0, B[0, 0])
        assertEquals(2.0, B[0, 1])
        assertEquals(3.0, B[1, 0])
        assertEquals(4.0, B[1, 1])
        assertEquals(2, B.height)
        assertEquals(2, B.width)
    }

    @Test
    fun constructor2() {
        val B = Matrix(2, 2, 666.6)
        assertEquals(666.6, B[0, 0])
        assertEquals(666.6, B[0, 1])
        assertEquals(666.6, B[1, 0])
        assertEquals(666.6, B[1, 1])
        assertEquals(2, B.height)
        assertEquals(2, B.width)
    }

    @Test
    fun plus() {
        val A = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
                doubleArrayOf(3.0, 4.0),
            )
        )
        val B = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
                doubleArrayOf(3.0, 4.0),
            )
        )
        val C = Matrix(
            arrayOf(
                doubleArrayOf(2.0, 4.0),
                doubleArrayOf(6.0, 8.0),
            )
        )
        assertEquals(C, A + B)
    }

    @Test
    fun plusFail() {
        val A = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
                doubleArrayOf(3.0, 4.0),
            )
        )
        val B = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
            )
        )
        assertFailsWith(IllegalStateException::class) {
            A + B
        }
    }

    @Test
    fun minus() {
    }

    @Test
    fun div() {
    }

    @Test
    fun times() {
        val A = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
                doubleArrayOf(3.0, 4.0),
            )
        )
        val B = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
                doubleArrayOf(3.0, 4.0),
            )
        )
        val C = Matrix(
            arrayOf(
                doubleArrayOf(7.0, 10.0),
                doubleArrayOf(15.0, 22.0),
            )
        )
        assertEquals(C, A * B)
    }

    @Test
    fun timesForVectors() {
        val A = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 0.0, 3.0)
            )
        )
        val B = Matrix(
            arrayOf(
                doubleArrayOf(1.0),
                doubleArrayOf(2.0),
                doubleArrayOf(0.0),
            )
        )
        val AB = Matrix(arrayOf(doubleArrayOf(1.0)))
        val BA = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 0.0, 3.0),
                doubleArrayOf(2.0, 0.0, 6.0),
                doubleArrayOf(0.0, 0.0, 0.0),
            )
        )

        assertEquals(AB, A * B)
        assertEquals(BA, B*A)
    }

    @Test
    fun timesFail() {
        val A = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
                doubleArrayOf(3.0, 4.0),
            )
        )
        val B = Matrix(
            arrayOf(
                doubleArrayOf(1.0, 2.0),
            )
        )
        assertFailsWith(IllegalStateException::class) {
            A * B
        }
    }

    @Test
    fun testTimes() {
    }

    @Test
    operator fun unaryMinus() {
    }

    @Test
    operator fun unaryPlus() {
    }
}