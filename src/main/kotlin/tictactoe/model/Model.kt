package tictactoe.model

import tictactoe.model.Cell.*

enum class Cell(private val textValue: String) {
    X("X"),
    O("O"),
    EMPTY(" ");

    override fun toString(): String = textValue
}

enum class State(val textValue: String) {
    X_MOVE("Waiting for X move..."),
    O_MOVE("Waiting for O move..."),
    X_WIN("Game finished. X win, congrats!!!"),
    O_WIN("Game finished. O win, congrats!!!"),
    DRAW("Game finished. Draw")
}

const val BOARD_SIZE = 3
private val FIRST_MOVE = State.X_MOVE

val GAME_NOT_FINISHED = setOf(State.O_MOVE, State.X_MOVE)

interface ModelChangeListener {
    fun onModelChanged()
}

class Model {
    private val _board: MutableList<MutableList<Cell>> = initEmptyBoard()
    val board: List<List<Cell>>
        get() = _board

    var state: State = FIRST_MOVE
        private set

    private val listeners: MutableSet<ModelChangeListener> = mutableSetOf()

    fun addModelChangeListener(listener: ModelChangeListener) {
        listeners.add(listener)
    }

    fun removeModelChangeListener(listener: ModelChangeListener) {
        listeners.remove(listener)
    }

    fun doMove(col: Int, row: Int) {

        require(state == State.O_MOVE || state == State.X_MOVE) { "Game finished" }

        require(col in 0 until BOARD_SIZE) { "Wrong column" }
        require(row in 0 until BOARD_SIZE) { "Wrong row" }

        require(board[row][col] == EMPTY) { "Wrong move" }

        // update board
        _board[row][col] = if (state == State.X_MOVE) X else O

        // check win
        val player = if (state == State.X_MOVE) X else O

        if (checkWin(player)) {
            state = when (player) {
                X -> State.X_WIN
                O -> State.O_WIN
                else -> error("Player can not be empty")
            }
        } else {

            if (checkDraw()) {
                state = State.DRAW
            } else {
                state = when (state) {
                    State.X_MOVE -> State.O_MOVE
                    State.O_MOVE -> State.X_MOVE
                    else -> state
                }
            }
        }

        notifyListeners()
    }

    private fun notifyListeners() {
        listeners.forEach { it.onModelChanged() }
    }

    private fun checkDraw(): Boolean {
        if (state != State.X_MOVE && state != State.O_MOVE) return false
        return board.flatten().none { it == EMPTY }
    }

    private fun checkWin(player: Cell): Boolean {
        require(player != EMPTY) { "Player cannot be empty" }

        return isRowWin(player) ||
                isColWin(player) ||
                isFirstDiagWin(player) ||
                isSecondDiagWin(player)
    }

    private fun isSecondDiagWin(player: Cell) = (0 until BOARD_SIZE).all { index ->
        board[index][BOARD_SIZE - 1 - index] == player
    }

    private fun isFirstDiagWin(player: Cell) = (0 until BOARD_SIZE).all { index ->
        board[index][index] == player
    }

    private fun isColWin(player: Cell) = (0 until BOARD_SIZE).any { colIndex ->
        (0 until BOARD_SIZE).all { rowIndex ->
            board[rowIndex][colIndex] == player
        }
    }

    private fun isRowWin(player: Cell): Boolean = board.any { row -> row.all { it == player } }

    private fun initEmptyBoard() = MutableList(BOARD_SIZE) {
        MutableList(BOARD_SIZE) { EMPTY }
    }

    override fun toString(): String {
        return buildString {
            append(state).appendLine()

            board.forEach {
                append(it).appendLine()
            }
        }
    }
}