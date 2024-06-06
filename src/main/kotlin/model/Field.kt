package org.example.model

enum class EventField { OPEN, MARK, UNMARK, EXPLOSION, RESTART }

data class Field(val row: Int, val column: Int) {

    private val neighbors = ArrayList<Field>()
    private val callbacks = ArrayList<(Field, EventField) -> Unit>()

    var marked: Boolean = false
    var opened: Boolean = false
    var mined: Boolean = false

    //Only read
    val unmarked: Boolean get() =!marked
    val closed: Boolean get() =!opened
    val secure: Boolean get() =!mined
    val quantityMinedNeighbors: Int get() = neighbors.filter { it.mined }.size
    val secureNeighbor: Boolean
        get() = neighbors.map { it.secure }.reduce { result, secure -> result && secure }
    val reachedGoal: Boolean get() = secure && opened || mined && marked

    fun onEvent(callback: (Field, EventField) -> Unit) {
        callbacks.add(callback)
    }

    fun addNeighbors(neighbor: Field) {
        neighbors.add(neighbor)
    }

    fun opening() {
        if (closed) {
            opened = true
            if (mined) {
                callbacks.forEach { it(this, EventField.EXPLOSION) }
            } else {
                callbacks.forEach { it(this, EventField.OPEN) }
                neighbors.filter { it.closed && it.secure && it.secureNeighbor }.forEach { it.opening() }
            }
        }
    }

    fun changingMark() {
        if (closed) {
            marked = !marked
            val event = if(marked) EventField.MARK else EventField.UNMARK
            callbacks.forEach { it(this, event) }
        }
    }

    fun mining() {
        mined = true
    }

    fun restart() {
        opened = false
        mined = false
        marked = false
        callbacks.forEach { it(this, EventField.RESTART) }
    }
}