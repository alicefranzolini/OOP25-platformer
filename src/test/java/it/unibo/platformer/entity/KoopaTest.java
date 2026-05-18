package it.unibo.platformer.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.platformer.model.entities.enemies.Koopa;
import it.unibo.platformer.model.physics.BasicPhysics;

import static org.junit.jupiter.api.Assertions.*;

class KoopaTest {
BasicPhysics physics = new BasicPhysics();
    private Koopa koopa;

    @BeforeEach
    void setUp() {
        koopa = new Koopa(100, 200, physics);
    }

    @Test
    void initialStateIsWalk() {
        assertEquals(Koopa.KoopaState.WALK, koopa.getState());
    }

    @Test
    void initiallyHitsPlayer() {
        assertTrue(koopa.hitsPlayer());
    }

    @Test
    void initiallyCannotKillEnemies() {
        assertFalse(koopa.canKillEnemies());
    }

    @Test
    void stompChangesToShell() {
        koopa.stomp();
        assertEquals(Koopa.KoopaState.SHELL, koopa.getState()); // Change WALK to SHELL
}

    @Test
    void shellDoesNotHitPlayer() {
        koopa.stomp();
        assertFalse(koopa.hitsPlayer());
    }

    @Test
    void kickRightMovesShell() {
        koopa.stomp();
        koopa.kick(true);
        assertEquals(Koopa.KoopaState.SHELL_MOVING, koopa.getState()); // Change WALK to SHELL_MOVING
    }

    @Test
    void kickLeftMovesShellLeft() {
        koopa.stomp();
        koopa.kick(false);
        assertTrue(koopa.getVelocityX() < 0);
    }

    @Test
    void movingShellCanKillEnemies() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.canKillEnemies());
    }

    @Test
    void movingShellHitsPlayer() {
        koopa.stomp();
        koopa.kick(true);
        assertTrue(koopa.hitsPlayer());
    }

    @Test
    void stompIdempotent() {
        koopa.stomp();
        koopa.stomp();
        assertEquals(Koopa.KoopaState.SHELL, koopa.getState()); // Change WALK to SHELL
    }
}
