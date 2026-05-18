package it.unibo.platformer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.unibo.platformer.model.entities.worldEntity.Coin;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

class GameManagerTest {

    @Test
    void startsFromMenuState() {
        final GameManager gameManager = new GameManager();

        assertEquals(GameManager.GameState.MENU, gameManager.getCurrentState());
    }

    @Test
    void startGameChangesStateToPlaying() {
        final GameManager gameManager = new GameManager();

        gameManager.startGame();

        assertEquals(GameManager.GameState.PLAYING, gameManager.getCurrentState());
    }

    @Test
    void pauseAndResumeOnlyWorkFromCorrectStates() {
        final GameManager gameManager = new GameManager();

        gameManager.pauseGame();
        assertEquals(GameManager.GameState.MENU, gameManager.getCurrentState());

        gameManager.startGame();
        gameManager.pauseGame();
        assertEquals(GameManager.GameState.PAUSED, gameManager.getCurrentState());

        gameManager.resumeGame();
        assertEquals(GameManager.GameState.PLAYING, gameManager.getCurrentState());
    }

    @Test
    void loadLevelResetsCameraAndChangesCurrentLevel() {
        final GameManager gameManager = new GameManager(800, 720);

        gameManager.loadLevel(2);

        assertEquals(2, gameManager.getCurrentLevel().getLevelNumber());
        assertEquals(0, gameManager.getCameraX());
    }

    @Test
    void nextLevelLoadsFollowingLevelUntilLastLevel() {
        final GameManager gameManager = new GameManager();

        gameManager.loadLevel(1);
        gameManager.nextLevel();
        assertEquals(GameManager.GameState.MENU, gameManager.getCurrentState());
        assertEquals(1, gameManager.getCurrentLevel().getLevelNumber());
    }

    @Test
    void pauseKeyTogglesPauseOnlyOncePerPress() {
        final GameManager gameManager = new GameManager();

        gameManager.startGame();
        gameManager.getInputController().pressKey(KeyCode.ESCAPE);
        gameManager.update();
        gameManager.update();

        assertEquals(GameManager.GameState.PAUSED, gameManager.getCurrentState());

        gameManager.getInputController().releaseKey(KeyCode.ESCAPE);
        gameManager.getInputController().pressKey(KeyCode.ESCAPE);
        gameManager.update();

        assertEquals(GameManager.GameState.PLAYING, gameManager.getCurrentState());
    }

    @Test
    void restartKeyLoadsFirstLevelAndStartsGame() {
        final GameManager gameManager = new GameManager();

        gameManager.loadLevel(3);
        gameManager.gameOver();
        gameManager.getInputController().pressKey(KeyCode.R);
        gameManager.update();

        assertEquals(GameManager.GameState.PLAYING, gameManager.getCurrentState());
        assertEquals(1, gameManager.getCurrentLevel().getLevelNumber());
    }

    @Test
    void collectingCoinUpdatesScoreSystem() {
        final GameManager gameManager = new GameManager();

        gameManager.getCurrentLevel().addEntity(new Coin(105, 300));
        gameManager.startGame();
        gameManager.update(0);

        assertEquals(1, gameManager.getScoreSystem().getCoins());
        assertEquals(100, gameManager.getScoreSystem().getScore());
        assertEquals(0, gameManager.getCurrentLevel().getCollectedCoins());
    }

    @Test
    void menuLevelSelectionStartsSelectedLevel() {
        final GameManager gameManager = new GameManager();

        gameManager.getInputController().pressKey(KeyCode.DIGIT3);
        gameManager.update();

        assertEquals(GameManager.GameState.PLAYING, gameManager.getCurrentState());
        assertEquals(3, gameManager.getCurrentLevel().getLevelNumber());
    }

    @Test
    void reachingEndOfLevelReturnsToMenu() {
        final GameManager gameManager = new GameManager();

        gameManager.startGame();
        gameManager.getCurrentLevel().getPlayer().setX(gameManager.getCurrentLevel().getWidth());
        gameManager.update(0);

        assertEquals(GameManager.GameState.MENU, gameManager.getCurrentState());
    }
}
