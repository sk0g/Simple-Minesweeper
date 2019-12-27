package minesweeper;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static minesweeper.Main.switchToGame;
import static minesweeper.Main.switchToSettings;
import static minesweeper.SettingSelectorView.*;
import static minesweeper.TableView.BLACKBLUE;

class ResultView {
    static Scene getResultViewScene(boolean gameWasWon) {
        if (HEX) { colCount /= 2; }
        Button nextGameButton = new Button();
        nextGameButton.setText("Next game");
        nextGameButton.setOnAction(e -> switchToGame());

        Button settingsButton = new Button();
        settingsButton.setText("Change settings");
        settingsButton.setOnAction(e -> switchToSettings());

        // Horizontal alignment for buttons
        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(10.0);
        buttonContainer.getChildren().addAll(settingsButton,  nextGameButton);
        buttonContainer.setAlignment(Pos.CENTER);

        Text result = new Text();
        result.setFont(Font.font(36));
        if (gameWasWon) {
            result.setText("Congratulations, you won the game :)");
            result.setFill(Color.GREEN);
        } else {
            result.setText("You lost :(");
            result.setFill(Color.RED);
        }

        // Vertical container for status text and buttons
        VBox centreBox = new VBox();
        centreBox.getChildren().addAll(result, buttonContainer);
        centreBox.setAlignment(Pos.CENTER);

        BorderPane pane = new BorderPane();
        pane.setBackground(new Background(new BackgroundFill(BLACKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        pane.setCenter(centreBox);

        return new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
    }
}
