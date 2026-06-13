package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.players.Player;

/**
 * Contract for all power-up entities.
 */
public interface PowerUp {

    /**
     * Updates the power-up state.
     *
     * @param deltaTime the elapsed time since the previous update
     */
    void update(double deltaTime);

    /**
     * Applies the specific effect to the player.
     *
     * @param player the player collecting the power-up
     */
    void applyEffect(Player player);

    /**
     * Collects the power-up, applies its effect, and destroys it.
     *
     * @param player the player collecting the power-up
     */
    void collect(Player player);

    /**
     * Reverses the horizontal movement direction.
     */
    void reverseDirection();

    /**
     * Checks whether the power-up is still emerging from its spawn block.
     *
     * @return true if the power-up is emerging
     */
    boolean isEmerging();

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
     * Gets the entity width.
     *
     * @return the width
     */
    double getWidth();

    /**
     * Gets the entity height.
     *
     * @return the height
     */
    double getHeight();

    /**
     * Checks whether the power-up is active.
     *
     * @return true if the power-up is active
     */
    boolean isActive();
}
