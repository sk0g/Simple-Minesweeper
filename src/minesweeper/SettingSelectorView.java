package minesweeper;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import static minesweeper.Main.switchToGame;
import static minesweeper.TableView.*;

class SettingSelectorView {
    private static final Text rowText = new Text("ROWS");
    private static final Text colText = new Text("COLUMNS");

    static int rowCount = 6;
    static int colCount = 6;
    private static Text rowCountText = new Text();
    private static Text colCountText = new Text();

    private static final int MAXSIZE = 25;
    private static final int MINSIZE = 5;

    private static final String upArrow = "\u25B2";
    private static final String downArrow = "\u25BC";
    private static final String rectangleShape = " \u25A0 ";
    private static final String hexagonShape = " \u2B23 ";

    static final int SCENE_WIDTH  = 960;
    static final int SCENE_HEIGHT = 960;

    static boolean HEX;

    static Scene getSettingSelectorView() {
        updateRow();
        updateCol();

        Button rowUpButton = new Button(upArrow);
        rowUpButton.setOnAction(event -> increment("row"));

        Button colUpButton = new Button(upArrow);
        colUpButton.setOnAction(event -> increment("column"));

        Button rowDownButton = new Button(downArrow);
        rowDownButton.setOnAction(event -> decrement("row"));

        Button colDownButton = new Button(downArrow);
        colDownButton.setOnAction(event -> decrement("column"));

        Button playGameButton = new Button("Play");
        playGameButton.setTextFill(BLUE);
        playGameButton.setOnAction(d -> switchToGame());

        Label hexToggleButton = new Label();
        updateHexButton(hexToggleButton);
        hexToggleButton.setTextFill(NEONBLUE);
        hexToggleButton.setFont(Font.font(60));
        hexToggleButton.setPadding(Insets.EMPTY);
        hexToggleButton.setOnMouseClicked(e -> toggleHex(hexToggleButton));

        VBox actionVBox = new VBox();
        actionVBox.getChildren().addAll(playGameButton, hexToggleButton);
        actionVBox.setSpacing(20);
        actionVBox.setAlignment(Pos.CENTER);

        // Row Displays
        VBox rowVBox = new VBox();
        rowVBox.getChildren().addAll(rowUpButton, rowDownButton);

        HBox rowHBox = new HBox();
        rowCountText.setTextAlignment(TextAlignment.CENTER);
        rowCountText.setFont(Font.font(40));
        rowCountText.setFill(LIGHTGRAY);
        rowHBox.getChildren().addAll(rowVBox, rowCountText);

        VBox rowDisplayVBox = new VBox();
        rowText.setTextAlignment(TextAlignment.JUSTIFY);
        rowText.setFont(Font.font(30));
        rowText.setFill(LIGHTGRAY);
        rowDisplayVBox.setPrefWidth(100);
        rowDisplayVBox.getChildren().addAll(rowText, rowHBox);
        rowDisplayVBox.setAlignment(Pos.CENTER);

        // Column Displays
        VBox colVBox = new VBox();
        colVBox.getChildren().addAll(colUpButton, colDownButton);

        HBox colHBox = new HBox();
        colCountText.setTextAlignment(TextAlignment.CENTER);
        colCountText.setFont(Font.font(40));
        colCountText.setFill(LIGHTGRAY);
        colHBox.getChildren().addAll(colVBox, colCountText);

        VBox colDisplayVBox = new VBox();
        colText.setTextAlignment(TextAlignment.JUSTIFY);
        colText.setFont(Font.font(30));
        colText.setFill(LIGHTGRAY);
        colDisplayVBox.setPrefWidth(100);
        colDisplayVBox.getChildren().addAll(colText, colHBox);
        colDisplayVBox.setAlignment(Pos.CENTER);

        // Merging the two
        HBox displayHBox = new HBox();
        displayHBox.setSpacing(50);
        displayHBox.getChildren().addAll(rowDisplayVBox, actionVBox, colDisplayVBox);
        displayHBox.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(BLACKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setCenter(displayHBox);

        return new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private static void decrement(String axis) {
        if (axis.equals("row") && rowCount > MINSIZE) {
            rowCount -= 1;
            updateRow();
        } else if (axis.equals("column") && colCount > MINSIZE) {
            colCount -= 1;
            updateCol();
        }
    }

    private static void increment(String axis) {
        if (axis.equals("row") && rowCount < MAXSIZE) {
            rowCount += 1;
            updateRow();
        } else if (axis.equals("column") && colCount < MAXSIZE) {
            colCount += 1;
            updateCol();
        }
    }

    private static void updateRow() {
        rowCountText.setText(Integer.toString(rowCount));
    }

    private static void updateCol() {
        colCountText.setText(Integer.toString(colCount));
    }

    private static void toggleHex(Label bt) {
        HEX = !HEX;
        updateHexButton(bt);
    }

    private static void updateHexButton(Label bt) {
        bt.setText(HEX ? hexagonShape :
                        rectangleShape);
    }
}