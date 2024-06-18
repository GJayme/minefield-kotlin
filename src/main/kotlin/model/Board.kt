package org.example.model

import java.util.*
import kotlin.collections.ArrayList

enum class EventBoard { VICTORY, DEFEAT }

class Board(val rowsQuantity: Int, val columnQuantity: Int, private val mineQuantity: Int) {

    private val fields = ArrayList<ArrayList<Field>>()
    private val callbacks = ArrayList<(EventBoard) -> Unit>()

    init {
        fieldsGenerating()
        neighborsAssociating()
        randomizeMines()
    }

    private fun fieldsGenerating() {
        for (row in 0 until rowsQuantity) {
            fields.add(ArrayList())
            for (column in 0 until columnQuantity) {
                val newField = Field(row, column)
                newField.onEvent(this::checkingDefeatOrVictory)
                fields[row].add(newField)
            }
        }
    }

    private fun neighborsAssociating() {
        forEachField { neighborsAssociating(it) }
    }

    private fun neighborsAssociating(field: Field) {
        val (row, column) = field
        val rows = arrayOf(row - 1, row, row + 1)
        val columns = arrayOf(column - 1, column, column + 1)

        rows.forEach { row ->
            columns.forEach { column ->
                val actual = fields.getOrNull(row)?.getOrNull(column)
                actual?.takeIf { field != it }?.let { field.addNeighbors(it) }
            }
        }
    }

    private fun randomizeMines() {
        val random = Random()

        var sortedRow = -1
        var sortedColumn = -1
        var actualQuantityMines = 0

        while (actualQuantityMines < this.mineQuantity) {
            sortedRow = random.nextInt(rowsQuantity)
            sortedColumn = random.nextInt(columnQuantity)

            val sortedMine = fields[sortedRow][sortedColumn]
            if (sortedMine.secure) {
                sortedMine.mining()
                actualQuantityMines++
            }
        }
    }

    private fun achievedGoal(): Boolean {
        var winner = true
        forEachField { if (!it.reachedGoal) winner = false }
        return winner
    }

    private fun checkingDefeatOrVictory(field: Field, eventField: EventField) {
        if (eventField == EventField.EXPLOSION) {
            callbacks.forEach { it(EventBoard.DEFEAT) }
        } else if (achievedGoal()) {
            callbacks.forEach { it(EventBoard.VICTORY) }
        }
    }

    fun forEachField(callback: (Field) -> Unit) {
        fields.forEach { row -> row.forEach(callback) }
    }

    fun onEvent(callback: (EventBoard) -> Unit) {
        callbacks.add(callback)
    }

    fun restart() {
        forEachField { it.restart() }
        randomizeMines()
    }
}