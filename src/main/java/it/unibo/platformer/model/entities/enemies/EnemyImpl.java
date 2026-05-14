package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.physics.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;


public abstract class EnemyImpl extends DynamicEntity implements Enemy {

   
    public interface EnemyStateHandler {
        /** Called every frame while the enemy is in this state. */
        void update(EnemyImpl e, double deltaTime);
        /** Draws the enemy for this state. */
        void render(EnemyImpl e, GraphicsContext gc);
        /** Returns true if touching the player hurts or kills them. */
        boolean hitsPlayer();
    }

    // -------------------------------------------------------------------------
    // Marker interface: WalkingHandler
    // -------------------------------------------------------------------------

    
    public interface WalkingHandler extends EnemyStateHandler {}

    // -------------------------------------------------------------------------
    // Shared fields
    // -------------------------------------------------------------------------

    /** Animation controller — shared by all enemies. */
    protected final AnimationManager anim = new AnimationManager();

    /** True when the enemy sprite should be mirrored horizontally. */
    protected boolean facingLeft = true;

    /** The active state handler; drives update() and render(). */
    private EnemyStateHandler handler;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    
    protected EnemyImpl(double x, double y, double width, double height, BasicPhysics physics) {
        super(x, y, width, height, physics);
        loadAnimations();
    }


    // -------------------------------------------------------------------------
    // Template Method — subclasses register their own sprites
    // -------------------------------------------------------------------------

    /**
     * Called once during construction.
     * Subclasses register their animations via {@link #anim}.
     */
    protected abstract void loadAnimations();

    // -------------------------------------------------------------------------
    // Game loop — final: subclasses must not override these
    // -------------------------------------------------------------------------

    @Override
    public final void update(double deltaTime) {
        if (handler != null) handler.update(this, deltaTime);
    }

    @Override
    public final void render(GraphicsContext gc) {
        if (!isActive() || handler == null) return;
        handler.render(this, gc);
    }

    // -------------------------------------------------------------------------
    // State transition
    // -------------------------------------------------------------------------

    /**
     * Switches the active state handler.
     * Subclasses call this from their own transition methods (e.g. squish(), stomp()).
     */
    protected void transitionTo(EnemyStateHandler newHandler) {
        this.handler = newHandler;
    }

    // -------------------------------------------------------------------------
    // Physics helper — called by WalkingHandler implementations
    // -------------------------------------------------------------------------

    
    protected void physicsTick(double deltaTime) {
        super.update(deltaTime);
    }

    // -------------------------------------------------------------------------
    // Shared queries — work for every enemy via the handler/marker interface
    // -------------------------------------------------------------------------

    /** Returns true if this enemy can currently hurt the player on contact. */
    public boolean hitsPlayer() {
        return handler != null && handler.hitsPlayer();
    }

   
    public boolean isWalking() {
        return handler instanceof WalkingHandler;
    }
}