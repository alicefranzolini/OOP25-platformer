package it.unibo.platformer;

import it.unibo.platformer.model.entities.powerup.MushroomPowerUp;
import it.unibo.platformer.model.entities.players.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MushroomPowerUp: applyEffect -> BIG.
 */
class MushroomPowerUpTest {

    @Test
    void applyEffectSetsPlayerBig() {
        MushroomPowerUp mushroom = new MushroomPowerUp(0, 0);
        Player p = new Player(0, 0);

        mushroom.applyEffect(p);

        assertEquals(Player.PlayerState.BIG, p.getPlayerState(), "Mushroom should set player to BIG");
    }
}
