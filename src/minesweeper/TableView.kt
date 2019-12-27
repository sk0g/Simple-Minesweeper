package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import static java.lang.Math.cbrt;
import static sample.Main.switchToResults;
import static sample.SettingSelectorView.*;
import static sample.Tile.setLabelValue;
import static sample.Tile.wasSwept;

/**
 * Created by sk0g on 15/07/17.
 */

class TableView {

    static Color NEONBLUE = Color.rgb(24, 202, 230);
    static Color BLACKBLUE = Color.rgb(5,13,16);
    static Color NAVYBLUE = Color.rgb(13,12,28);
    static Color LIGHTGRAY = Color.rgb(216, 218, 231);
    static Color BLUE = Color.rgb(52,96,141);

    private static final int PADDING = 20;
    private static final double BOMB_PROBABILITY = 0.85;

    private static Tile[][] board;

    static Scene getTableViewScene() {

        int largerDimension = colCount > rowCount ? colCount
                                                  : rowCount;
        if (HEX) { largerDimension += 1; }
        double tileSize = (SCENE_WIDTH - PADDING) / largerDimension - cbrt(largerDimension);

        if (HEX) { colCount *= 2; }

        board = new Tile[rowCount][colCount];
        VBox gridVBox = new VBox();

        for (int x = 0; x < rowCount; x++) {
            HBox tempHBox = new HBox();
            for (int y = 0; y < colCount; y++) {
                if (!HEX || x % 2 == y % 2) {
                    board[x][y] = new Tile(x, y, tileSize, HEX, Math.random() >= BOMB_PROBABILITY);
                    tempHBox.getChildren().add(board[x][y]);
                }
            }
            double halfTile = tileSize / 2;
            if (HEX) {
                if (x % 2 == 1) {
                    tempHBox.setTranslateX(halfTile);
                }
                tempHBox.setTranslateY(-halfTile * x * 0.5);
                tempHBox.setSpacing(-4);
            }
            gridVBox.getChildren().add(tempHBox);
        }

        for (int x = 0; x < rowCount; x++) {
            for (int y = 0; y < colCount; y++) {
                if (board[x][y] != null) {
//                    setLabelValue(board[x][y], "" + x + " " + y);
                    setLabelValue(board[x][y]);
                }
            }
        }

        gridVBox.setAlignment(Pos.CENTER);
        return new Scene(gridVBox, SCENE_WIDTH, SCENE_HEIGHT);
    }

    static Tile getTileAt(int x, int y) {
        return board[x][y];
    }

    static void checkWin() {
        Tile t;
        for (int x = 0; x < rowCount; x++) {
            for (int y = 0; y < colCount; y++) {
                // Without HEX, no numerical checking is needed.
                if (!HEX || (x % 2 == y % 2)) {
                    t = board[x][y];
                    if (!wasSwept(t)) {
                        return;
                    }
                }
            }
        }
        switchToResults(true);
    }
}
