package it.unibo.platformer;

import it.unibo.platformer.model.entities.powerup.StarPowerUp;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for StarPowerUp: bounce behavior and applyEffect -> INVINCIBLE.
 */
class TestPowerUpStar {

    private static final double START_X = 10.0;
    private static final double START_Y = 50.0;
    private static final double UPDATE_TIME = 0.016;

    private BasicPhysics physics;

    @BeforeEach
    void setup() {
        physics = new BasicPhysicsImpl();
    }

    @Test
    void starBouncesWhenOnGround() {
        final StarPowerUp star = new StarPowerUp(START_X, START_Y, physics);

        // simulate that it finished emerging and is on ground
        star.setAffectedByGravity(true);
        star.setVelocityY(0);
        star.setOnGround(true);

        star.update(UPDATE_TIME);

        assertTrue(star.getVelocityY() < 0, "Star should receive a negative Y velocity to bounce");
        assertFalse(star.isOnGround(), "onGround should be cleared after bounce");
    }

    @Test
    void applyEffectSetsPlayerInvincible() {
        final StarPowerUp star = new StarPowerUp(0, 0, physics);
        final PlayerImpl p = new PlayerImpl(0, 0, physics);

        // apply effect
        star.applyEffect(p);

        assertEquals(PlayerImpl.PlayerState.INVINCIBLE, p.getPlayerState(), "Star should set player to INVINCIBLE");
    }
}
