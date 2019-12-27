package minesweeper

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    fun start(primaryStage: Stage?) {
        globalStage = primaryStage
        switchToSettings()
    }

    companion object {
        private var globalStage: Stage? = null
        private const val transparency = 0.88
        @JvmStatic
        fun main(args: Array<String>) {
            launch(args)
        }

        @JvmStatic
        fun switchToSettings() {
            val settingsScene: Scene = SettingSelectorView.getSettingSelectorView()
            globalStage.setScene(settingsScene)
            globalStage.setOpacity(transparency)
            globalStage.show()
            globalStage.setTitle("Minesweeper: Select your settings")
        }

        @JvmStatic
        fun switchToGame() {
            val gameScene: Scene = TableView.tableViewScene
            globalStage.setScene(gameScene)
            globalStage.setOpacity(1)
            globalStage.show()
            globalStage.setTitle("Minesweeper: Playing a game")
        }

        @JvmStatic
        fun switchToResults(gameWasWon: Boolean) {
            val resultScene: Scene = ResultView.getResultViewScene(gameWasWon)
            globalStage.setScene(resultScene)
            globalStage.setOpacity(transparency)
            globalStage.show()
            globalStage.setTitle("Minesweeper: Post-game results")
        }
    }
}