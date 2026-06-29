package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Generic PowerUp behaviour tests: emergence, start horizontal movement,
 * reverseDirection, collect -> applyEffect + destroy.
 */
class TestPowerUps {

    private static final double POWER_UP_WIDTH = 16.0;
    private static final double POWER_UP_HEIGHT = 16.0;
    private static final double START_X = 100.0;
    private static final double START_Y = 200.0;
    private static final int MAX_EMERGE_FRAMES = 300;
    private static final double UPDATE_TIME = 0.016;
    private static final double INITIAL_SPEED = 50.0;
    private static final double REVERSED_SPEED = -50.0;
    private static final double DELTA = 1e-6;

    private BasicPhysics physics;

    @BeforeEach
    void setUp() {
        physics = new BasicPhysicsImpl();
    }

    @Test
    void emergenceCompletesAndStartsHorizontalMovement() {
        final TestPowerUp p = new TestPowerUp(START_X, START_Y, physics);
        assertTrue(p.isEmerging(), "Should start in emerging state");

        // simulate frames until emerging finishes (adjust iterations if physics differs)
        for (int i = 0; i < MAX_EMERGE_FRAMES && p.isEmerging(); i++) {
            p.update(UPDATE_TIME);
        }

        assertFalse(p.isEmerging(), "PowerUp should finish emerging after updates");
        assertNotEquals(0.0, p.getVelocityX(), "Horizontal velocity should be non-zero after emerging");
    }

    @Test
    void reverseDirectionFlipsVelocityX() {
        final TestPowerUp p = new TestPowerUp(0, 0, physics);
        p.setVelocityX(INITIAL_SPEED);
        p.reverseDirection();
        assertEquals(REVERSED_SPEED, p.getVelocityX(), DELTA);
    }

    @Test
    void collectCallsApplyEffectAndDestroy() {
        final TestPowerUp p = new TestPowerUp(0, 0, physics);
        p.collect(null);
        assertTrue(p.effectApplied, "applyEffect should be invoked by collect");
        assertTrue(p.destroyedFlag, "destroy should be invoked by collect");
    }

    private static class TestPowerUp extends AbstractPowerUp {
        private boolean effectApplied;
        private boolean destroyedFlag;

        TestPowerUp(final double x, final double y, final BasicPhysics physics) {
            super(x, y, POWER_UP_WIDTH, POWER_UP_HEIGHT, physics);
        }

        @Override
        public void applyEffect(final Player player) {
            effectApplied = true;
        }

        @Override
        public void destroy() {
            destroyedFlag = true;
        }

        @Override
        public void render(final GraphicsContext gc) {
            // Rendering is not relevant for these behaviour tests.
        }
    }
}
