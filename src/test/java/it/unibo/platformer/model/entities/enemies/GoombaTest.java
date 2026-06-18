package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.entities.enemies.Goomba.GoombaState;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.api.GameObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Goomba}.
 *
 * <p>JavaFX rendering is intentionally not exercised; tests focus on state
 * transitions, the {@link Enemy} contract, and the squish timer.
 */
class GoombaTest {

    /** 
     * Initial X position of the Goomba under test. 
     */
    private static final double START_X = 100.0;

    /** 
     * Initial Y position of the Goomba under test. 
     */
    private static final double START_Y = 200.0;

    /** 
     * Expected side length of a Goomba sprite, in pixels. 
     */
    private static final double GOOMBA_SIZE = 32.0;

    /**
     *  Delta time that is shorter than the squish timeout (0.4 s). 
     */
    private static final double SHORT_DELTA = 0.1;

    /** 
     * Delta time used for long-running squish simulations. 
     */
    private static final double SQUISH_STEP = 0.05;

    /** 
     * Number of steps to guarantee the squish timer expires. 
     */
    private static final int SQUISH_STEPS = 30;

     private static final double DELTA_TIME = 0.016;

    /** 
     * The Goomba instance shared across tests in this class. 
     */
    private Goomba goomba;

    /** 
     * Creates a fresh Goomba before each test. 
     */
    @BeforeEach
    void setUp() {
        goomba = new Goomba(START_X, START_Y, new NoOpPhysics());
    }

    /**
     * The Goomba must begin in the {@link GoombaState#WALK} state.
     */
    @Test
    void initialStateisWalk() {
        assertTrue(goomba.getState() == GoombaState.WALK);
    }

    /**
     * The Goomba must be active immediately after construction.
     */
    @Test
    void initialStateisActive() {
        assertTrue(goomba.isActive());
    }

    /**
     * The Goomba must report that it is walking in the initial state.
     */
    @Test
    void initialStateisWalking() {
        assertTrue(goomba.isWalking());
    }

    /**
     * The Goomba must hit the player while in the walking state.
     */
    @Test
    void initialStatehitsPlayer() {
        assertTrue(goomba.hitsPlayer());
    }

    /**
     * Verifies that the initial position matches the constructor arguments.
     */
    @Test
    void initialStatepositionIsCorrect() {
        assertTrue(goomba.getX() == START_X);
        assertTrue(goomba.getY() == START_Y);
    }

    /**
     * Verifies that the Goomba has the expected fixed dimensions.
     */
    @Test
    void initialStatesizeIsCorrect() {
        assertTrue(goomba.getWidth() == GOOMBA_SIZE);
        assertTrue(goomba.getHeight() == GOOMBA_SIZE);
    }

    /**
     * After {@code squish()}, the state must change to {@link GoombaState#SQUISHED}.
     */
    @Test
    void squishchangesStateToSquished() {
        goomba.squish();
        assertTrue(goomba.getState() == GoombaState.SQUISHED);
    }

    /**
     * A squished Goomba must no longer report that it is walking.
     */
    @Test
    void squishisNoLongerWalking() {
        goomba.squish();
        assertFalse(goomba.isWalking());
    }

    /**
     * A squished Goomba must not be able to hurt the player.
     */
    @Test
    void squishdoesNotHitPlayer() {
        goomba.squish();
        assertFalse(goomba.hitsPlayer());
    }

    /**
     * Calling {@code squish()} a second time must be a no-op and must not throw.
     */
    @Test
    void squishidempotentwhenAlreadySquished() {
        goomba.squish();
        assertDoesNotThrow(() -> goomba.squish());
        assertTrue(goomba.getState() == GoombaState.SQUISHED);
    }

    /**
     * Calling {@code update()} while walking must not throw an exception.
     */
    @Test
    void updatewalkStatedoesNotThrow() {
        assertDoesNotThrow(() -> goomba.update(DELTA_TIME));
    }

    /**
     * After enough simulation ticks, a squished Goomba must be destroyed.
     */
    @Test
    void updatesquishStateafterEnoughTimeisDestroyed() {
        goomba.squish();
        for (int i = 0; i < SQUISH_STEPS; i++) {
            goomba.update(SQUISH_STEP);
        }
        assertFalse(goomba.isActive());
    }

    /**
     * Shortly after being squished the Goomba must still be active.
     */
    @Test
    void updatesquishStatebeforeTimeoutstillActive() {
        goomba.squish();
        goomba.update(SHORT_DELTA);
        assertTrue(goomba.isActive());
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
