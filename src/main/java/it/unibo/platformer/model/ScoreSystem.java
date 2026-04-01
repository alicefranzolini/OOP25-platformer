package it.unibo.platformer.model;

public class ScoreSystem {

    private int score;
    private int coins;
    private int lives;

    public ScoreSystem() {
        this.score = 0;
        this.coins = 0;
        this.lives = 3;
    }

    public void addScore(final int points) {
        this.score += points;
    }

    public void addCoin() {
        this.coins++;

        if (this.coins >= 100) {
            this.coins = 0;
            addLife();
        }
    }

    public void addLife() {
        this.lives++;
    }

    public void loseLife() {
        if (this.lives > 0) {
            this.lives--;
        }
    }

    public boolean isGameOver() {
        return this.lives <= 0;
    }

    public int getScore() {
        return this.score;
    }

    public int getCoins() {
        return this.coins;
    }

    public int getLives() {
        return this.lives;
    }

    public void reset() {
        this.score = 0;
        this.coins = 0;
        this.lives = 3;
    }
}