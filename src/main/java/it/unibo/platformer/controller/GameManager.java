package it.unibo.platformer.controller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
        this.victoryTimer = 0;
    }

    /**
     * Switches the game to the game over state.
     */
    public void gameOver() {
        this.currentState = GameState.GAME_OVER;
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
     * Returns to the level selection menu.
     */
    public void backToMenu() {
        this.currentState = GameState.MENU;
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
        final double nonNegativeDeltaTime = Math.max(0, deltaTime);
        final double safeDeltaTime = Math.min(nonNegativeDeltaTime, MAX_DELTA_TIME);

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
                break;
            case GAME_OVER:
                break;
            case VICTORY:
                updateVictory(nonNegativeDeltaTime);
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
        }

        this.currentLevel.resetCollectedCoins();

        final int defeatedEnemies = this.currentLevel.getDefeatedEnemies();
        for (int i = 0; i < defeatedEnemies; i++) {
            this.scoreSystem.addDefeatedEnemy();
        }

        this.currentLevel.resetDefeatedEnemies();
    }

    private void checkLevelEnd() {
        if (this.currentLevel.getPlayer() == null) {
            return;
        }

        if (this.currentLevel.isCompleted()) {
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
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "The controller exposes the active level to the application and its tests."
    )
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Gets the score system.
     *
     * @return the score system
     */
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "The controller exposes the current score data to the HUD and its tests."
    )
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
