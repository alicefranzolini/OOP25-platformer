package it.unibo.platformer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(2, gameManager.getCurrentLevel().getLevelNumber());

        gameManager.nextLevel();
        assertEquals(3, gameManager.getCurrentLevel().getLevelNumber());

        gameManager.nextLevel();
        assertEquals(GameManager.GameState.GAME_OVER, gameManager.getCurrentState());
    }
}
