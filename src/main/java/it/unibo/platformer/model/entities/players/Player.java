package it.unibo.platformer.model.entities.players;

/**
 * Contract for the main controllable character.
 * Exposes input handling, state management, and status queries
 * without depending on the concrete Player implementation.
 */
public interface Player {

    /**
     * Possible player states.
     */
    enum PlayerState {
        /** Small player state. */
        SMALL,
        /** Big player state. */
        BIG,
        /** Temporarily invincible player state. */
        INVINCIBLE
    }

    /**
     * Possible sprite animation states.
     */
    enum SpriteState {
        /** Idle sprite state. */
        IDLE,
        /** Walking sprite state. */
        WALK,
        /** Jumping sprite state. */
        JUMP,
        /** Dead sprite state. */
        DEAD
    }

    /**
     * Starts moving the player left.
     */
    void moveLeft();

    /**
     * Starts moving the player right.
     */
    void moveRight();

    /**
     * Stops horizontal movement.
     */
    void stopX();

    /**
     * Sets whether a jump has been requested.
     *
     * @param value true if a jump is requested
     */
    void setJumpRequested(boolean value);

    /**
     * Changes the player state.
     *
     * @param newState the new player state
     */
    void setState(PlayerState newState);

    /**
     * Applies damage to the player.
     *
     * @return true if damage was applied
     */
    boolean takeDamage();

    /**
     * Kills the player.
     */
    void die();

    /**
     * Gets the current player state.
     *
     * @return the current player state
     */
    PlayerState getPlayerState();

    /**
     * Gets the current sprite state.
     *
     * @return the current sprite state
     */
    SpriteState getSpriteState();

    /**
     * Checks whether the player is facing right.
     *
     * @return true if the player is facing right
     */
    boolean isFacingRight();

    /**
     * Checks whether the player is invincible.
     *
     * @return true if the player is invincible
     */
    boolean isInvincible();

    /**
     * Checks whether the player is dying.
     *
     * @return true if the player is dying
     */
    boolean isDying();

    /**
     * Checks whether the death animation is complete.
     *
     * @return true if the death animation is complete
     */
    boolean isDeathComplete();

    /**
     * Checks whether the player is solid.
     *
     * @return true if the player is solid
     */
    boolean isSolid();

    /**
     * Gets the x coordinate.
     *
     * @return the x coordinate
     */
    double getX();

    /**
     * Gets the y coordinate.
     *
     * @return the y coordinate
     */
    double getY();

    /**
     * Gets the player width.
     *
     * @return the width
     */
    double getWidth();

    /**
     * Gets the player height.
     *
     * @return the height
     */
    double getHeight();

    /**
     * Checks whether the player is active.
     *
     * @return true if the player is active
     */
    boolean isActive();
}
