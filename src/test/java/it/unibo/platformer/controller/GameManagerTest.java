package it.unibo.platformer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.enemies.Enemy;
import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.entities.world.Coin;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

class GameManagerTest {

    private static final int FIRST_LEVEL = 1;
    private static final int SECOND_LEVEL = 2;
    private static final int THIRD_LEVEL = 3;
    private static final int ZERO_COUNT = 0;
    private static final int ONE_COUNT = 1;
    private static final int STARTING_LIVES = 3;
    private static final int COIN_SCORE = 100;
    private static final int ENEMY_SCORE = 200;
    private static final int OLD_SCORE = 500;
    private static final double ZERO_DELTA_TIME = 0.0;
    private static final double CAMERA_START_X = 0.0;
    private static final double VIEW_WIDTH = 800.0;
    private static final double VIEW_HEIGHT = 720.0;
    private static final double COIN_X = 105.0;
    private static final double COIN_Y = 300.0;
    private static final double LARGE_FRAME_TIME = 10.0;
    private static final double VICTORY_RETURN_TIME = 3.0;
    private static final double FLAG_FRAME_TIME = 0.05;
    private static final int FLAG_LOWERING_FRAMES = 60;

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
        final GameManager gameManager = new GameManager(VIEW_WIDTH, VIEW_HEIGHT);

        gameManager.loadLevel(SECOND_LEVEL);

        assertEquals(SECOND_LEVEL, gameManager.getCurrentLevel().getLevelNumber());
        assertEquals(CAMERA_START_X, gameManager.getCameraX());
    }

    @Test
    void pauseKeyPausesThenResumesGame() {
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
    void menuKeyReturnsFromPauseToMenu() {
        final GameManager gameManager = new GameManager();

        gameManager.startGame();
        gameManager.pauseGame();
        gameManager.getInputController().pressKey(KeyCode.M);
        gameManager.update();

        assertEquals(GameManager.GameState.MENU, gameManager.getCurrentState());
    }

    @Test
    void restartKeyLoadsFirstLevelAndStartsGame() {
        final GameManager gameManager = new GameManager();

        gameManager.loadLevel(THIRD_LEVEL);
        gameManager.gameOver();
        gameManager.getInputController().pressKey(KeyCode.R);
        gameManager.update();

        assertEquals(GameManager.GameState.PLAYING, gameManager.getCurrentState());
        assertEquals(FIRST_LEVEL, gameManager.getCurrentLevel().getLevelNumber());
    }

    @Test
    void collectingCoinUpdatesScoreSystem() {
        final GameManager gameManager = new GameManager();

        gameManager.getCurrentLevel().addEntity(new Coin(COIN_X, COIN_Y, new BasicPhysicsImpl()));
        gameManager.startGame();
        gameManager.update(ZERO_DELTA_TIME);

        assertEquals(ONE_COUNT, gameManager.getScoreSystem().getCoins());
        assertEquals(COIN_SCORE, gameManager.getScoreSystem().getScore());
        assertEquals(ZERO_COUNT, gameManager.getCurrentLevel().getCollectedCoins());
    }

    @Test
    void menuLevelSelectionStartsSelectedLevel() {
        final GameManager gameManager = new GameManager();

        gameManager.getInputController().pressKey(KeyCode.DIGIT3);
        gameManager.update();

        assertEquals(GameManager.GameState.PLAYING, gameManager.getCurrentState());
        assertEquals(THIRD_LEVEL, gameManager.getCurrentLevel().getLevelNumber());
    }

    @Test
    void menuLevelSelectionStartsWithFreshScoreSystem() {
        final GameManager gameManager = new GameManager();

        gameManager.getScoreSystem().addScore(OLD_SCORE);
        gameManager.getScoreSystem().addCoin();
        gameManager.getScoreSystem().loseLife();
        gameManager.getInputController().pressKey(KeyCode.DIGIT2);
        gameManager.update();

        assertEquals(GameManager.GameState.PLAYING, gameManager.getCurrentState());
        assertEquals(SECOND_LEVEL, gameManager.getCurrentLevel().getLevelNumber());
        assertEquals(ZERO_COUNT, gameManager.getScoreSystem().getScore());
        assertEquals(ZERO_COUNT, gameManager.getScoreSystem().getCoins());
        assertEquals(STARTING_LIVES, gameManager.getScoreSystem().getLives());
    }

    @Test
    void levelSelectionPressedDuringGameplayIsIgnoredWhenReturningToMenu() {
        final GameManager gameManager = new GameManager();

        gameManager.startGame();
        gameManager.getInputController().pressKey(KeyCode.DIGIT3);
        gameManager.update();
        gameManager.backToMenu();
        gameManager.update();

        assertEquals(GameManager.GameState.MENU, gameManager.getCurrentState());
        assertEquals(FIRST_LEVEL, gameManager.getCurrentLevel().getLevelNumber());
    }

    @Test
    void defeatingEnemyUpdatesScoreSystem() {
        final GameManager gameManager = new GameManager();
        final Player player = gameManager.getCurrentLevel().getPlayer();

        player.setState(Player.PlayerState.INVINCIBLE);
        gameManager.getCurrentLevel().addEntity(new Goomba(player.getX(), player.getY(), new BasicPhysicsImpl()));
        gameManager.startGame();
        gameManager.update(ZERO_DELTA_TIME);

        assertEquals(ENEMY_SCORE, gameManager.getScoreSystem().getScore());
    }

    @Test
    void veryLargeFrameDoesNotMakeEnemiesFallThroughLevel() {
        final GameManager gameManager = new GameManager();

        gameManager.startGame();
        gameManager.update(LARGE_FRAME_TIME);

        assertTrue(gameManager.getCurrentLevel().getEntities().stream()
            .filter(Enemy.class::isInstance)
            .allMatch(entity -> entity.getY() < gameManager.getCurrentLevel().getHeight()));
    }

    @Test
    void reachingEndOfLevelShowsVictoryThenReturnsToMenu() {
        final GameManager gameManager = new GameManager();

        gameManager.startGame();
        ((AbstractEntity) gameManager.getCurrentLevel().getPlayer()).setX(gameManager.getCurrentLevel().getWidth());
        for (int i = ZERO_COUNT; i < FLAG_LOWERING_FRAMES; i++) {
            gameManager.update(FLAG_FRAME_TIME);
        }

        assertEquals(GameManager.GameState.VICTORY, gameManager.getCurrentState());

        gameManager.update(VICTORY_RETURN_TIME);

        assertEquals(GameManager.GameState.MENU, gameManager.getCurrentState());
    }
}
