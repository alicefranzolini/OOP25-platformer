package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.entities.AbstractDynamicEntity;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import java.util.Optional;

/**
 * Basic implementation that centralizes physics, animations, and state management.
 */
public abstract class EnemyImpl extends AbstractDynamicEntity implements Enemy {

    /** Animation manager shared by all enemy states. */
    private final AnimationManager anim = new AnimationManager();

    /** True if the sprite is currently facing left. */
    private boolean facingLeft = true;

    /** The active state handler; drives {@link #update} and {@link #render}. */
    private Optional<EnemyStateHandler> handler = Optional.empty();

    /**
     * Constructs an EnemyImpl with the given position, dimensions and physics engine.
     * Calls {@link #loadAnimations()} so subclasses can register their sprites.
     *
     * @param x       the horizontal position
     * @param y       the vertical position
     * @param width   the width of the entity
     * @param height  the height of the entity
     * @param physics the {@link BasicPhysics} engine to use for movement
     */
    protected EnemyImpl(final double x, final double y, final double width, final double height, final BasicPhysics physics) {
        super(x, y, width, height, physics);
        loadAnimations();
    }

    /**
     * Registers the sprites used by this enemy.
     * Subclasses must implement this to load their own animations.
     */
    protected abstract void loadAnimations();

    /**
     * @return the animation manager for this enemy
     */
    protected AnimationManager getAnim() {
        return anim;
    }

    /**
     * @return true if the sprite is currently facing left
     */
    public boolean isFacingLeft() {
        return facingLeft;
    }

    /**
     * @param facingLeft true to face left, false to face right
     */
    public void setFacingLeft(final boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    /**
     * Delegates the update logic to the active {@link EnemyStateHandler}.
     * Subclasses must not override this method.
     *
     * @param deltaTime the time elapsed since the last frame, in seconds
     */
    @Override
    public final void update(final double deltaTime) {
        handler.ifPresent(h -> h.update(this, deltaTime));
    }

    /**
     * Delegates rendering to the active {@link EnemyStateHandler}.
     * Does nothing if this enemy is no longer active.
     * Subclasses must not override this method.
     *
     * @param gc the {@link GraphicsContext} to render onto
     */
    @Override
    public final void render(final GraphicsContext gc) {
        if (!isActive()) {
            return;
        }
        handler.ifPresent(h -> h.render(this, gc));
    }

    /**
     * Transitions this enemy to a new state.
     *
     * @param newHandler the {@link EnemyStateHandler} representing the new state
     */
    protected void transitionTo(final EnemyStateHandler newHandler) {
        this.handler = Optional.of(newHandler);
    }

    /**
     * Advances the base physics simulation by one tick.
     * Can be called by state handlers as needed.
     *
     * @param deltaTime the time elapsed since the last frame, in seconds
     */
    protected void physicsTick(final double deltaTime) {
        super.update(deltaTime);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hitsPlayer() {
        return handler.map(EnemyStateHandler::hitsPlayer).orElse(false);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWalking() {
        return handler.map(h -> h instanceof WalkingHandler).orElse(false);
    }

    /**
     * Defines the behavior of a single enemy state.
     * Each state has its own update/render logic and player interaction rules.
     */
    public interface EnemyStateHandler {

        /**
         * Updates the state logic for the given enemy.
         *
         * @param e         the enemy to update
         * @param deltaTime the time elapsed since the last frame, in seconds
         */
        void update(EnemyImpl e, double deltaTime);

        /**
         * Draws the enemy in its current state.
         *
         * @param e  the enemy to render
         * @param gc the {@link GraphicsContext} to render onto
         */
        void render(EnemyImpl e, GraphicsContext gc);

        /**
         * @return true if the enemy in this state can hurt or kill the player
         */
        boolean hitsPlayer();
    }

    /** Marker interface to group all walking states. */
    public interface WalkingHandler extends EnemyStateHandler {
    }

}
