package it.unibo.platformer.model.score;

/**
 * Stores the player's score, collected coins and remaining lives.
 */
public final class ScoreSystem {

    private static final int INITIAL_VALUE = 0;
    private static final int STARTING_LIVES = 3;
    private static final int COINS_FOR_EXTRA_LIFE = 100;
    private static final int COIN_SCORE = 100;
    private static final int ENEMY_SCORE = 200;

    private int score;
    private int coins;
    private int lives;

    /**
     * Creates a score system with no points or coins and three lives.
     */
    public ScoreSystem() {
        reset();
    }

    /**
     * Adds points to the current score.
     *
     * @param points the points to add
     */
    public void addScore(final int points) {
        this.score += points;
    }

    /**
     * Adds one coin, awards its points and gives an extra life after one hundred coins.
     */
    public void addCoin() {
        this.coins++;
        addScore(COIN_SCORE);

        if (this.coins >= COINS_FOR_EXTRA_LIFE) {
            this.coins = INITIAL_VALUE;
            addLife();
        }
    }

    /**
     * Adds the points awarded for defeating one enemy.
     */
    public void addDefeatedEnemy() {
        addScore(ENEMY_SCORE);
    }

    /**
     * Adds one life.
     */
    public void addLife() {
        this.lives++;
    }

    /**
     * Removes one life without allowing the counter to become negative.
     */
    public void loseLife() {
        if (this.lives > INITIAL_VALUE) {
            this.lives--;
        }
    }

    /**
     * Checks whether no lives remain.
     *
     * @return true when the player has no remaining lives
     */
    public boolean isGameOver() {
        return this.lives <= INITIAL_VALUE;
    }

    /**
     * Returns the current score.
     *
     * @return the score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the current coin count.
     *
     * @return the collected coins
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     * Returns the remaining lives.
     *
     * @return the lives
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Restores score, coins and lives to their initial values.
     */
    public void reset() {
        this.score = INITIAL_VALUE;
        this.coins = INITIAL_VALUE;
        this.lives = STARTING_LIVES;
    }
}
