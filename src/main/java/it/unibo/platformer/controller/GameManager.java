package it.unibo.platformer.controller;
import it.unibo.platformer.model.level.Level;
import it.unibo.platformer.model.level.BasicLevelLoader;
import it.unibo.platformer.model.level.LevelLoader;
import it.unibo.platformer.model.score.ScoreSystem;
import javafx.scene.canvas.GraphicsContext;
import it.unibo.platformer.model.entities.players.InputController;

public class GameManager {

    public enum GameState {
        MENU,
        PLAYING,
        GAME_OVER,
        PAUSED
    }

    private double cameraX;
    private final InputController inputController;
    private GameState currenState;
    private boolean running;
    private final ScoreSystem scoreSystem;

    private Level currentLevel;
    private final LevelLoader loader;

    public GameManager() {
        this.currenState = GameState.MENU;
        this.running = false;

        this.scoreSystem = new ScoreSystem();
        this.loader = new BasicLevelLoader();
        this.currentLevel = this.loader.loadLevel(1);

        this.inputController = new InputController();
        this.cameraX = 0;
    }

    public void startGame() {
        this.currenState = GameState.PLAYING;
        this.running = true;
    }

    public void gameOver() {
        this.currenState = GameState.GAME_OVER;
        this.running = false;
    }

    public void pauseGame() {
        if (this.currenState == GameState.PLAYING) {
            this.currenState = GameState.PAUSED;
        }
    }

    public void resumeGame() {
        if (this.currenState == GameState.PAUSED) {
            this.currenState = GameState.PLAYING;
        }
    }

    public void backToMenu() {
        this.currenState = GameState.MENU;
        this.running = false;
    }

    public void update() {
        double deltaTime = 0.016;

        switch (currenState) {
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

    private void updateGame(double deltaTime) {
        inputController.handleInput(currentLevel.getPlayer());

        if (currentLevel != null) {
            currentLevel.update(deltaTime);

            if (this.currentLevel.getPlayer() != null) {
                this.cameraX = this.currentLevel.getPlayer().getX() - 400;

                if (this.cameraX < 0) {
                    this.cameraX = 0;
                }
            }
        }
    }

    public void render(final GraphicsContext gc) {

        gc.clearRect(0, 0, 1280, 720);
        gc.save();
        gc.translate(-cameraX, 0);

        if (this.currentLevel != null) {
            this.currentLevel.render(gc);
        }

        gc.restore();
        gc.fillText("Score: " + scoreSystem.getScore(), 20, 30);
        gc.fillText("Level: 1", 20, 50);
    }

    private void updatePaused() {
        // paused logic
    }

    private void updateGameOver() {
        // game over logic
    }

    public GameState getCurrentSate() {
        return currenState;
    }

    public InputController getInputController() {
        return this.inputController;
    }
}
