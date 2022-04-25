package lab4.view

import lab4.controller.LabyrinthController
import lab4.model.LabyrinthModel
import lab4.model.ModelListener
import java.awt.*
import java.io.File
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.filechooser.FileFilter
import kotlin.math.min
import kotlin.system.exitProcess

private const val GAP = 10
private val cellColor = Color.LIGHT_GRAY
private val locationColor = Color.CYAN

class LabyrinthUI: JFrame("Labyrinth"), ModelListener {
    private lateinit var model: LabyrinthModel
    private lateinit var controller: LabyrinthController
    private val fieldCells = mutableListOf<MutableList<JButton>>()
    private val fileChooser = JFileChooser("./fields/")
    class FieldFilter: FileFilter() {
        override fun accept(f: File): Boolean {
            return f.isDirectory || f.extension == "field"
        }

        override fun getDescription(): String {
            return ".field"
        }
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isFocusable = true
        fileChooser.fileFilter = FieldFilter()
        openField()
    }

    private var lastSelectedFile: File? = null

    private fun openField() {
        when (fileChooser.showOpenDialog(this)) {
            JFileChooser.APPROVE_OPTION -> {
                if (lastSelectedFile == fileChooser.selectedFile) { // the same field
                    model.reset()
                    return
                }
                if (lastSelectedFile != null) { // another field
                    model.removeModelListener(this)
                    removeKeyListener(controller)
                }
                resetField(true)
                model = LabyrinthModel(fileChooser.selectedFile)
                controller = LabyrinthController(model)
                lastSelectedFile = fileChooser.selectedFile
                val screenSize = Toolkit.getDefaultToolkit().screenSize
                val cellSize = min((screenSize.height.toDouble() - 160) / model.fieldSize.y, screenSize.width.toDouble() / model.fieldSize.x).toInt()
                setSize(cellSize * model.fieldSize.x, cellSize * model.fieldSize.y + 80)
                rootPane.contentPane = JPanel(BorderLayout(GAP, GAP)).apply {
                    border = BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP)
                    add(createGameField(cellSize), BorderLayout.CENTER)
                    add(createRestartButton(), BorderLayout.SOUTH)
                }
                model.addModelListener(this)
                addKeyListener(controller)
                requestFocus()
            }
            JFileChooser.CANCEL_OPTION -> exitProcess(0)
        }
    }

    private fun createRestartButton(): Component {
        val button = JButton("Restart").apply {
            val derived = font.deriveFont(60f)
            font = derived
            isFocusable = false
        }
        button.addActionListener {
            askForRestart()
        }
        return button
    }

    private fun createGameField(cellSize: Int): Component {
        val field = JPanel(GridLayout(model.fieldSize.y, model.fieldSize.x))
        val border = BorderFactory.createBevelBorder(0)
        for (i in 0 until model.fieldSize.y) {
            val line = mutableListOf<JButton>()
            for (j in 0 until model.fieldSize.x) {
                val cell = JButton("")
                cell.isVisible = !model.field[i][j]
                cell.isEnabled = false
                cell.border = border
                cell.background = cellColor
                cell.apply {
                    val derived = font.deriveFont(cellSize * 0.8f)
                    font = derived
                }
                line.add(cell)
                field.add(cell)
            }
            fieldCells.add(line)
        }
        fieldCells[model.location!!.y][model.location!!.x].background = locationColor
        fieldCells[model.start!!.y][model.start!!.x].text = "S"
        fieldCells[model.finish!!.y][model.finish!!.x].text = "F"
        return field
    }

    private fun resetField(fullReset: Boolean = false) {
        fieldCells.forEach { it ->
            it.forEach {
                if (fullReset) it.removeAll()
                else it.background = cellColor
            }
        }
        if (fullReset) fieldCells.clear()
    }

    override fun onMoveDone() {
        resetField()
        fieldCells[model.location!!.y][model.location!!.x].background = locationColor
    }

    override fun onFinish() {
        askForRestart(true)
    }

    private fun askForRestart(finished: Boolean = false) {
        val result = JOptionPane.showConfirmDialog(this, "Start new one?", if (finished) "Game is finished" else "Game is not finished", JOptionPane.YES_NO_OPTION)
        if (finished && result == 1) { // exit game
            exitProcess(0)
        }
        if (!finished && result == 1) { // continue game
            return
        }
        // restart game
        openField()
    }
}