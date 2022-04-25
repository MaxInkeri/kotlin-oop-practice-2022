package lab4.model

import lab4.Coordinates
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.system.exitProcess

interface ModelListener {
    fun onMoveDone()
    fun onFinish()
}

class LabyrinthModel(file: File) {

    private val dimensions: Coordinates = Coordinates(0, 0) // width and height of game field
    val fieldSize: Coordinates
        get() = dimensions

    var start: Coordinates? = null // initial location
        private set

    var location: Coordinates? = null // current location
        private set

    var finish: Coordinates? = null // target location
        private set

    private val walls: MutableList<List<Boolean>> = mutableListOf()
    val field: List<List<Boolean>>
        get() = walls.toList()

    private val listeners: MutableSet<ModelListener> = mutableSetOf()

    init {
        readFile(file)
    }

    fun addModelListener(listener: ModelListener) {
        listeners.add(listener)
    }

    fun removeModelListener(listener: ModelListener) {
        listeners.remove(listener)
    }

    private fun readLine(line: String) {
        if (line.length != dimensions.x) {
            error("Labyrinth must be square")
        }
        val lineData = mutableListOf<Boolean>()
        line.forEach {
            when (it) {
                '#' -> lineData.add(true)
                '-' -> lineData.add(false)
                'P' -> {
                    start = Coordinates(lineData.size, walls.size)
                    location = start!!.copy()
                    lineData.add(false)
                }
                'E' -> {
                    finish = Coordinates(lineData.size, walls.size)
                    lineData.add(false)
                }
                else -> error("Incorrect data in file")
            }
        }
        walls.add(lineData)
        dimensions.y++
    }

    private fun readFile(file: File) {
        try {
            FileInputStream(file).bufferedReader().use { reader ->
                reader.readLine().also {
                    dimensions.y = 0
                    dimensions.x = it.length
                    readLine(it)
                }
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    readLine(line!!)
                }
            }
        }
        catch (e: IOException) {
            exitProcess(-1)
        }
        if (location == null) {
            error("Labyrinth must contain P character - initial location")
        }
        if (finish == null) {
            error("Labyrinth must contain E character - target location")
        }
    }

    enum class Direction(val coordinates: Coordinates) {
        UP(Coordinates(0, -1)),
        DOWN(Coordinates(0, 1)),
        LEFT(Coordinates(-1, 0)),
        RIGHT(Coordinates(1, 0))
    }
    fun move(dir: Direction) {
        val newLocation = location!!.copy()
        newLocation += dir.coordinates
        if (!newLocation.isValidLocation(dimensions)) return
        if (walls[newLocation.y][newLocation.x]) return
        location = newLocation
        listeners.forEach { it.onMoveDone() }
        if (location == finish) {
            listeners.forEach { it.onFinish() }
        }
    }

    fun reset() {
        location = start!!.copy()
        listeners.forEach { it.onMoveDone() }
    }
}