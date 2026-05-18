package it.unibo.platformer.model.entities.players;

/**
 * Contract for the main controllable character.
 * Exposes input handling, state management, and status queries
 * without depending on the concrete Player implementation.
 */
public interface Player {

    // Enums here so both the interface and PlayerImpl can use them
    enum PlayerState { SMALL, BIG, INVINCIBLE }
    enum SpriteState { IDLE, WALK, JUMP, DEAD }

    // Input
    void moveLeft();
    void moveRight();
    void stopX();
    void setJumpRequested(boolean v);

    // State management
    void setState(PlayerState newState);
    boolean takeDamage();
    void die();

    // Queries
    PlayerState getPlayerState();
    SpriteState getSpriteState();
    boolean isFacingRight();
    boolean isInvincible();
    boolean isDying();
    boolean isDeathComplete();
    boolean isSolid();

    // Position and size
    double getX();
    double getY();
    double getWidth();
    double getHeight();
    boolean isActive();
}