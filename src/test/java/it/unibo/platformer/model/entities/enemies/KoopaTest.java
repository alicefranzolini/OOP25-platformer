package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.entities.AbstractDynamicEntity;
import it.unibo.platformer.model.entities.enemies.Koopa;
import it.unibo.platformer.model.entities.enemies.Koopa.KoopaState;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Koopa}.
 *
 * <p>JavaFX rendering is intentionally not exercised; tests focus on state
 * transitions, kick/stomp mechanics, and the {@link Enemy} contract.
 */
class KoopaTest {

    /**
     * Minimal no-op {@link BasicPhysics} stub that allows enemy instantiation
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

    /** Initial X position of the Koopa under test. */
    private static final double START_X = 50.0;

    /** Initial Y position of the Koopa under test. */
    private static final double START_Y = 100.0;

    /** Expected width of a Koopa sprite, in pixels. */
    private static final double KOOPA_WIDTH = 32.0;

    /** Expected height of a Koopa sprite, in pixels. */
    private static final double KOOPA_HEIGHT = 48.0;

    /** The Koopa instance shared across tests in this class. */
    private Koopa koopa;

    /** Creates a fresh Koopa before each test. */
    @BeforeEach
    void setUp() {
        koopa = new Koopa(START_X, START_Y, new NoOpPhysics());
    }

    // -----------------------------------------------------------------------
    // Initial state
    // -----------------------------------------------------------------------

    /**
     * The Koopa must begin in the {@link KoopaState#WALK} state.
     */
    @Test
    void initialState_isWalk() {
        assertTrue(koopa.getState() == KoopaState.WALK);
    }

    /**
     * The Koopa must be active immediately after construction.
     */
    @Test
    void initialState_isActive() {
        assertTrue(koopa.isActive());
    }

    /**
     * The Koopa must report that it is walking in the initial state.
     */
    @Test
    void initialState_isWalking() {
        assertTrue(koopa.isWalking());
    }

    /**
     * The Koopa must be able to hit the player while walking.
     */
    @Test
    void initialState_hitsPlayer() {
        assertTrue(koopa.hitsPlayer());
    }

    /**
     * A walking Koopa must not be able to kill other enemies.
     */
    @Test
    void initialState_cannotKillEnemies() {
        assertFalse(koopa.canKillEnemies());
    }

    /**
     * Verifies that the Koopa has the expected fixed dimensions.
     */
    @Test
    void initialState_sizeIsCorrect() {
        assertTrue(koopa.getWidth() == KOOPA_WIDTH);
        assertTrue(koopa.getHeight() == KOOPA_HEIGHT);
    }

    // -----------------------------------------------------------------------
    // stomp()
    // -----------------------------------------------------------------------

    /**
     * After {@code stomp()}, the state must change to {@link KoopaState#SHELL}.
     */
    @Test
    void stomp_changesStateToShell() {
        koopa.stomp();
        assertTrue(koopa.getState() == KoopaState.SHELL);
    }

    /**
     * A sheltered Koopa must not be able to hurt the player.
     */
    @Test
    void stomp_shellState_doesNotHitPlayer() {
        koopa.stomp();
        assertFalse(koopa.hitsPlayer());
    }

    /**
     * A sheltered Koopa must no longer report that it is walking.
     */
    @Test
    void stomp_shellState_isNotWalking() {
        koopa.stomp();
        assertFalse(koopa.isWalking());
    }

    /**
     * A stationary shell must not be able to kill other enemies.
     */
    @Test
    void stomp_shellState_cannotKillEnemies() {
        koopa.stomp();
        assertFalse(koopa.canKillEnemies());
    }

    /**
     * Calling {@code stomp()} a second time must be a no-op and must not throw.
     */
    @Test
    void stomp_idempotent_whenAlreadyShell() {
        koopa.stomp();
        assertDoesNotThrow(() -> koopa.stomp());
        assertTrue(koopa.getState() == KoopaState.SHELL);
    }

    // -----------------------------------------------------------------------
    // kick()
    // -----------------------------------------------------------------------

    /**
     * Kicking the shell to the right must set the state to
     * {@link KoopaState#SHELL_MOVING}.
     */
    @Test
    void kick_right_changesStateToShellMoving() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.getState() == KoopaState.SHELL_MOVING);
    }

    /**
     * Kicking the shell to the left must set the state to
     * {@link KoopaState#SHELL_MOVING}.
     */
    @Test
    void kick_left_changesStateToShellMoving() {
        koopa.stomp();
        koopa.kick(false);
        assertTrue(koopa.getState() == KoopaState.SHELL_MOVING);
    }

    /**
     * Kicking to the right must give the shell a positive horizontal velocity.
     */
    @Test
    void kick_right_velocityIsPositive() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.getVelocityX() > 0);
    }

    /**
     * Kicking to the left must give the shell a negative horizontal velocity.
     */
    @Test
    void kick_left_velocityIsNegative() {
        koopa.stomp();
        koopa.kick(false);
        assertTrue(koopa.getVelocityX() < 0);
    }

    /**
     * A moving shell must be able to hurt the player.
     */
    @Test
    void kick_shellMoving_hitsPlayer() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.hitsPlayer());
    }

    /**
     * A moving shell must be able to kill other enemies.
     */
    @Test
    void kick_shellMoving_canKillEnemies() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.canKillEnemies());
    }

    /**
     * A moving shell must not be considered "walking" (it is a distinct state).
     */
    @Test
    void kick_shellMoving_isNotWalking() {
        koopa.stomp();
        koopa.kick(true);
        assertFalse(koopa.isWalking());
    }

    // -----------------------------------------------------------------------
    // State-machine guard
    // -----------------------------------------------------------------------

    /**
     * Calling {@code stomp()} while the shell is already moving must be ignored;
     * the state must remain {@link KoopaState#SHELL_MOVING}.
     */
    @Test
    void stomp_whileShellMoving_isIgnored() {
        koopa.stomp();
        koopa.kick(true);
        koopa.stomp();
        assertTrue(koopa.getState() == KoopaState.SHELL_MOVING);
    }

    // -----------------------------------------------------------------------
    // update() – no-op physics: only verify no exceptions are thrown
    // -----------------------------------------------------------------------

    /**
     * Calling {@code update()} while walking must not throw an exception.
     */
    @Test
    void update_walkState_doesNotThrow() {
        assertDoesNotThrow(() -> koopa.update(0.016));
    }

    /**
     * Calling {@code update()} while in the shell state must not throw an exception.
     */
    @Test
    void update_shellState_doesNotThrow() {
        koopa.stomp();
        assertDoesNotThrow(() -> koopa.update(0.016));
    }

    /**
     * Calling {@code update()} while the shell is moving must not throw an exception.
     */
    @Test
    void update_shellMovingState_doesNotThrow() {
        koopa.stomp();
        koopa.kick(true);
        assertDoesNotThrow(() -> koopa.update(0.016));
    }
}
