package it.unibo.platformer;

import it.unibo.platformer.model.entities.powerup.MushroomPowerUp;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MushroomPowerUp: applyEffect -> BIG.
 */
class TestPowerUpMushroom {
    BasicPhysics physics ;
    @Test
    void applyEffectSetsPlayerBig() {
        MushroomPowerUp mushroom = new MushroomPowerUp(0, 0, physics);
        PlayerImpl p = new PlayerImpl(0, 0, physics);

        mushroom.applyEffect(p);

        assertEquals(PlayerImpl.PlayerState.BIG, p.getPlayerState(), "Mushroom should set player to BIG");
    }
}
