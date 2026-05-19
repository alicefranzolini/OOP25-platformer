package it.unibo.platformer;

import it.unibo.platformer.model.entities.powerup.StarPowerUp;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StarPowerUp: bounce behavior and applyEffect -> INVINCIBLE.
 */
class TestPowerUpStar {
    private BasicPhysics physics;

    @Test
    void starBouncesWhenOnGround() {
        StarPowerUp star = new StarPowerUp(10, 50, physics);

        // simulate that it finished emerging and is on ground
        star.setAffectedByGravity(true);
        star.setVelocityY(0);
        star.setOnGround(true);

        star.update(0.016);

        assertTrue(star.getVelocityY() < 0, "Star should receive a negative Y velocity to bounce");
        assertFalse(star.isOnGround(), "onGround should be cleared after bounce");
    }

    @Test
    void applyEffectSetsPlayerInvincible() {
        StarPowerUp star = new StarPowerUp(0, 0, physics);
        PlayerImpl p = new PlayerImpl(0, 0, physics);

        // apply effect
        star.applyEffect(p);

        assertEquals(PlayerImpl.PlayerState.INVINCIBLE, p.getPlayerState(), "Star should set player to INVINCIBLE");
    }
}
