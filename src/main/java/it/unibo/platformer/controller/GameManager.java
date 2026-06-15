package it.unibo.platformer.controller;

import it.unibo.platformer.model.level.BasicLevelLoader;
import it.unibo.platformer.model.level.Level;
import it.unibo.platformer.model.level.LevelLoader;
import it.unibo.platformer.model.score.ScoreSystem;
import it.unibo.platformer.view.HudView;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Coordinates input, levels, score, camera, and rendering.
 */
public final class GameManager {

    private static final int FIRST_LEVEL = 1;
    private static final int LAST_LEVEL = 3;
    private static final double DEFAULT_VIEW_WIDTH = 1280;
    private static final double DEFAULT_VIEW_HEIGHT = 720;
    private static final double FIXED_DELTA_TIME = 0.016;
    private static final double MAX_DELTA_TIME = 0.05;
    private static final double CAMERA_PLAYER_OFFSET = 400;
    private static final int COIN_SCORE = 100;
    private static final int ENEMY_SCORE = 200;
    private static final double LEVEL_END_DISTANCE = 64;
    private static final double VICTORY_MENU_DELAY = 2.5;

    /**
     * Main states used by the game loop.
     */
    public enum GameState {
        /** The level selection menu is shown. */
        MENU,
        /** A level is currently being played. */
        PLAYING,
        /** The player has no lives left. */
        GAME_OVER,
        /** The game is paused. */
        PAUSED,
        /** The current level has been completed. */
        VICTORY
    }

    private double cameraX;
    private final InputController inputController;
    private GameState currentState;
    private boolean running;
    private final ScoreSystem scoreSystem;
    private final HudView hudView;
    private double viewWidth;
    private double viewHeight;
    private double victoryTimer;

    private Level currentLevel;
    private final LevelLoader loader;

    /**
     * Creates a game manager with the default view size.
     */
    public GameManager() {
        this(DEFAULT_VIEW_WIDTH, DEFAULT_VIEW_HEIGHT);
    }

    /**
     * Creates a game manager with a custom view size.
     *
     * @param initialViewWidth the starting view width
     * @param initialViewHeight the starting view height
     */
    public GameManager(final double initialViewWidth, final double initialViewHeight) {
        this.currentState = GameState.MENU;
        this.running = false;
        this.scoreSystem = new ScoreSystem();
        this.hudView = new HudView();
        this.loader = new BasicLevelLoader();
        this.currentLevel = this.loader.loadLevel(FIRST_LEVEL);
        this.inputController = new InputController();
        this.viewWidth = initialViewWidth;
        this.viewHeight = initialViewHeight;
        this.cameraX = 0;
        this.victoryTimer = 0;
    }

    /**
     * Updates the current view size after the window is resized.
     *
     * @param newViewWidth the new view width
     * @param newViewHeight the new view height
     */
    public void setViewSize(final double newViewWidth, final double newViewHeight) {
        this.viewWidth = newViewWidth;
        this.viewHeight = newViewHeight;
        updateCamera();
    }

    /**
     * Starts or resumes active gameplay.
     */
    public void startGame() {
        this.currentState = GameState.PLAYING;
        this.running = true;
        this.victoryTimer = 0;
    }

    /**
     * Switches the game to the game over state.
     */
    public void gameOver() {
        this.currentState = GameState.GAME_OVER;
        this.running = false;
    }

    /**
     * Pauses the game if a level is being played.
     */
    public void pauseGame() {
        if (this.currentState == GameState.PLAYING) {
            this.currentState = GameState.PAUSED;
        }
    }

    /**
     * Resumes the game if it is paused.
     */
    public void resumeGame() {
        if (this.currentState == GameState.PAUSED) {
            this.currentState = GameState.PLAYING;
        }
    }

    /**
     * Toggles between playing and paused states.
     */
    public void togglePause() {
        if (this.currentState == GameState.PLAYING) {
            pauseGame();
        } else if (this.currentState == GameState.PAUSED) {
            resumeGame();
        }
    }

    /**
     * Returns to the level selection menu.
     */
    public void backToMenu() {
        this.currentState = GameState.MENU;
        this.running = false;
        this.victoryTimer = 0;
    }

    /**
     * Resets score and starts a new game from the first level.
     */
    public void restartGame() {
        this.scoreSystem.reset();
        loadLevel(FIRST_LEVEL);
        startGame();
    }

    /**
     * Loads the selected level.
     *
     * @param levelNumber the level to load
     */
    public void loadLevel(final int levelNumber) {
        this.currentLevel = this.loader.loadLevel(levelNumber);
        this.cameraX = 0;
        this.victoryTimer = 0;
    }

    /**
     * Ends the current level flow and returns to the menu.
     */
    public void nextLevel() {
        backToMenu();
    }

    /**
     * Updates the game using the default fixed delta time.
     */
    public void update() {
        update(FIXED_DELTA_TIME);
    }

    /**
     * Updates the current game state.
     *
     * @param deltaTime elapsed time in seconds
     */
    public void update(final double deltaTime) {
        final double safeDeltaTime = Math.max(0, Math.min(deltaTime, MAX_DELTA_TIME));

        handleGameCommands();
        discardLevelSelectionOutsideMenu();

        switch (currentState) {
            case MENU:
                updateMenu();
                break;
            case PLAYING:
                updateGame(safeDeltaTime);
                break;
            case PAUSED:
                updatePaused();
                break;
            case GAME_OVER:
                updateGameOver();
                break;
            case VICTORY:
                updateVictory(deltaTime);
                break;
        }
    }

    private void discardLevelSelectionOutsideMenu() {
        if (this.currentState != GameState.MENU) {
            this.inputController.consumeSelectedLevel();
        }
    }

    private void handleGameCommands() {
        // Restart has priority because it creates a new game state.
        if (this.inputController.consumeRestartPressed()) {
            restartGame();
            return;
        }

        if (this.inputController.consumePausePressed()) {
            handlePauseCommand();
        }

        if (this.inputController.consumeMenuPressed()) {
            handleMenuCommand();
        }
    }

    private void handlePauseCommand() {
        if (this.currentState == GameState.PLAYING) {
            pauseGame();
        } else if (this.currentState == GameState.PAUSED) {
            resumeGame();
        } else if (this.currentState == GameState.GAME_OVER || this.currentState == GameState.VICTORY) {
            backToMenu();
        }
    }

    private void handleMenuCommand() {
        if (this.currentState == GameState.PAUSED
            || this.currentState == GameState.GAME_OVER
            || this.currentState == GameState.VICTORY) {
            backToMenu();
        }
    }

    /**
     * Runs a simple blocking loop used by non-JavaFX tests or experiments.
     */
    public void gameLoop() {
        this.running = true;

        while (running) {
            update();

            try {
                Thread.sleep(16);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                this.running = false;
            }
        }
    }

    private void updateMenu() {
        final int selectedLevel = this.inputController.consumeSelectedLevel();
        if (selectedLevel >= FIRST_LEVEL && selectedLevel <= LAST_LEVEL) {
            this.scoreSystem.reset();
            loadLevel(selectedLevel);
            startGame();
        }
    }

    private void updateGame(final double deltaTime) {
        if (this.currentLevel == null) {
            return;
        }

        final boolean jumpPressed = this.inputController.consumeJumpPressed();
        this.inputController.handleInput(this.currentLevel.getPlayer());
        this.currentLevel.handleJumpPressed(jumpPressed);
        this.currentLevel.update(deltaTime);
        updateScoreFromLevel();
        checkPlayerDeath();
        checkLevelEnd();
        updateCamera();
    }

    private void updateScoreFromLevel() {
        final int collectedCoins = this.currentLevel.getCollectedCoins();

        for (int i = 0; i < collectedCoins; i++) {
            this.scoreSystem.addCoin();
            this.scoreSystem.addScore(COIN_SCORE);
        }

        this.currentLevel.resetCollectedCoins();

        final int defeatedEnemies = this.currentLevel.getDefeatedEnemies();
        for (int i = 0; i < defeatedEnemies; i++) {
            this.scoreSystem.addScore(ENEMY_SCORE);
        }

        this.currentLevel.resetDefeatedEnemies();
    }

    private void checkLevelEnd() {
        if (this.currentLevel.getPlayer() == null) {
            return;
        }

        final double playerEndX = this.currentLevel.getPlayer().getX()
            + this.currentLevel.getPlayer().getWidth();

        if (this.currentLevel.isCompleted()
            || playerEndX >= this.currentLevel.getWidth() - LEVEL_END_DISTANCE) {
            completeLevel();
        }
    }

    private void checkPlayerDeath() {
        if (this.currentLevel.getPlayer() == null || !this.currentLevel.getPlayer().isDeathComplete()) {
            return;
        }

        final int levelNumber = this.currentLevel.getLevelNumber();
        this.scoreSystem.loseLife();

        if (this.scoreSystem.isGameOver()) {
            gameOver();
        } else {
            loadLevel(levelNumber);
            startGame();
        }
    }

    private void completeLevel() {
        this.currentState = GameState.VICTORY;
        this.running = true;
        this.victoryTimer = 0;
    }

    private void updateCamera() {
        if (this.currentLevel.getPlayer() == null) {
            this.cameraX = 0;
            return;
        }

        // The camera follows the player, but it cannot go outside the level.
        final double playerOffset = Math.min(CAMERA_PLAYER_OFFSET, this.viewWidth * 0.35);
        final double desiredCameraX = this.currentLevel.getPlayer().getX() - playerOffset;
        final double maxCameraX = Math.max(0, this.currentLevel.getWidth() - this.viewWidth);
        this.cameraX = Math.max(0, Math.min(desiredCameraX, maxCameraX));
    }

    /**
     * Renders the game world and the HUD.
     *
     * @param gc the canvas graphics context
     */
    public void render(final GraphicsContext gc) {
        gc.clearRect(0, 0, this.viewWidth, this.viewHeight);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, this.viewWidth, this.viewHeight);

        if (this.currentState != GameState.MENU) {
            gc.save();
            gc.translate(-this.cameraX, 0);
            if (this.currentLevel != null) {
                this.currentLevel.render(gc);
            }
            gc.restore();
        }

        this.hudView.render(
            gc,
            this.scoreSystem,
            this.currentLevel.getLevelNumber(),
            this.currentState,
            this.viewWidth,
            this.viewHeight
        );
    }

    private void updatePaused() {
        // paused logic
    }

    private void updateGameOver() {
        // game over logic
    }

    private void updateVictory(final double deltaTime) {
        this.victoryTimer += deltaTime;
        if (this.victoryTimer >= VICTORY_MENU_DELAY) {
            backToMenu();
        }
    }

    /**
     * Gets the current state.
     *
     * @return the current state
     * @deprecated use {@link #getCurrentState()} instead
     */
    @Deprecated
    public GameState getCurrentSate() {
        return getCurrentState();
    }

    /**
     * Gets the current state.
     *
     * @return the current state
     */
    public GameState getCurrentState() {
        return this.currentState;
    }

    /**
     * Gets the input controller.
     *
     * @return the input controller
     */
    public InputController getInputController() {
        return this.inputController;
    }

    /**
     * Gets the loaded level.
     *
     * @return the current level
     */
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Gets the score system.
     *
     * @return the score system
     */
    public ScoreSystem getScoreSystem() {
        return this.scoreSystem;
    }

    /**
     * Gets the horizontal camera position.
     *
     * @return the camera x coordinate
     */
    public double getCameraX() {
        return this.cameraX;
    }
}
