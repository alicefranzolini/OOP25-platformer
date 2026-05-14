package it.unibo.platformer.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.platformer.model.entities.enemies.Goomba;

import static org.junit.jupiter.api.Assertions.*;

class GoombaTest {
    private final it.unibo.platformer.model.physics.BasicPhysics physics = new it.unibo.platformer.model.physics.BasicPhysics();
    private Goomba goomba;

    @BeforeEach
    void setUp() {
        goomba = new Goomba(100, 200, physics);
    }

    @Test
    void initialStateIsWalk() {
        assertTrue(goomba.isWalking());
    }

    @Test
    void initiallyHitsPlayer() {
        assertTrue(goomba.hitsPlayer());
    }

    @Test
    void squishChangesState() {
        goomba.squish();
        assertFalse(goomba.isWalking());
    }

    @Test
    void squishStopsHittingPlayer() {
        goomba.squish();
        assertFalse(goomba.hitsPlayer());
    }

    @Test
    void squishIdempotent() {
        goomba.squish();
        goomba.squish(); // second call must be ignored
        assertFalse(goomba.isWalking());
        assertFalse(goomba.hitsPlayer());
    }

    @Test
    void updateWalkAdvancesPosition() {
        double startX = goomba.getX();
        goomba.update(0.1);
        // walking left → x decreases
        assertTrue(goomba.getX() < startX);
    }

    @Test
    void updateSquishedEventuallyDestroys() {
        goomba.squish();
        // SQUISH_TIME = 0.4 — push past it
        goomba.update(0.5);
        assertFalse(goomba.isActive());
    }
}