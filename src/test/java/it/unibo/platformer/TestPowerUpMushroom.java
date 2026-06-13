package it.unibo.platformer;

import it.unibo.platformer.model.entities.powerup.MushroomPowerUp;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for MushroomPowerUp: applyEffect -> BIG.
 */
class TestPowerUpMushroom {

    private BasicPhysics physics;

    @BeforeEach
    void setup() {
        physics = new BasicPhysicsImpl();
    }

    @Test
    void applyEffectSetsPlayerBig() {
        final MushroomPowerUp mushroom = new MushroomPowerUp(0, 0, physics);
        final PlayerImpl p = new PlayerImpl(0, 0, physics);

        mushroom.applyEffect(p);

        assertEquals(PlayerImpl.PlayerState.BIG, p.getPlayerState(), "Mushroom should set player to BIG");
    }
}
