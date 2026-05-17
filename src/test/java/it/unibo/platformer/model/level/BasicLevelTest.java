package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.entities.worldEntity.Coin;
import org.junit.jupiter.api.Test;

class BasicLevelTest {

    @Test
    void collectsCoinWhenPlayerBoundingBoxOverlapsIt() {
        final BasicLevel level = new BasicLevel();
        final Player player = new Player(100, 300);
        final Coin coin = new Coin(105, 300);

        level.setPlayer(player);
        level.addEntity(coin);

        level.update(0);

        assertFalse(level.getEntities().contains(coin));
        assertFalse(coin.isActive());
    }

    @Test
    void doesNotCollectCoinWhenPlayerDoesNotOverlapIt() {
        final BasicLevel level = new BasicLevel();
        final Player player = new Player(100, 300);
        final Coin coin = new Coin(200, 300);

        level.setPlayer(player);
        level.addEntity(coin);

        level.update(0);

        assertTrue(level.getEntities().contains(coin));
        assertTrue(coin.isActive());
    }

    @Test
    void replacingPlayerRemovesPreviousPlayerFromLevel() {
        final BasicLevel level = new BasicLevel();
        final Player firstPlayer = new Player(100, 300);
        final Player secondPlayer = new Player(150, 300);

        level.setPlayer(firstPlayer);
        level.setPlayer(secondPlayer);

        assertEquals(secondPlayer, level.getPlayer());
        assertFalse(level.getEntities().contains(firstPlayer));
        assertTrue(level.getEntities().contains(secondPlayer));
    }
}
