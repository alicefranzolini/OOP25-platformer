package it.unibo.platformer.model.entities.enemies;

import javafx.scene.canvas.GraphicsContext;

/**
 * Defines the behaviors observable by the game system (loops, collisions, HUD)
 * without exposing implementation details.
 */
public interface Enemy {

    /**
     * Updates the enemy's logic for the current frame.
     *
     * @param deltaTime the time elapsed since the last frame, in seconds
     */
    void update(double deltaTime);

    /**
     * Draws this enemy onto the given graphics context.
     *
     * @param gc the {@link GraphicsContext} to render onto
     */
    void render(GraphicsContext gc);

    /**
     * @return true if this enemy can currently hurt or kill the player
     */
    boolean hitsPlayer();

    /**
     * @return true if this enemy is currently moving
     */
    boolean isWalking();

    /**
     * @return true if this enemy is currently active in the game world
     */
    boolean isActive();

    /**
     * @return the horizontal position of this enemy
     */
    double getX();

    /**
     * @return the vertical position of this enemy
     */
    double getY();

    /**
     * @return the width of this enemy
     */
    double getWidth();

    /**
     * @return the height of this enemy
     */
    double getHeight();
}