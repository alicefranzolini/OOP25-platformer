package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.entities.worldEntity.Coin;
import it.unibo.platformer.model.physics.BasicPhysics;
import org.junit.jupiter.api.Test;

class BasicLevelTest {

    @Test
    void collectsCoinWhenPlayerBoundingBoxOverlapsIt() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 300);
        final Coin coin = new Coin(105, 300, new BasicPhysics());

        level.setPlayer(player);
        level.addEntity(coin);

        level.update(0);

        assertFalse(level.getEntities().contains(coin));
        assertFalse(coin.isActive());
        assertEquals(1, level.getCollectedCoins());
    }

    @Test
    void doesNotCollectCoinWhenPlayerDoesNotOverlapIt() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 300);
        final Coin coin = new Coin(200, 300, new BasicPhysics());

        level.setPlayer(player);
        level.addEntity(coin);

        level.update(0);

        assertTrue(level.getEntities().contains(coin));
        assertTrue(coin.isActive());
        assertEquals(0, level.getCollectedCoins());
    }

    @Test
    void collectedCoinsCanBeResetAfterScoreUpdate() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 300);
        final Coin coin = new Coin(105, 300, new BasicPhysics());

        level.setPlayer(player);
        level.addEntity(coin);
        level.update(0);
        level.resetCollectedCoins();

        assertEquals(0, level.getCollectedCoins());
    }

    @Test
    void replacingPlayerRemovesPreviousPlayerFromLevel() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl firstPlayer = new PlayerImpl(100, 300);
        final PlayerImpl secondPlayer = new PlayerImpl(150, 300);

        level.setPlayer(firstPlayer);
        level.setPlayer(secondPlayer);

        assertEquals(secondPlayer, level.getPlayer());
        assertFalse(level.getEntities().contains(firstPlayer));
        assertTrue(level.getEntities().contains(secondPlayer));
    }
}
