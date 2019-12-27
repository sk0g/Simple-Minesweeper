package minesweeper

import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import minesweeper.Main.Companion.switchToResults
import minesweeper.Tile.Companion.setLabelValue
import minesweeper.Tile.Companion.wasSwept

/**
 * Created by sk0g on 15/07/17.
 */
internal object TableView {
    @JvmField
    var NEONBLUE: Color = Color.rgb(24, 202, 230)
    @JvmField
    var BLACKBLUE: Color = Color.rgb(5, 13, 16)
    var NAVYBLUE: Color = Color.rgb(13, 12, 28)
    @JvmField
    var LIGHTGRAY: Color = Color.rgb(216, 218, 231)
    @JvmField
    var BLUE: Color = Color.rgb(52, 96, 141)
    private const val PADDING = 20
    private const val BOMB_PROBABILITY = 0.85
    private var board: Array<Array<Tile?>>
    //                    setLabelValue(board[x][y], "" + x + " " + y);
    val tableViewScene: Scene
        get() {
            var largerDimension = if (SettingSelectorView.colCount > SettingSelectorView.rowCount) SettingSelectorView.colCount else SettingSelectorView.rowCount
            if (SettingSelectorView.HEX) {
                largerDimension += 1
            }
            val tileSize = (SettingSelectorView.SCENE_WIDTH - PADDING) / largerDimension - Math.cbrt(largerDimension.toDouble())
            if (SettingSelectorView.HEX) {
                SettingSelectorView.colCount *= 2
            }
            board = Array(SettingSelectorView.rowCount) { arrayOfNulls<Tile>(SettingSelectorView.colCount) }
            val gridVBox = VBox()
            for (x in 0 until SettingSelectorView.rowCount) {
                val tempHBox = HBox()
                for (y in 0 until SettingSelectorView.colCount) {
                    if (!SettingSelectorView.HEX || x % 2 == y % 2) {
                        board[x][y] = Tile(x, y, tileSize, SettingSelectorView.HEX, Math.random() >= BOMB_PROBABILITY)
                        tempHBox.getChildren().add(board[x][y])
                    }
                }
                val halfTile = tileSize / 2
                if (SettingSelectorView.HEX) {
                    if (x % 2 == 1) {
                        tempHBox.setTranslateX(halfTile)
                    }
                    tempHBox.setTranslateY(-halfTile * x * 0.5)
                    tempHBox.setSpacing(-4)
                }
                gridVBox.getChildren().add(tempHBox)
            }
            for (x in 0 until SettingSelectorView.rowCount) {
                for (y in 0 until SettingSelectorView.colCount) {
                    if (board[x][y] != null) { //                    setLabelValue(board[x][y], "" + x + " " + y);
                        setLabelValue(board[x][y]!!)
                    }
                }
            }
            gridVBox.setAlignment(Pos.CENTER)
            return Scene(gridVBox, SettingSelectorView.SCENE_WIDTH, SettingSelectorView.SCENE_HEIGHT)
        }

    fun getTileAt(x: Int, y: Int): Tile? {
        return board[x][y]
    }

    fun checkWin() {
        var t: Tile?
        for (x in 0 until SettingSelectorView.rowCount) {
            for (y in 0 until SettingSelectorView.colCount) { // Without HEX, no numerical checking is needed.
                if (!SettingSelectorView.HEX || x % 2 == y % 2) {
                    t = board[x][y]
                    if (!wasSwept(t!!)) {
                        return
                    }
                }
            }
        }
        switchToResults(true)
    }
}