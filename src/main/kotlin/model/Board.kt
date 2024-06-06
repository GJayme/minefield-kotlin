package org.example.model

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
                fields[row].add(newField)
            }
        }
    }

    private fun neighborsAssociating() {
        forEachField { neighborsAssociating(it) }
    }

    private fun neighborsAssociating(field: Field) {

    }

    private fun randomizeMines() {

    }

    fun forEachField(callback: (Field) -> Unit) {
        fields.forEach { row -> row.forEach(callback) }
    }
}