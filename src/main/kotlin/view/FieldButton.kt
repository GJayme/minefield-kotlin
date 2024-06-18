package org.example.view

import org.example.model.EventField
import org.example.model.Field
import java.awt.Color
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingUtilities

private val COLOR_BG_NORMAL = Color(184, 184, 184)
private val COLOR_BG_MARK = Color(8, 179, 247)
private val COLOR_BG_EXPLOSION = Color(189, 66, 68)
private val COLOR_TEXT_GREEN = Color(0, 100, 0)

class FieldButton(private val field: Field): JButton() {
    init {
        font = font.deriveFont(Font.BOLD)
        background = COLOR_BG_NORMAL
        isOpaque = true
        border = BorderFactory.createBevelBorder(0)
        addMouseListener(MouseClickListener(field, {it.opening()}, {it.changingMark()}))

        field.onEvent(this::applyStyle)
    }

    private fun applyStyle(field: Field, event: EventField) {
        when(event) {
            EventField.EXPLOSION -> applyExplosionStyle()
            EventField.OPEN -> applyOpenStyle()
            EventField.MARK -> applyMarkedStyle()
            else -> applyDefaultPatter()
        }

        SwingUtilities.invokeLater {
            repaint()
            validate()
        }
    }

    private fun applyExplosionStyle() {
        background = COLOR_BG_EXPLOSION
        text = "X"
    }

    private fun applyOpenStyle() {
        background = COLOR_BG_NORMAL
        border = BorderFactory.createLineBorder(Color.GRAY)

        foreground = when (field.quantityMinedNeighbors) {
            1 -> COLOR_TEXT_GREEN
            2 -> Color.BLUE
            3 -> Color.YELLOW
            4, 5 ,6 -> Color.RED
            else -> Color.PINK
        }

        text = if (field.quantityMinedNeighbors > 0) field.quantityMinedNeighbors.toString() else ""
    }

    private fun applyMarkedStyle() {
        background = COLOR_BG_MARK
        foreground = Color.BLACK
        text = "M"
    }

    private fun applyDefaultPatter() {
        background = COLOR_BG_NORMAL
        border = BorderFactory.createBevelBorder(0)
        text = ""
    }
}