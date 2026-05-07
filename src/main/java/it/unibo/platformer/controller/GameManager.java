package it.unibo.platformer.controller;
import it.unibo.platformer.model.level.Level;
import it.unibo.platformer.model.level.BasicLevelLoader;
import it.unibo.platformer.model.level.LevelLoader;

public class GameManager {

    public enum GameState {
        MENU,
        PLAYING,
        GAME_OVER,
        PAUSED
    }

    private GameState currenState;
    private boolean running;

    private Level currentLevel;
    private final LevelLoader loader;

    public GameManager() {
        this.currenState = GameState.MENU;
        this.running = false;
        
        this.loader = new BasicLevelLoader();
        this.currentLevel = this.loader.loadLevel(1);
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
        if (currentLevel != null) {
            currentLevel.update(deltaTime);
        }
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
}
