package it.unibo.platformer.model.entities.enemies;

import javafx.scene.canvas.GraphicsContext;

/**
 * Public contract for all enemy entities in the game.
 *
 * <p>This interface defines only the observable behaviour that the rest of the
 * system (game loop, collision, HUD) needs to know about an enemy, without
 * exposing any implementation detail.
 *
 * <p>The full implementation lives in {@link Enemy} (abstract class), which
 * centralises the shared logic (animation, physics, state dispatching).
 * Concrete enemies ({@link Goomba}, {@link Koopa}) extend {@code Enemy} and
 * automatically satisfy this interface.
 */
public interface Enemy {

    /**
     * Advances the enemy's logic by {@code deltaTime} seconds.
     * Called once per game frame.
     */
    void update(double deltaTime);

    /**
     * Draws the enemy onto the given graphics context.
     * Called once per render frame, after {@link #update}.
     */
    void render(GraphicsContext gc);

    /**
     * Returns {@code true} if this enemy can currently hurt or kill the player
     * on contact (e.g. a walking Goomba, a moving Koopa shell).
     */
    boolean hitsPlayer();

    /**
     * Returns {@code true} if this enemy is currently in a walking state.
     * Useful for collision systems that need to distinguish walking enemies
     * from shells, projectiles, or defeated enemies.
     */
    boolean isWalking();

    /**
     * Returns {@code true} if this enemy is still active in the world.
     * An inactive enemy has been destroyed and should be removed from the scene.
     */
    boolean isActive();

    /** Returns the enemy's current x position in world coordinates. */
    double getX();

    /** Returns the enemy's current y position in world coordinates. */
    double getY();

    /** Returns the enemy's width in pixels. */
    double getWidth();

    /** Returns the enemy's height in pixels. */
    double getHeight();
}