package it.unibo.platformer;

import it.unibo.platformer.model.entities.powerup.AbstractPowerUp;
import it.unibo.platformer.model.physics.api.BasicPhysics;
//import it.unibo.platformer.model.entities.players.PlayerImpl;
import javafx.scene.canvas.GraphicsContext;
import it.unibo.platformer.model.entities.players.Player;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Generic PowerUp behaviour tests: emergence, start horizontal movement,
 * reverseDirection, collect -> applyEffect + destroy.
 */
class TestPowerUps {
    private BasicPhysics physics;

    private static class TestPowerUp extends AbstractPowerUp {
        boolean effectApplied = false;
        boolean destroyedFlag = false;

        TestPowerUp(double x, double y, BasicPhysics physics) {
            super(x, y, 16, 16, physics);
        }

        @Override
        public void applyEffect(Player player) {
            effectApplied = true;
        }

        @Override
        public void destroy() {
            destroyedFlag = true;
            // keep simple: mark destroyedFlag; do not rely on engine removal here
        }

        @Override
        public void render(GraphicsContext gc) {
            throw new UnsupportedOperationException("Unimplemented method 'render'");
        }
    }

    @Test
    void emergenceCompletesAndStartsHorizontalMovement() {
        TestPowerUp p = new TestPowerUp(100, 200, physics);
        assertTrue(p.isEmerging(), "Should start in emerging state");

        // simulate frames until emerging finishes (adjust iterations if physics differs)
        for (int i = 0; i < 300 && p.isEmerging(); i++) {
            p.update(0.016); // ~16ms per frame
        }

        assertFalse(p.isEmerging(), "PowerUp should finish emerging after updates");
        assertNotEquals(0.0, p.getVelocityX(), "Horizontal velocity should be non-zero after emerging");
    }

    @Test
    void reverseDirectionFlipsVelocityX() {
        TestPowerUp p = new TestPowerUp(0, 0, physics);
        p.setVelocityX(50);
        p.reverseDirection();
        assertEquals(-50.0, p.getVelocityX(), 1e-6);
    }

    @Test
    void collectCallsApplyEffectAndDestroy() {
        TestPowerUp p = new TestPowerUp(0, 0, physics);
        p.collect(null);
        assertTrue(p.effectApplied, "applyEffect should be invoked by collect");
        assertTrue(p.destroyedFlag, "destroy should be invoked by collect");
    }
}
