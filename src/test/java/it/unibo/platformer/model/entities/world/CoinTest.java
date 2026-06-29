package it.unibo.platformer.model.entities.world;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.api.GameObject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Coin}.
 *
 * <p>Tests cover both the stationary coin and the popping coin created via
 * the {@link Coin#createPopping(double, double, BasicPhysics)} factory method.
 * JavaFX rendering is intentionally not exercised.
 */
class CoinTest {

    /** 
     * X coordinate used when creating test coins.
     */
    private static final double COIN_X = 10.0;

    /** 
     * Y coordinate used when creating stationary test coins. 
     */
    private static final double COIN_Y = 10.0;

    /** 
     * Y coordinate used when creating popping test coins. 
     */
    private static final double POP_Y = 200.0;

    /** 
     * Expected width and height of a coin sprite, in pixels. 
     */
    private static final double COIN_SIZE = 16.0;

    /**
     *  Delta time used for each step in the pop-flight simulation. 
     */
    private static final double POP_STEP = 0.05;

    /** 
     * Maximum number of simulation steps before the popping coin must land. 
     */
    private static final int POP_MAX_STEPS = 300;

     private static final double NCOIN_X = 30.0;

    private static final double NNCOIN_Y = 40.0;

    /**
     * A freshly constructed stationary coin must be active.
     */
    @Test
    void stationaryCoininitialStateisActive() {
        final Coin coin = new Coin(COIN_X, COIN_Y, new NoOpPhysics());
        assertTrue(coin.isActive());
    }

    /**
     * A stationary coin must not report that it is popping.
     */
    @Test
    void stationaryCoininitialStateisNotPopping() {
        final Coin coin = new Coin(COIN_X, COIN_Y, new NoOpPhysics());
        assertFalse(coin.isPopping());
    }

    /**
     * Verifies that the coin stores the correct position on construction.
     */
    @Test
    void stationaryCoinpositionIsCorrect() {
        final Coin coin = new Coin(NCOIN_X, NNCOIN_Y, new NoOpPhysics());
        assertEquals(NCOIN_X, coin.getX());
        assertEquals(NNCOIN_Y, coin.getY());
    }

    /**
     * Verifies that the coin has the expected fixed dimensions.
     */
    @Test
    void stationaryCoinsizeIsCorrect() {
        final Coin coin = new Coin(COIN_X, COIN_Y, new NoOpPhysics());
        assertEquals(COIN_SIZE, coin.getWidth());
        assertEquals(COIN_SIZE, coin.getHeight());
    }

    /**
     * Updating a stationary coin (even with a large delta) must not destroy it.
     */
    @Test
    void stationaryCoinupdatedoesNotDestroyCoin() {
        final Coin coin = new Coin(COIN_X, COIN_Y, new NoOpPhysics());
        coin.update(1.0);
        assertTrue(coin.isActive());
    }

    /**
     * A coin created via {@code createPopping} must report that it is popping.
     */
    @Test
    void poppingCoinisPopping() {
        final Coin coin = Coin.createPopping(COIN_X, POP_Y, new NoOpPhysics());
        assertTrue(coin.isPopping());
    }

    /**
     * A popping coin must be active immediately after creation.
     */
    @Test
    void poppingCoininitialStateisActive() {
        final Coin coin = Coin.createPopping(COIN_X, POP_Y, new NoOpPhysics());
        assertTrue(coin.isActive());
    }

    /**
     * A popping coin must have an upward (negative) initial velocity.
     */
    @Test
    void poppingCoinmovesUpwardInitially() {
        final Coin coin = Coin.createPopping(COIN_X, POP_Y, new NoOpPhysics());
        assertTrue(coin.getVelocityY() < 0);
    }

    /**
     * After rising and falling back to its origin Y, the popping coin
     * must be destroyed.
     */
    @Test
    void poppingCoinafterReturnToStartYisDestroyed() {
        final Coin coin = Coin.createPopping(COIN_X, POP_Y, new NoOpPhysics());
        for (int i = 0; i < POP_MAX_STEPS; i++) {
            if (!coin.isActive()) {
                break;
            }
            coin.update(POP_STEP);
        }
        assertFalse(coin.isActive());
    }

    /**
     * Minimal no-op {@link BasicPhysics} stub that allows entity instantiation
     * without a real physics engine.
     */
    static final class NoOpPhysics implements BasicPhysics {

        /**
         * Does nothing – physics is not under test here.
         */
        @Override
        public void updatePosition(final GameObject obj, final double dt) { }

    }
}
