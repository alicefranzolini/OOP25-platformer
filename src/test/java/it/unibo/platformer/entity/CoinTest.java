package it.unibo.platformer.entity;

import org.junit.jupiter.api.Test;

import it.unibo.platformer.model.entities.worldEntity.Coin;
import it.unibo.platformer.model.physics.api.BasicPhysics;

import static org.junit.jupiter.api.Assertions.*;

class CoinTest {
BasicPhysics physics;
    @Test
    void staticCoinNotPopping() {
        Coin coin = new Coin(50, 50, physics);
        assertFalse(coin.isPopping());
    }

    @Test
    void staticCoinActiveOnCreation() {
        Coin coin = new Coin(50, 50, physics);
        assertTrue(coin.isActive());
    }

    @Test
    void poppingCoinIsPopping() {
        Coin coin = Coin.createPopping(50, 50, physics);
        assertTrue(coin.isPopping());
    }

    @Test
    void poppingCoinHasUpwardVelocity() {
        Coin coin = Coin.createPopping(50, 50, physics);
        assertTrue(coin.getVelocityY() < 0);
    }

    @Test
    void poppingCoinDestroysAfterArc() {
        Coin coin = Coin.createPopping(50, 50, physics);
        // simulate many frames until coin returns to origin
        for (int i = 0; i < 300; i++) {
            if (!coin.isActive()) break;
            coin.update(0.016);
        }
        assertFalse(coin.isActive());
    }

    @Test
    void staticCoinDoesNotMoveVertically() {
        Coin coin = new Coin(50, 100, physics);
        double startY = coin.getY();
        coin.update(0.5);
        assertEquals(startY, coin.getY(), 0.001);
    }
}
