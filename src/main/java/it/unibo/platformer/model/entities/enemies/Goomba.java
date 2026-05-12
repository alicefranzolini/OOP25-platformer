package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.physics.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * A Goomba enemy.
 *
 * <p>Behaviour is implemented via the <em>State</em> pattern: each concrete
 * inner class handles its own {@code update} and {@code render} logic, so
 * adding a new state never requires touching the existing ones.
 *
 * <pre>
 *   WALK ──(stomp)──► SQUISHED ──(timer expires)──► destroyed
 * </pre>
 */
public class Goomba extends DynamicEntity {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final double WALK_SPEED    = 60.0;
    private static final double SQUISH_TIME   = 0.4;
    private static final double FRAME_DURATION = 0.2;

    // -------------------------------------------------------------------------
    // Animation
    // -------------------------------------------------------------------------

    private final AnimationManager anim = new AnimationManager();
    boolean facingLeft = true; // package-visible for inner state classes

    // -------------------------------------------------------------------------
    // State — pattern State with inner interface
    // -------------------------------------------------------------------------

    /**
     * Encapsulates all behaviour that changes with the Goomba's state.
     * Implementations are inner static classes below.
     */
    private interface GoombaState {
        /** Called every frame while the Goomba is in this state. */
        void update(Goomba g, double deltaTime);
        /** Draws the Goomba for this state. */
        void render(Goomba g, GraphicsContext gc);
        /** Returns true if touching the player kills/hurts them. */
        boolean hitsPlayer();
    }

    // -- WALK -----------------------------------------------------------------

    private static final class WalkState implements GoombaState {
        @Override
        public void update(Goomba g, double deltaTime) {
            if (g.getVelocityX() < 0) g.facingLeft = true;
            else if (g.getVelocityX() > 0) g.facingLeft = false;

            g.anim.play("walk");
            g.anim.update(deltaTime);
            g.physicsTick(deltaTime);
        }

        @Override
        public void render(Goomba g, GraphicsContext gc) {
            if (g.anim.hasAnimation("walk")) {
                g.anim.render(gc, g.getX(), g.getY(), g.getWidth(), g.getHeight(), g.facingLeft);
            } else {
                renderFallbackWalk(g, gc);
            }
        }

        private void renderFallbackWalk(Goomba g, GraphicsContext gc) {
            gc.setFill(Color.SADDLEBROWN);
            gc.fillRect(g.getX(), g.getY(), g.getWidth(), g.getHeight());
            gc.setFill(Color.WHITE);
            gc.fillOval(g.getX() + 4,  g.getY() + 8, 8, 8);
            gc.fillOval(g.getX() + 20, g.getY() + 8, 8, 8);
            gc.setFill(Color.BLACK);
            gc.fillOval(g.getX() + 6,  g.getY() + 10, 4, 4);
            gc.fillOval(g.getX() + 22, g.getY() + 10, 4, 4);
        }

        @Override
        public boolean hitsPlayer() { return true; }
    }

    // -- SQUISHED -------------------------------------------------------------

    private static final class SquishState implements GoombaState {
        private double squishTimer = 0;

        @Override
        public void update(Goomba g, double deltaTime) {
            g.anim.play("squished");
            g.anim.update(deltaTime);
            squishTimer += deltaTime;
            if (squishTimer >= SQUISH_TIME) {
                g.destroy();
            }
        }

        @Override
        public void render(Goomba g, GraphicsContext gc) {
            double halfH = g.getHeight() / 2.0;
            if (g.anim.hasAnimation("squished")) {
                g.anim.render(gc, g.getX(), g.getY() + halfH, g.getWidth(), halfH, g.facingLeft);
            } else {
                gc.setFill(Color.SADDLEBROWN);
                gc.fillRect(g.getX(), g.getY() + halfH, g.getWidth(), halfH);
            }
        }

        @Override
        public boolean hitsPlayer() { return false; }
    }

    // -------------------------------------------------------------------------
    // Current state field
    // -------------------------------------------------------------------------

    private GoombaState state;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Convenience constructor that uses a default {@link BasicPhysics} instance.
     * Useful for tests and simple factory methods that do not need to inject
     * a custom physics implementation.
     *
     * @param x initial X position
     * @param y initial Y position
     */
    public Goomba(double x, double y) {
        this(x, y, new BasicPhysics());
    }

    /**
     * Full constructor that accepts an explicit {@link BasicPhysics} instance.
     * Use this when you need to inject a custom or mock physics object.
     *
     * @param x       initial X position
     * @param y       initial Y position
     * @param physics physics engine to use
     */
    public Goomba(double x, double y, BasicPhysics physics) {
        super(x, y, 32, 32, physics);
        this.state = new WalkState();
        setVelocityX(-WALK_SPEED);
        loadAnimations();
        anim.play("walk");
    }

    // -------------------------------------------------------------------------
    // Animation loading
    // -------------------------------------------------------------------------

    /**
     * Loads walk and squish animations from individual PNG files.
     *
     * <pre>
     *   /sprites/enemies/goomba1.png    – walk frame 1
     *   /sprites/enemies/goomba2.png    – walk frame 2
     *   /sprites/enemies/goomba_dead.png – squished sprite
     * </pre>
     */
    private void loadAnimations() {
        Image frame1 = AnimationManager.loadImage("/sprites/enemies/goomba1.png");
        Image frame2 = AnimationManager.loadImage("/sprites/enemies/goomba2.png");
        Image dead   = AnimationManager.loadImage("/sprites/enemies/goomba_dead.png");

        if (frame1 != null && frame2 != null) {
            anim.register("walk", new Animation(new Image[]{frame1, frame2}, FRAME_DURATION, true));
        } else {
            System.err.println("[Goomba] Walk sprites not found – using fallback.");
        }

        if (dead != null) {
            anim.register("squished", new Animation(new Image[]{dead}, SQUISH_TIME, false));
        } else {
            System.err.println("[Goomba] Squish sprite not found – using fallback.");
        }
    }

    // -------------------------------------------------------------------------
    // Game loop — delegated entirely to the current state
    // -------------------------------------------------------------------------

    @Override
    public void update(double deltaTime) {
        state.update(this, deltaTime);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!isActive()) return;
        state.render(this, gc);
    }

    // -------------------------------------------------------------------------
    // Physics helper — called by WalkState so super.update() is not public API
    // -------------------------------------------------------------------------

    void physicsTick(double deltaTime) {
        super.update(deltaTime);
    }

    // -------------------------------------------------------------------------
    // State transitions
    // -------------------------------------------------------------------------

    /**
     * Called when Mario jumps on the Goomba from above.
     * Transitions to the squished state; does nothing if already squished.
     */
    public void squish() {
        if (!(state instanceof WalkState)) return;
        state = new SquishState();
        setVelocityX(0);
        affectedByGravity = false;
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    /** Returns true if this Goomba can currently hurt the player on contact. */
    public boolean hitsPlayer() {
        return state.hitsPlayer();
    }

    /** Returns true if this Goomba is walking (not squished or dead). */
    public boolean isWalking() {
        return state instanceof WalkState;
    }
}