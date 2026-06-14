package it.unibo.platformer.model.entities.world;

import it.unibo.platformer.model.entities.AbstractDynamicEntity;
import it.unibo.platformer.model.entities.world.Coin;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import org.junit.jupiter.api.Test;

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
     * Minimal no-op {@link BasicPhysics} stub that allows entity instantiation
     * without a real physics engine.
     */
    static final class NoOpPhysics implements BasicPhysics {

        /**
         * Does nothing – physics is not under test here.
         *
         * @param entity    the entity that would be updated
         * @param deltaTime time elapsed since the last frame, in seconds
         */
        @Override
        public void update(final AbstractDynamicEntity entity, final double deltaTime) {
            // intentionally empty
        }
    }

    /** X coordinate used when creating test coins. */
    private static final double COIN_X = 10.0;

    /** Y coordinate used when creating stationary test coins. */
    private static final double COIN_Y = 10.0;

    /** Y coordinate used when creating popping test coins. */
    private static final double POP_Y = 200.0;

    /** Expected width and height of a coin sprite, in pixels. */
    private static final double COIN_SIZE = 16.0;

    /** Delta time used for each step in the pop-flight simulation. */
    private static final double POP_STEP = 0.05;

    /** Maximum number of simulation steps before the popping coin must land. */
    private static final int POP_MAX_STEPS = 300;

    // -----------------------------------------------------------------------
    // Stationary coin
    // -----------------------------------------------------------------------

    /**
     * A freshly constructed stationary coin must be active.
     */
    @Test
    void stationaryCoin_initialState_isActive() {
        final Coin coin = new Coin(COIN_X, COIN_Y, new NoOpPhysics());
        assertTrue(coin.isActive());
    }

    /**
     * A stationary coin must not report that it is popping.
     */
    @Test
    void stationaryCoin_initialState_isNotPopping() {
        final Coin coin = new Coin(COIN_X, COIN_Y, new NoOpPhysics());
        assertFalse(coin.isPopping());
    }

    /**
     * Verifies that the coin stores the correct position on construction.
     */
    @Test
    void stationaryCoin_positionIsCorrect() {
        final Coin coin = new Coin(30, 40, new NoOpPhysics());
        assertTrue(coin.getX() == 30);
        assertTrue(coin.getY() == 40);
    }

    /**
     * Verifies that the coin has the expected fixed dimensions.
     */
    @Test
    void stationaryCoin_sizeIsCorrect() {
        final Coin coin = new Coin(COIN_X, COIN_Y, new NoOpPhysics());
        assertTrue(coin.getWidth() == COIN_SIZE);
        assertTrue(coin.getHeight() == COIN_SIZE);
    }

    /**
     * Updating a stationary coin (even with a large delta) must not destroy it.
     */
    @Test
    void stationaryCoin_update_doesNotDestroyCoin() {
        final Coin coin = new Coin(COIN_X, COIN_Y, new NoOpPhysics());
        coin.update(1.0);
        assertTrue(coin.isActive());
    }

    // -----------------------------------------------------------------------
    // Popping coin (factory method)
    // -----------------------------------------------------------------------

    /**
     * A coin created via {@code createPopping} must report that it is popping.
     */
    @Test
    void poppingCoin_isPopping() {
        final Coin coin = Coin.createPopping(COIN_X, POP_Y, new NoOpPhysics());
        assertTrue(coin.isPopping());
    }

    /**
     * A popping coin must be active immediately after creation.
     */
    @Test
    void poppingCoin_initialState_isActive() {
        final Coin coin = Coin.createPopping(COIN_X, POP_Y, new NoOpPhysics());
        assertTrue(coin.isActive());
    }

    /**
     * A popping coin must have an upward (negative) initial velocity.
     */
    @Test
    void poppingCoin_movesUpwardInitially() {
        final Coin coin = Coin.createPopping(COIN_X, POP_Y, new NoOpPhysics());
        assertTrue(coin.getVelocityY() < 0);
    }

    /**
     * After rising and falling back to its origin Y, the popping coin
     * must be destroyed.
     */
    @Test
    void poppingCoin_afterReturnToStartY_isDestroyed() {
        final Coin coin = Coin.createPopping(COIN_X, POP_Y, new NoOpPhysics());
        for (int i = 0; i < POP_MAX_STEPS; i++) {
            if (!coin.isActive()) {
                break;
            }
            coin.update(POP_STEP);
        }
        assertFalse(coin.isActive());
    }
}