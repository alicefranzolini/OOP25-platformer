package it.unibo.platformer;

import it.unibo.platformer.model.entities.powerup.StarPowerUp;
import it.unibo.platformer.model.entities.players.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StarPowerUp: bounce behavior and applyEffect -> INVINCIBLE.
 */
class StarPowerUpTest {

    @Test
    void starBouncesWhenOnGround() {
        StarPowerUp star = new StarPowerUp(10, 50);

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
        StarPowerUp star = new StarPowerUp(0, 0);
        Player p = new Player(0, 0);

        // apply effect
        star.applyEffect(p);

        assertEquals(Player.PlayerState.INVINCIBLE, p.getPlayerState(), "Star should set player to INVINCIBLE");
    }
}
