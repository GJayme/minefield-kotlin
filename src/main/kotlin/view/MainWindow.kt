package org.example.view

import org.example.model.Board
import org.example.model.EventBoard
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    MainWindow()
}

class MainWindow: JFrame() {
    private val board = Board(rowsQuantity = 16, columnQuantity = 30, mineQuantity = 89)
    private val boardPanel = BoardPanel(board)

    init {
        board.onEvent (this::showResult)
        add(boardPanel)

        setSize(690, 438)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        title = "Mine Field"
        isVisible = true
    }

    private fun showResult(eventBoard: EventBoard) {
        SwingUtilities.invokeLater {
            val msg = when(eventBoard) {
                EventBoard.VICTORY -> "You win!"
                EventBoard.DEFEAT -> "You lose!"
            }

            JOptionPane.showMessageDialog(this, msg)
            board.restart()

            boardPanel.repaint()
            boardPanel.validate()
        }
    }
}