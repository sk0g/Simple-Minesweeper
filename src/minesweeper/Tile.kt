package sample;

import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

import static sample.Main.switchToResults;
import static sample.SettingSelectorView.colCount;
import static sample.SettingSelectorView.rowCount;
import static sample.TableView.*;

class Tile extends StackPane {
    private int x, y;
    private boolean isBomb;
    private boolean isRevealed = false;
    private boolean isHex;
    private boolean isFlagged;

    private Polygon poly = new Polygon();
    private Label label = new Label();

    Tile(int x, int y, double size, boolean isHex, boolean Bomb) {
        this.x = x;
        this.y = y;
        this.isHex  = isHex;
        this.isBomb = Bomb;
        this.isFlagged = false;

        label.setVisible(false);

        poly.getPoints().addAll(getPointsForShape(size, isHex));
        poly.setStroke(BLUE);
        poly.setFill(NAVYBLUE);

        poly.setOnMouseClicked(this::handleMouseClick);

        getChildren().addAll(poly, label);
    }

    static void setLabelValue(Tile t) {
        t.label.setText(t.getLabelValue());
    }

    private String getLabelValue() {
        if (isBomb) { return "BOOM"; }

        int bombCount = 0;
        for (Tile currentTile : getNeighbours()) {
            if (currentTile != null && currentTile.isBomb) {
                bombCount += 1;
            }
        }

        if (bombCount == 0) { return " "; }

        return Integer.toString(bombCount);
    }

    private void handleMouseClick(MouseEvent e) {
        if (!isRevealed
                && (e.isControlDown()
                || e.isAltDown()
                || e.isShiftDown()
                || e.getButton() == MouseButton.SECONDARY)) {
            if (isFlagged) {
                isFlagged = false;
                poly.setFill(NAVYBLUE);
            } else {
                isFlagged = true;
                poly.setFill(Color.CRIMSON);
            }
        } else {
            open();
        }
        checkWin();
    }

    private void open() {
        if (isRevealed || isFlagged) { return; }

        isRevealed = true;

        if (isBomb) {
            revealTile();
            switchToResults(false);
        }

        if (label.getText().equals(" ")) {
            for (Tile currentTile : getNeighbours()) {
                if (currentTile != null && !currentTile.isRevealed) {
                    currentTile.open();
                }
            }
        } else {
            revealTile();
        }

        poly.setFill(NEONBLUE);
        poly.setStroke(NEONBLUE);

        checkWin();
    }

    private void revealTile() {
        label.setFont(Font.font(14));
        label.setVisible(true);
        label.setTextFill(NAVYBLUE);
    }

    private List<Tile> getNeighbours() {
        List<Tile> neighbours = new ArrayList<>();

        int[] points = getPointModifiers(isHex);

        for (int i = 0; i < points.length - 1; i += 2) {
            int newX = x + points[i];
            int newY = y + points[i+1];

            if (withinBounds(newX, newY) && getTileAt(newX, newY) != null) {
                neighbours.add(getTileAt(newX, newY));
            }
        }
        return neighbours;
    }

    private static boolean withinBounds(int x, int y) {
        return (x >= 0       &&
                y >= 0       &&
                x < rowCount &&
                y < colCount);
    }

    private static int[] getPointModifiers(boolean hex) {
        if (hex) { return new int[] {
                0, 2,       // 🡒
                0, -2,      // 🡐
                1, 1,       // 🡖
                1, -1,      // 🡗
                -1, 1,      // 🡕
                -1, -1 };   // 🡔
        } else { return new int[] {
                0, 1,
                0, -1,
                1, -1,
                1, 0,
                1, 1,
                -1, -1,
                -1, 0,
                -1, 1 };
        }
    }

    private  Double[] getPointsForShape(double diameter, boolean isHex) {
        /*
         *  A hexagon only needs 7 distinctive points, and this function
         *  generates the points and lines using a single argument, while
         *  assuming the center is at (0.5d, 0.5d)
         */
        if (isHex) {
            return new Double[] {
                    0 * diameter, 0.75 * diameter, 0.5 * diameter, 1 * diameter,
                    0.5 * diameter, 1 * diameter, 1 * diameter, 0.75 * diameter,
                    1 * diameter, 0.75 * diameter, 1 * diameter, 0.25 * diameter,
                    1 * diameter, 0.25 * diameter, 0.5 * diameter, 0 * diameter,
                    0.5 * diameter, 0 * diameter, 0 * diameter, 0.25 * diameter,
                    0 * diameter, 0.25 * diameter, 0 * diameter, 0.75 * diameter
            };
        } else {
            double d = diameter * 1.0; // casting a double to integer proved to be needlessly complicated. This works.
            return new Double[] {
                    0.0, d,
                    d, d,
                    d, 0.0,
                    0.0, 0.0
            };
        }
    }

    static boolean wasSwept(Tile t) {
        return (t.isRevealed || (t.isFlagged && t.isBomb));
    }
}