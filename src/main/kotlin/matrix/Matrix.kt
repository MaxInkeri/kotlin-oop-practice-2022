package matrix

private const val EPSILON: Double = 0.000001

class Matrix private constructor(h: Int, w: Int, initialValue: Double = 0.0, initialData: Array<DoubleArray>?) {
    val height: Int
        get() = data.size
    val width: Int
        get() = data[0].size

    private val data: Array<DoubleArray>

    constructor(h: Int, w: Int, initialValue: Double = 0.0) : this(h, w, initialValue, null)
    constructor(initialData: Array<DoubleArray>) : this(0, 0, initialData = initialData)

    init {
        if (initialData != null) {
            val n = initialData.size
            if (n == 0) error("Initial matrix data shouldn't be empty")

            val m = initialData[0].size
            if (m == 0) error("Initial matrix data shouldn't be empty")

            for (line in initialData) {
                if (line.size != m) error("Initial matrix data has wrong sizes")
            }

            data = Array(n) { i ->
                DoubleArray(m) { j ->
                    initialData[i][j]
                }
            }
        } else {
            // check h & w
            data = Array(h) { i ->
                DoubleArray(w) { j ->
                    initialValue
                }
            }
        }
    }

    operator fun get(i: Int, j: Int): Double {
        checkIndexes(i, j)
        return data[i][j]
    }

    operator fun set(i: Int, j: Int, value: Double) {
        checkIndexes(i, j)
        data[i][j] = value
    }

    private fun checkIndexes(i: Int, j: Int) {
        if (i < 0 || i > height || j < 0 || j > width)
            throw IndexOutOfBoundsException("Indexes $i,$j out of matrix bounds ${height},${width}")
    }

    private fun checkSizes(other: Matrix) {
        if (height != other.height || width != other.width)
            error("Wrong sizes")
    }

    operator fun plus(other: Matrix): Matrix {
        checkSizes(other)
        val newMatrix = Matrix(data)
        for (i in 0 until height) {
            for (j in 0 until width) {
                newMatrix[i, j] += other[i, j]
            }
        }
        return newMatrix
    }

    operator fun minus(other: Matrix): Matrix {
        checkSizes(other)
        val newMatrix = Matrix(data)
        for (i in 0 until height) {
            for (j in 0 until width) {
                newMatrix[i, j] -= other[i, j]
            }
        }
        return newMatrix
    }

    operator fun div(scalar: Double): Matrix {
        return times(1 / scalar)
    }

    operator fun times(scalar: Double): Matrix {
        val newMatrix = Matrix(data)
        for (i in 0 until height) {
            for (j in 0 until width) {
                newMatrix[i, j] *= scalar
            }
        }
        return newMatrix
    }

    operator fun times(other: Matrix): Matrix {
        if (width != other.height) error("Wrong sizes")

        val newMatrix = Matrix(height, other.width)

        for (i in 0 until height) {
            for (j in 0 until other.width) {
                for (k in 0 until width) {
                    newMatrix[i, j] += data[i][k] * other[k, j]
                }
            }
        }
        return newMatrix
    }

    operator fun unaryMinus(): Matrix {
        return times(-1.0)
    }

    operator fun unaryPlus(): Matrix {
        return Matrix(data)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix

        for ((i, row) in data.withIndex()) {
            for ((j, value) in row.withIndex()) {
                val equals = Math.abs(value - other.data[i][j]) <= EPSILON
                if (!equals) return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        return data.contentDeepHashCode()
    }

    override fun toString(): String {
        return "Matrix(data=${data.contentDeepToString()})"
    }
}