package it.unibo.platformer.model.entities.world;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Flag} and {@link Pole}.
 */
class FlagPoleTest {

    /**
     *  X coordinate of the pole used in each test. 
     */
    private static final double POLE_X = 100.0;

    /**
     *  Y coordinate of the pole used in each test. 
     */
    private static final double POLE_Y = 50.0;

    /** 
     * Height of the pole used in each test. 
     */
    private static final double POLE_HEIGHT = 200.0;

    /**
     *  Expected width of a pole, in pixels. 
     */
    private static final double EXPECTED_POLE_WIDTH = 6.0;

    /** 
     * Expected width of a flag, in pixels. 
     */
    private static final double EXPECTED_FLAG_WIDTH = 48.0;

    /** 
     * Expected height of a flag, in pixels. 
     */
    private static final double EXPECTED_FLAG_HEIGHT = 32.0;

    /** 
     * Delta time used for a single large simulation step, in seconds. 
     */
    private static final double LARGE_DELTA = 0.05;

    /** 
     * Number of simulation steps used to drive the flag all the way down. 
     */
    private static final int LOWER_ITERATIONS = 1000;

    private static final double SHORT_DELTA = 0.1;

    private static final double DEFAULT_DELTA = 0.016;

    private static final double DELTA = 0.001;

    /**
     *  The pole instance shared across tests in this class. 
     */
    private Pole pole;

    /** 
     * The flag instance shared across tests in this class. 
     */
    private Flag flag;

    /**
     *  Creates a fresh pole and flag before each test. 
     */
    @BeforeEach
    void setUp() {
        pole = new Pole(POLE_X, POLE_Y, POLE_HEIGHT);
        flag = new Flag(pole);
    }

    /**
     * Verifies that the pole stores the correct position.
     */
    @Test
    void polepositionIsCorrect() {
        assertEquals(POLE_X, pole.getX());
        assertEquals(POLE_Y, pole.getY());
    }

    /**
     * Verifies that the pole has the expected fixed width and provided height.
     */
    @Test
    void polesizeIsCorrect() {
        assertEquals(EXPECTED_POLE_WIDTH, pole.getWidth());
        assertEquals(POLE_HEIGHT, pole.getHeight());
    }

    /**
     * Calling {@code update()} on a pole must not throw an exception.
     */
    @Test
    void poleupdatedoesNotThrow() {
        assertDoesNotThrow(() -> pole.update(DEFAULT_DELTA));
    }

    /**
     * The flag must start at the very top of the pole (same Y coordinate).
     */
    @Test
    void flagstartsAtTopOfPole() {
        assertEquals(POLE_Y, flag.getY());
    }

    /**
     * The flag must not be in a lowering state before {@code lower()} is called.
     */
    @Test
    void flagstartsNotLowering() {
        assertFalse(flag.isLowering());
    }

    /**
     * The flag must not be considered down when it starts at the top.
     */
    @Test
    void flagstartsNotDown() {
        assertFalse(flag.isDown());
    }

    /**
     * The flag entity must be non-solid so the player can pass through it.
     */
    @Test
    void flagisNotSolid() {
        assertFalse(flag.isSolid());
    }

    /**
     * Verifies that the flag has the expected fixed dimensions.
     */
    @Test
    void flagsizeIsCorrect() {
        assertEquals(EXPECTED_FLAG_WIDTH, flag.getWidth());
        assertEquals(EXPECTED_FLAG_HEIGHT, flag.getHeight());
    }

    /**
     * After calling {@code lower()}, the flag must report that it is lowering.
     */
    @Test
    void flaglowersetsLoweringTrue() {
        flag.lower();
        assertTrue(flag.isLowering());
    }

    /**
     * After {@code lower()}, an update tick must move the flag downward.
     */
    @Test
    void flagafterLowerupdateMovesItDown() {
        final double startY = flag.getY();
        flag.lower();
        flag.update(SHORT_DELTA);
        assertTrue(flag.getY() > startY);
    }

    /**
     * Without calling {@code lower()}, the flag must not move on update.
     */
    @Test
    void flagwithoutLowerupdateDoesNotMove() {
        final double startY = flag.getY();
        flag.update(1.0);
        assertEquals(startY, flag.getY(), DELTA);
    }

    // -----------------------------------------------------------------------
    // Flag reaches bottom
    // -----------------------------------------------------------------------

    /**
     * After enough simulation ticks, the flag must reach the bottom of the pole.
     */
    @Test
    void flagafterEnoughUpdatesreachesBottom() {
        flag.lower();
        for (int i = 0; i < LOWER_ITERATIONS; i++) {
            flag.update(LARGE_DELTA);
        }
        assertTrue(flag.isDown());
    }

    /**
     * Once at the bottom, the flag must not descend further on additional updates.
     */
    @Test
    void flagonceDowndoesNotDescendFurther() {
        flag.lower();
        for (int i = 0; i < LOWER_ITERATIONS; i++) {
            flag.update(LARGE_DELTA);
        }
        final double bottomY = flag.getY();
        flag.update(1.0);
        assertEquals(bottomY, flag.getY(), DELTA);
    }
}
