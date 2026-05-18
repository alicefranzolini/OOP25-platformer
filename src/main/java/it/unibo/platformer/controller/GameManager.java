package it.unibo.platformer.controller;

import it.unibo.platformer.model.entities.players.InputController;
import it.unibo.platformer.model.level.BasicLevelLoader;
import it.unibo.platformer.model.level.Level;
import it.unibo.platformer.model.level.LevelLoader;
import it.unibo.platformer.model.score.ScoreSystem;
import javafx.scene.canvas.GraphicsContext;

public class GameManager {

    private static final int FIRST_LEVEL = 1;
    private static final int LAST_LEVEL = 3;
    private static final double DEFAULT_VIEW_WIDTH = 1280;
    private static final double DEFAULT_VIEW_HEIGHT = 720;
    private static final double FIXED_DELTA_TIME = 0.016;
    private static final double CAMERA_PLAYER_OFFSET = 400;

    public enum GameState {
        MENU,
        PLAYING,
        GAME_OVER,
        PAUSED
    }

    private double cameraX;
    private final InputController inputController;
    private GameState currentState;
    private boolean running;
    private final ScoreSystem scoreSystem;
    private final double viewWidth;
    private final double viewHeight;

    private Level currentLevel;
    private final LevelLoader loader;

    public GameManager() {
        this(DEFAULT_VIEW_WIDTH, DEFAULT_VIEW_HEIGHT);
    }

    public GameManager(final double viewWidth, final double viewHeight) {
        this.currentState = GameState.MENU;
        this.running = false;
        this.scoreSystem = new ScoreSystem();
        this.loader = new BasicLevelLoader();
        this.currentLevel = this.loader.loadLevel(FIRST_LEVEL);
        this.inputController = new InputController();
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.cameraX = 0;
    }

    public void startGame() {
        this.currentState = GameState.PLAYING;
        this.running = true;
    }

    public void gameOver() {
        this.currentState = GameState.GAME_OVER;
        this.running = false;
    }

    public void pauseGame() {
        if (this.currentState == GameState.PLAYING) {
            this.currentState = GameState.PAUSED;
        }
    }

    public void resumeGame() {
        if (this.currentState == GameState.PAUSED) {
            this.currentState = GameState.PLAYING;
        }
    }

    public void togglePause() {
        if (this.currentState == GameState.PLAYING) {
            pauseGame();
        } else if (this.currentState == GameState.PAUSED) {
            resumeGame();
        }
    }

    public void backToMenu() {
        this.currentState = GameState.MENU;
        this.running = false;
    }

    public void restartGame() {
        this.scoreSystem.reset();
        loadLevel(FIRST_LEVEL);
        startGame();
    }

    public void loadLevel(final int levelNumber) {
        this.currentLevel = this.loader.loadLevel(levelNumber);
        this.cameraX = 0;
    }

    public void nextLevel() {
        final int currentLevelNumber = this.currentLevel.getLevelNumber();
        if (currentLevelNumber < LAST_LEVEL) {
            loadLevel(currentLevelNumber + 1);
        } else {
            gameOver();
        }
    }

    public void update() {
        update(FIXED_DELTA_TIME);
    }

    public void update(final double deltaTime) {
        handleGameCommands();

        switch (currentState) {
            case MENU:
                updateMenu();
                break;
            case PLAYING:
                updateGame(deltaTime);
                break;
            case PAUSED:
                updatePaused();
                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void handleGameCommands() {
        // Restart has priority because it creates a new game state.
        if (this.inputController.consumeRestartPressed()) {
            restartGame();
            return;
        }

        if (this.inputController.consumePausePressed()) {
            togglePause();
        }
    }

    public void gameLoop() {
        this.running = true;

        while (running) {
            update();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateMenu() {
        // menu logic
    }

    private void updateGame(final double deltaTime) {
        if (this.currentLevel == null) {
            return;
        }

        this.inputController.handleInput(this.currentLevel.getPlayer());
        this.currentLevel.update(deltaTime);
        updateCamera();
    }

    private void updateCamera() {
        if (this.currentLevel.getPlayer() == null) {
            this.cameraX = 0;
            return;
        }

        // The camera follows the player, but it cannot go outside the level.
        final double desiredCameraX = this.currentLevel.getPlayer().getX() - CAMERA_PLAYER_OFFSET;
        final double maxCameraX = Math.max(0, this.currentLevel.getWidth() - this.viewWidth);
        this.cameraX = Math.max(0, Math.min(desiredCameraX, maxCameraX));
    }

    public void render(final GraphicsContext gc) {
        gc.clearRect(0, 0, this.viewWidth, this.viewHeight);
        gc.save();
        gc.translate(-this.cameraX, 0);
        if (this.currentLevel != null) {
            this.currentLevel.render(gc);
        }
        gc.restore();
        renderSimpleHud(gc);
    }

    private void renderSimpleHud(final GraphicsContext gc) {
        // Temporary HUD. Later it can be moved to a dedicated view class.
        gc.fillText("Score: " + this.scoreSystem.getScore(), 20, 30);
        gc.fillText("Coins: " + this.scoreSystem.getCoins(), 20, 50);
        gc.fillText("Lives: " + this.scoreSystem.getLives(), 20, 70);
        gc.fillText("Level: " + this.currentLevel.getLevelNumber(), 20, 90);

        if (this.currentState == GameState.PAUSED) {
            gc.fillText("PAUSED", this.viewWidth / 2 - 30, 40);
        } else if (this.currentState == GameState.GAME_OVER) {
            gc.fillText("GAME OVER", this.viewWidth / 2 - 40, 40);
        }
    }

    private void updatePaused() {
        // paused logic
    }

    private void updateGameOver() {
        // game over logic
    }

    public GameState getCurrentSate() {
        return getCurrentState();
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public InputController getInputController() {
        return this.inputController;
    }

    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    public ScoreSystem getScoreSystem() {
        return this.scoreSystem;
    }

    public double getCameraX() {
        return this.cameraX;
    }
}
