package minesweeper

import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import minesweeper.Main.Companion.switchToResults
import java.util.*

internal class Tile(private val x: Int, private val y: Int, size: Double, private val isHex: Boolean, private val isBomb: Boolean) : StackPane() {
    private var isRevealed = false
    private var isFlagged = false
    private val poly: Polygon = Polygon()
    private val label: Label = Label()
    private val labelValue: String
        private get() {
            if (isBomb) {
                return "BOOM"
            }
            var bombCount = 0
            for (currentTile in neighbours) {
                if (currentTile != null && currentTile.isBomb) {
                    bombCount += 1
                }
            }
            return if (bombCount == 0) {
                " "
            } else Integer.toString(bombCount)
        }

    private fun handleMouseClick(e: MouseEvent) {
        if (!isRevealed
                && (e.isControlDown()
                        || e.isAltDown()
                        || e.isShiftDown()
                        || e.getButton() === MouseButton.SECONDARY)) {
            if (isFlagged) {
                isFlagged = false
                poly.setFill(TableView.NAVYBLUE)
            } else {
                isFlagged = true
                poly.setFill(Color.CRIMSON)
            }
        } else {
            open()
        }
        TableView.checkWin()
    }

    private fun open() {
        if (isRevealed || isFlagged) {
            return
        }
        isRevealed = true
        if (isBomb) {
            revealTile()
            switchToResults(false)
        }
        if (label.getText().equals(" ")) {
            for (currentTile in neighbours) {
                if (currentTile != null && !currentTile.isRevealed) {
                    currentTile.open()
                }
            }
        } else {
            revealTile()
        }
        poly.setFill(TableView.NEONBLUE)
        poly.setStroke(TableView.NEONBLUE)
        TableView.checkWin()
    }

    private fun revealTile() {
        label.setFont(Font.font(14))
        label.setVisible(true)
        label.setTextFill(TableView.NAVYBLUE)
    }

    private val neighbours: List<Tile>
        private get() {
            val neighbours: MutableList<Tile> = ArrayList()
            val points = getPointModifiers(isHex)
            var i = 0
            while (i < points.size - 1) {
                val newX = x + points[i]
                val newY = y + points[i + 1]
                if (withinBounds(newX, newY) && TableView.getTileAt(newX, newY) != null) {
                    neighbours.add(TableView.getTileAt(newX, newY))
                }
                i += 2
            }
            return neighbours
        }

    private fun getPointsForShape(diameter: Double, isHex: Boolean): Array<Double> { /*
         *  A hexagon only needs 7 distinctive points, and this function
         *  generates the points and lines using a single argument, while
         *  assuming the center is at (0.5d, 0.5d)
         */
        return if (isHex) {
            arrayOf(
                    0 * diameter, 0.75 * diameter, 0.5 * diameter, 1 * diameter,
                    0.5 * diameter, 1 * diameter, 1 * diameter, 0.75 * diameter,
                    1 * diameter, 0.75 * diameter, 1 * diameter, 0.25 * diameter,
                    1 * diameter, 0.25 * diameter, 0.5 * diameter, 0 * diameter,
                    0.5 * diameter, 0 * diameter, 0 * diameter, 0.25 * diameter,
                    0 * diameter, 0.25 * diameter, 0 * diameter, 0.75 * diameter
            )
        } else {
            val d = diameter * 1.0 // casting a double to integer proved to be needlessly complicated. This works.
            arrayOf(
                    0.0, d,
                    d, d,
                    d, 0.0,
                    0.0, 0.0
            )
        }
    }

    companion object {
        @JvmStatic
        fun setLabelValue(t: Tile) {
            t.label.setText(t.labelValue)
        }

        private fun withinBounds(x: Int, y: Int): Boolean {
            return x >= 0 && y >= 0 && x < SettingSelectorView.rowCount && y < SettingSelectorView.colCount
        }

        private fun getPointModifiers(hex: Boolean): IntArray {
            return if (hex) {
                intArrayOf(
                        0, 2,  // 🡒
                        0, -2,  // 🡐
                        1, 1,  // 🡖
                        1, -1,  // 🡗
                        -1, 1,  // 🡕
                        -1, -1) // 🡔
            } else {
                intArrayOf(
                        0, 1,
                        0, -1,
                        1, -1,
                        1, 0,
                        1, 1,
                        -1, -1,
                        -1, 0,
                        -1, 1)
            }
        }

        @JvmStatic
        fun wasSwept(t: Tile): Boolean {
            return t.isRevealed || t.isFlagged && t.isBomb
        }
    }

    init {
        label.setVisible(false)
        poly.getPoints().addAll(getPointsForShape(size, isHex))
        poly.setStroke(TableView.BLUE)
        poly.setFill(TableView.NAVYBLUE)
        poly.setOnMouseClicked({ e: MouseEvent -> handleMouseClick(e) })
        getChildren().addAll(poly, label)
    }
}