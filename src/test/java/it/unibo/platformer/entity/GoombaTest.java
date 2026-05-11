package it.unibo.platformer.entity;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.physics.BasicPhysics;

import static org.junit.jupiter.api.Assertions.*;

class GoombaTest {
BasicPhysics physics = new BasicPhysics();
    private Goomba goomba;

    @BeforeEach
    void setUp() {
        goomba = new Goomba(100, 200, physics);
    }

    @Test
    void initialStateIsWalk() {
        assertEquals(Goomba.GoombaState.WALK, goomba.getState());
    }

    @Test
    void initiallyHitsPlayer() {
        assertTrue(goomba.hitsPlayer());
    }

    @Test
    void squishChangesState() {
        goomba.squish();
        assertEquals(Goomba.GoombaState.SQUISHED, goomba.getState());
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
        assertEquals(Goomba.GoombaState.SQUISHED, goomba.getState());
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
