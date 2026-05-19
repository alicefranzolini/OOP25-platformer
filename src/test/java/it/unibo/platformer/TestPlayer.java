package it.unibo.platformer;

import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.physics.api.BasicPhysics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlayer {
    private BasicPhysics physics;
    private PlayerImpl player;

    @BeforeEach
    void setup() {
        player = new PlayerImpl(100, 100, physics);
    }

    // ------------------------------------------------------------
    // HORIZONTAL MOVEMENT
    // ------------------------------------------------------------

    @Test
    void testMoveRight() {
        player.moveRight();
        assertEquals(180.0, player.getVelocityX(), 0.01);
    }

    @Test
    void testMoveLeft() {
        player.moveLeft();
        assertEquals(-180.0, player.getVelocityX(), 0.01);
    }

    @Test
    void testStopX() {
        player.moveRight();
        player.stopX();
        assertEquals(0.0, player.getVelocityX(), 0.01);
    }

    // ------------------------------------------------------------
    // JUMPING
    // ------------------------------------------------------------

    @Test
    void testJumpWhenOnGround() {
        player.setOnGround(true);
        player.setJumpRequested(true);

        player.update(0.016);

        assertTrue(player.getVelocityY() < -419 && player.getVelocityY() > -421);
        assertFalse(player.isOnGround());
    }

    @Test
    void testJumpNotAllowedInAir() {
        player.setOnGround(false);
        player.setJumpRequested(true);

        player.update(0.016);

        assertNotEquals(-420.0, player.getVelocityY(), 0.01);
    }

    // ------------------------------------------------------------
    // PLAYER STATES
    // ------------------------------------------------------------

    @Test
    void testPowerUpSmallToBig() {
        player.setState(PlayerImpl.PlayerState.BIG);
        assertEquals(PlayerImpl.PlayerState.BIG, player.getPlayerState());
        assertEquals(48, player.getHeight());
    }

    @Test
    void testPowerUpBigToInvincible() {
        player.setState(PlayerImpl.PlayerState.BIG);
        player.setState(PlayerImpl.PlayerState.INVINCIBLE);

        assertEquals(PlayerImpl.PlayerState.INVINCIBLE, player.getPlayerState());
        assertTrue(player.isInvincible());
    }

    @Test
    void testInvincibilityEndsAfterTimer() {
        player.setState(PlayerImpl.PlayerState.INVINCIBLE);

        // Simulate 11 seconds (60 FPS)
        for (int i = 0; i < 11 * 60; i++) {
            player.update(1.0 / 60.0);
        }

        assertEquals(PlayerImpl.PlayerState.BIG, player.getPlayerState());
    }

    // ------------------------------------------------------------
    // DAMAGE HANDLING
    // ------------------------------------------------------------

    @Test
    void testDamageWhenSmallDies() {
        boolean dead = player.takeDamage();
        assertTrue(dead);
    }

    @Test
        void testDamageWhenBigBecomesSmall() {
        player.setState(PlayerImpl.PlayerState.BIG);

        boolean dead = player.takeDamage();

        assertFalse(dead);
        assertEquals(PlayerImpl.PlayerState.SMALL, player.getPlayerState()); 
    }


    @Test
    void testDamageWhenInvincibleNoEffect() {
        player.setState(PlayerImpl.PlayerState.INVINCIBLE);

        boolean dead = player.takeDamage();

        assertFalse(dead);
        assertEquals(PlayerImpl.PlayerState.INVINCIBLE, player.getPlayerState());
    }

    // ------------------------------------------------------------
    // ANIMATIONS
    // ------------------------------------------------------------

    @Test
    void testSpriteIdle() {
        player.stopX();
        player.setOnGround(true);
        player.update(0.016);

        assertEquals(PlayerImpl.SpriteState.IDLE, player.getSpriteState());
    }

    @Test
    void testSpriteWalk() {
        player.moveRight();
        player.setOnGround(true);
        player.update(0.016);

        assertEquals(PlayerImpl.SpriteState.WALK, player.getSpriteState());
    }

    @Test
    void testSpriteJump() {
        player.setOnGround(false);
        player.update(0.016);

        assertEquals(PlayerImpl.SpriteState.JUMP, player.getSpriteState());
    }
}
