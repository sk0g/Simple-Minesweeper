package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage globalStage;
    private static final double transparency = 0.88;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        globalStage = primaryStage;
        switchToSettings();
    }

    static void switchToSettings() {
        Scene settingsScene = SettingSelectorView.getSettingSelectorView();
        globalStage.setScene(settingsScene);
        globalStage.setOpacity(transparency);
        globalStage.show();
        globalStage.setTitle("Minesweeper: Select your settings");
    }

    static void switchToGame() {
        Scene gameScene = TableView.getTableViewScene();
        globalStage.setScene(gameScene);
        globalStage.setOpacity(1);
        globalStage.show();
        globalStage.setTitle("Minesweeper: Playing a game");
    }

    static void switchToResults(boolean gameWasWon) {
        Scene resultScene = ResultView.getResultViewScene(gameWasWon);
        globalStage.setScene(resultScene);
        globalStage.setOpacity(transparency);
        globalStage.show();
        globalStage.setTitle("Minesweeper: Post-game results");
    }
}
