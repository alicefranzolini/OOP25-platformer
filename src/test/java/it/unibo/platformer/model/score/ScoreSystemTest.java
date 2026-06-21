package it.unibo.platformer.model.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ScoreSystemTest {

    private static final int INITIAL_VALUE = 0;
    private static final int STARTING_LIVES = 3;
    private static final int TEST_SCORE = 250;
    private static final int COINS_FOR_EXTRA_LIFE = 100;
    private static final int COIN_SCORE = 100;
    private static final int ENEMY_SCORE = 200;
    private static final int TWO_EXTRA_LIVES = 2;

    @Test
    void startsWithDefaultValues() {
        final ScoreSystem scoreSystem = new ScoreSystem();

        assertEquals(INITIAL_VALUE, scoreSystem.getScore());
        assertEquals(INITIAL_VALUE, scoreSystem.getCoins());
        assertEquals(STARTING_LIVES, scoreSystem.getLives());
        assertFalse(scoreSystem.isGameOver());
    }

    @Test
    void addsScoreAndLives() {
        final ScoreSystem scoreSystem = new ScoreSystem();

        scoreSystem.addScore(TEST_SCORE);
        scoreSystem.addLife();

        assertEquals(TEST_SCORE, scoreSystem.getScore());
        assertEquals(STARTING_LIVES + 1, scoreSystem.getLives());
    }

    @Test
    void oneHundredCoinsGiveAnExtraLife() {
        final ScoreSystem scoreSystem = new ScoreSystem();

        for (int i = INITIAL_VALUE; i < COINS_FOR_EXTRA_LIFE; i++) {
            scoreSystem.addCoin();
        }

        assertEquals(INITIAL_VALUE, scoreSystem.getCoins());
        assertEquals(STARTING_LIVES + 1, scoreSystem.getLives());
        assertEquals(COINS_FOR_EXTRA_LIFE * COIN_SCORE, scoreSystem.getScore());
    }

    @Test
    void everyGroupOfOneHundredCoinsGivesAnExtraLife() {
        final ScoreSystem scoreSystem = new ScoreSystem();

        for (int i = INITIAL_VALUE; i < COINS_FOR_EXTRA_LIFE * TWO_EXTRA_LIVES; i++) {
            scoreSystem.addCoin();
        }

        assertEquals(INITIAL_VALUE, scoreSystem.getCoins());
        assertEquals(STARTING_LIVES + TWO_EXTRA_LIVES, scoreSystem.getLives());
    }

    @Test
    void defeatingEnemyAddsEnemyScore() {
        final ScoreSystem scoreSystem = new ScoreSystem();

        scoreSystem.addDefeatedEnemy();

        assertEquals(ENEMY_SCORE, scoreSystem.getScore());
    }

    @Test
    void losingAllLivesCausesGameOverWithoutNegativeLives() {
        final ScoreSystem scoreSystem = new ScoreSystem();

        for (int i = INITIAL_VALUE; i < STARTING_LIVES + 1; i++) {
            scoreSystem.loseLife();
        }

        assertEquals(INITIAL_VALUE, scoreSystem.getLives());
        assertTrue(scoreSystem.isGameOver());
    }

    @Test
    void resetRestoresInitialValues() {
        final ScoreSystem scoreSystem = new ScoreSystem();
        scoreSystem.addScore(TEST_SCORE);
        scoreSystem.addCoin();
        scoreSystem.loseLife();

        scoreSystem.reset();

        assertEquals(INITIAL_VALUE, scoreSystem.getScore());
        assertEquals(INITIAL_VALUE, scoreSystem.getCoins());
        assertEquals(STARTING_LIVES, scoreSystem.getLives());
    }
}
