package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.entities.enemies.Koopa.KoopaState;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.api.GameObject;

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
     * Initial X position of the Koopa under test. 
     */
    private static final double STARTX = 50.0;

    /** 
     * Initial Y position of the Koopa under test. 
     */
    private static final double STARTY = 100.0;

    /** 
     * Expected width of a Koopa sprite, in pixels. 
     */
    private static final double KOOPAWIDTH = 32.0;

    /** 
     * Expected height of a Koopa sprite, in pixels. 
     */
    private static final double KOOPAHEIGHT = 48.0;

    private static final double DELTA_TIME = 0.016;

    /** 
     * The Koopa instance shared across tests in this class. 
     */
    private Koopa koopa;

    /** 
     * Creates a fresh Koopa before each test. 
     */
    @BeforeEach
    void setUp() {
        koopa = new Koopa(STARTX, STARTY, new NoOpPhysics());
    }

    /**
     * The Koopa must begin in the {@link KoopaState#WALK} state.
     */
    @Test
    void initialStateisWalk() {
        assertTrue(koopa.getState() == KoopaState.WALK);
    }

    /**
     * The Koopa must be active immediately after construction.
     */
    @Test
    void initialStateisActive() {
        assertTrue(koopa.isActive());
    }

    /**
     * The Koopa must report that it is walking in the initial state.
     */
    @Test
    void initialStateisWalking() {
        assertTrue(koopa.isWalking());
    }

    /**
     * The Koopa must be able to hit the player while walking.
     */
    @Test
    void initialStatehitsPlayer() {
        assertTrue(koopa.hitsPlayer());
    }

    /**
     * A walking Koopa must not be able to kill other enemies.
     */
    @Test
    void initialStatecannotKillEnemies() {
        assertFalse(koopa.canKillEnemies());
    }

    /**
     * Verifies that the Koopa has the expected fixed dimensions.
     */
    @Test
    void initialStatesizeIsCorrect() {
        assertTrue(koopa.getWidth() == KOOPAWIDTH);
        assertTrue(koopa.getHeight() == KOOPAHEIGHT);
    }

    /**
     * After {@code stomp()}, the state must change to {@link KoopaState#SHELL}.
     */
    @Test
    void stompchangesStateToShell() {
        koopa.stomp();
        assertTrue(koopa.getState() == KoopaState.SHELL);
    }

    /**
     * A sheltered Koopa must not be able to hurt the player.
     */
    @Test
    void stompshellStatedoesNotHitPlayer() {
        koopa.stomp();
        assertFalse(koopa.hitsPlayer());
    }

    /**
     * A sheltered Koopa must no longer report that it is walking.
     */
    @Test
    void stompshellStateisNotWalking() {
        koopa.stomp();
        assertFalse(koopa.isWalking());
    }

    /**
     * A stationary shell must not be able to kill other enemies.
     */
    @Test
    void stompshellStatecannotKillEnemies() {
        koopa.stomp();
        assertFalse(koopa.canKillEnemies());
    }

    /**
     * Calling {@code stomp()} a second time must be a no-op and must not throw.
     */
    @Test
    void stompidempotentwhenAlreadyShell() {
        koopa.stomp();
        assertDoesNotThrow(() -> koopa.stomp());
        assertTrue(koopa.getState() == KoopaState.SHELL);
    }

    /**
     * Kicking the shell to the right must set the state to
     * {@link KoopaState#SHELL_MOVING}.
     */
    @Test
    void kickrightchangesStateToShellMoving() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.getState() == KoopaState.SHELL_MOVING);
    }

    /**
     * Kicking the shell to the left must set the state to
     * {@link KoopaState#SHELL_MOVING}.
     */
    @Test
    void kickleftchangesStateToShellMoving() {
        koopa.stomp();
        koopa.kick(false);
        assertTrue(koopa.getState() == KoopaState.SHELL_MOVING);
    }

    /**
     * Kicking to the right must give the shell a positive horizontal velocity.
     */
    @Test
    void kickrightvelocityIsPositive() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.getVelocityX() > 0);
    }

    /**
     * Kicking to the left must give the shell a negative horizontal velocity.
     */
    @Test
    void kickleftvelocityIsNegative() {
        koopa.stomp();
        koopa.kick(false);
        assertTrue(koopa.getVelocityX() < 0);
    }

    /**
     * A moving shell must be able to hurt the player.
     */
    @Test
    void kickshellMovinghitsPlayer() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.hitsPlayer());
    }

    /**
     * A moving shell must be able to kill other enemies.
     */
    @Test
    void kickshellMovingcanKillEnemies() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.canKillEnemies());
    }

    /**
     * A moving shell must not be considered "walking" (it is a distinct state).
     */
    @Test
    void kickshellMovingisNotWalking() {
        koopa.stomp();
        koopa.kick(true);
        assertFalse(koopa.isWalking());
    }

    /**
     * Calling {@code stomp()} while the shell is already moving must be ignored;
     * the state must remain {@link KoopaState#SHELL_MOVING}.
     */
    @Test
    void stompwhileShellMovingisIgnored() {
        koopa.stomp();
        koopa.kick(true);
        koopa.stomp();
        assertTrue(koopa.getState() == KoopaState.SHELL_MOVING);
    }

    /**
     * Calling {@code update()} while walking must not throw an exception.
     */
    @Test
    void updatewalkStatedoesNotThrow() {
        assertDoesNotThrow(() -> koopa.update(DELTA_TIME));
    }

    /**
     * Calling {@code update()} while in the shell state must not throw an exception.
     */
    @Test
    void updateshellStatedoesNotThrow() {
        koopa.stomp();
        assertDoesNotThrow(() -> koopa.update(DELTA_TIME));
    }

    /**
     * Calling {@code update()} while the shell is moving must not throw an exception.
     */
    @Test
    void updateshellMovingStatesdoesNotThrow() {
        koopa.stomp();
        koopa.kick(true);
        assertDoesNotThrow(() -> koopa.update(DELTA_TIME));
    }

    /**
     * Minimal no-op {@link BasicPhysics} stub that allows enemy instantiation
     * without a real physics engine.
     */
    static final class NoOpPhysics implements BasicPhysics {

        /**
         * Does nothing – physics is not under test here.
         *
         * @param obj the object to update
         * @param dt the time elapsed since the last frame, in seconds
         */
        @Override
        public void updatePosition(final GameObject obj, final double dt) { }
    }
}
