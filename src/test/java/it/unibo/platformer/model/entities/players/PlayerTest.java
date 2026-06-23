package it.unibo.platformer.model.entities.players;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for player movement, state transitions and damage handling.
 */
final class PlayerTest {

    private static final double DELTA = 0.01;
    private static final double UPDATE_TIME = 0.016;
    private static final double LEFT_SPEED = -180.0;
    private static final double JUMP_SPEED = -420.0;
    private static final int BIG_HEIGHT = 48;
    private static final int INVINCIBILITY_SECONDS = 11;
    private static final int FPS = 60;
    private static final double FRAME_TIME = 1.0 / FPS;

    private PlayerImpl player;

    @BeforeEach
    void setUp() {
        final BasicPhysics physics = new BasicPhysicsImpl();
        player = new PlayerImpl(100, 100, physics);
    }

    // ------------------------------------------------------------
    // HORIZONTAL MOVEMENT
    // ------------------------------------------------------------

    @Test
    void testMoveRight() {
        player.moveRight();
        assertEquals(180.0, player.getVelocityX(), DELTA);
    }

    @Test
    void testMoveLeft() {
        player.moveLeft();
        assertEquals(LEFT_SPEED, player.getVelocityX(), DELTA);
    }

    @Test
    void testStopX() {
        player.moveRight();
        player.stopX();
        assertEquals(0.0, player.getVelocityX(), DELTA);
    }

    // ------------------------------------------------------------
    // JUMPING
    // ------------------------------------------------------------

    @Test
    void testJumpWhenOnGround() {
        player.setOnGround(true);
        player.setJumpRequested(true);

        player.update(UPDATE_TIME);

        assertTrue(player.getVelocityY() < 0.0);
        assertFalse(player.isOnGround());
    }

    @Test
    void testJumpNotAllowedInAir() {
        player.setOnGround(false);
        player.setJumpRequested(true);

        player.update(UPDATE_TIME);

        assertNotEquals(JUMP_SPEED, player.getVelocityY(), DELTA);
    }

    // ------------------------------------------------------------
    // PLAYER STATES
    // ------------------------------------------------------------

    @Test
    void testPowerUpSmallToBig() {
        player.setState(PlayerImpl.PlayerState.BIG);
        assertEquals(PlayerImpl.PlayerState.BIG, player.getPlayerState());
        assertEquals(BIG_HEIGHT, player.getHeight());
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
        for (int i = 0; i < INVINCIBILITY_SECONDS * FPS; i++) {
            player.update(FRAME_TIME);
        }

        assertEquals(PlayerImpl.PlayerState.BIG, player.getPlayerState());
    }

    // ------------------------------------------------------------
    // DAMAGE HANDLING
    // ------------------------------------------------------------

    @Test
    void testDamageWhenSmallDies() {
        final boolean dead = player.takeDamage();
        assertTrue(dead);
    }

    @Test
    void testDamageWhenBigBecomesSmall() {
        player.setState(PlayerImpl.PlayerState.BIG);

        final boolean dead = player.takeDamage();

        assertFalse(dead);
        assertEquals(PlayerImpl.PlayerState.SMALL, player.getPlayerState());
    }

    @Test
    void testDamageWhenInvincibleNoEffect() {
        player.setState(PlayerImpl.PlayerState.INVINCIBLE);

        final boolean dead = player.takeDamage();

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
        player.update(UPDATE_TIME);

        assertEquals(PlayerImpl.SpriteState.IDLE, player.getSpriteState());
    }

    @Test
    void testSpriteWalk() {
        player.moveRight();
        player.setOnGround(true);
        player.update(UPDATE_TIME);

        assertEquals(PlayerImpl.SpriteState.WALK, player.getSpriteState());
    }

    @Test
    void testSpriteJump() {
        player.setOnGround(false);
        player.update(UPDATE_TIME);

        assertEquals(PlayerImpl.SpriteState.JUMP, player.getSpriteState());
    }
}
