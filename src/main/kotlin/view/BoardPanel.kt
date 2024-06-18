package org.example.view

import org.example.model.Board
import org.example.model.Field
import java.awt.GridLayout
import javax.swing.JPanel

class BoardPanel(board: Board): JPanel() {
    init {
        layout = GridLayout(board.rowsQuantity, board.columnQuantity)
        board.forEachField { field: Field ->
            val button = FieldButton(field)
            add(button)
        }
    }
}