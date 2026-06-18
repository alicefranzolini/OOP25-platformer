package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * A Goomba enemy that walks until squished by the player.
 */
public final class Goomba extends AbstractEnemyImpl {

    private static final double WALK_SPEED = 60.0;
    private static final double SQUISH_TIME = 0.4;
    private static final double FRAME_DURATION = 0.2;

    private static final String WALK_ANIMATION = "walk";
    private static final String SQUISHED_ANIMATION = "squished";

    private static final double FALLBACK_X_OFFSET_1 = 4.0;
    private static final double FALLBACK_X_OFFSET_2 = 20.0;
    private static final double FALLBACK_X_EYE_1 = 6.0;
    private static final double FALLBACK_X_EYE_2 = 22.0;

    private GoombaState state;

    /**
     * Constructs a Goomba at the given position.
     *
     * @param x       the initial x coordinate
     * @param y       the initial y coordinate
     * @param physics the physics engine to use
     */
    public Goomba(final double x, final double y, final BasicPhysics physics) {
        super(x, y, 32, 32, physics);
        this.state = GoombaState.WALK;
        transitionTo(GoombaState.WALK);
        setVelocityX(-WALK_SPEED);
    }

    @Override
    protected void loadAnimations() {
        final Image frame1 = AnimationManager.loadImage("/sprites/enemies/goomba1.png");
        final Image frame2 = AnimationManager.loadImage("/sprites/enemies/goomba2.png");
        final Image dead = AnimationManager.loadImage("/sprites/enemies/goomba_dead.png");

        if (frame1 != null && frame2 != null) {
            getAnim().register("walk",
                    new Animation(new Image[]{frame1, frame2}, FRAME_DURATION, true));
        } else {
            System.err.println("[Goomba] Walk sprites not found – using fallback.");
        }
        if (dead != null) {
            getAnim().register("squished",
                    new Animation(new Image[]{dead}, SQUISH_TIME, false));
        } else {
            System.err.println("[Goomba] Squish sprite not found – using fallback.");
        }
    }

    private void transitionTo(final GoombaState newState) {
        this.state = newState;
        transitionTo(newState == GoombaState.WALK
                ? new WalkHandler()
                : new SquishHandler());
    }

    /**
     * Squishes this Goomba, transitioning it to the squished state.
     */
    public void squish() {
        if (state != GoombaState.WALK) {
            return;
        }
        transitionTo(GoombaState.SQUISHED);
        setVelocityX(0);
        setAffectedByGravity(false);
    }

    /**
     * Returns the current state of this Goomba.
     *
     * @return the current {@link GoombaState}
     */
    public GoombaState getState() {
        return state;
    }

    /** Possible states for a Goomba. */
    public enum GoombaState {
        /** Goomba is walking. */
        WALK,
        /** Goomba has been squished. */
        SQUISHED
    }

    /** Walk handler: manages the sprite's direction and physics. */
    private final class WalkHandler implements AbstractEnemyImpl.WalkingHandler {

        @Override
        public void update(final AbstractEnemyImpl e, final double deltaTime) {
            final double vx = e.getVelocityX();
            if (vx < 0) {
                e.setFacingLeft(true);
            } else if (vx > 0) {
                e.setFacingLeft(false);
            }
            e.getAnim().play(WALK_ANIMATION);
            e.getAnim().update(deltaTime);
            e.physicsTick(deltaTime);
        }

        @Override
        public void render(final AbstractEnemyImpl e, final GraphicsContext gc) {
           if (e.getAnim().hasAnimation(WALK_ANIMATION)) {
                e.getAnim().render(gc, e.getX(), e.getY(),
                        e.getWidth(), e.getHeight(), e.isFacingLeft());
            } else {
                renderFallback(e, gc);
            }
        }

        private void renderFallback(final AbstractEnemyImpl e, final GraphicsContext gc) {
            gc.setFill(Color.SADDLEBROWN);
            gc.fillRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
            gc.setFill(Color.WHITE);
            gc.fillOval(e.getX() + FALLBACK_X_OFFSET_1, e.getY() + 8, 8, 8);
            gc.fillOval(e.getX() + FALLBACK_X_OFFSET_2, e.getY() + 8, 8, 8);
            gc.setFill(Color.BLACK);
            gc.fillOval(e.getX() + FALLBACK_X_EYE_1, e.getY() + 10, 4, 4);
            gc.fillOval(e.getX() + FALLBACK_X_EYE_2, e.getY() + 10, 4, 4);
        }

        @Override
        public boolean hitsPlayer() {
            return true;
        }
    }

    /** Handler for the squished state: manages the squish animation and eventual removal. */
    private final class SquishHandler implements AbstractEnemyImpl.EnemyStateHandler {

        private double squishTimer;

        @Override
        public void update(final AbstractEnemyImpl e, final double deltaTime) {
            e.getAnim().play(SQUISHED_ANIMATION);
            e.getAnim().update(deltaTime);
            squishTimer += deltaTime;
            if (squishTimer >= SQUISH_TIME) {
                e.destroy();
            }
        }

        @Override
        public void render(final AbstractEnemyImpl e, final GraphicsContext gc) {
            final double halfH = e.getHeight() / 2.0;
            final double drawY = e.getY() + halfH;
           if (e.getAnim().hasAnimation(SQUISHED_ANIMATION)) {
                e.getAnim().render(gc, e.getX(), drawY,
                        e.getWidth(), halfH, e.isFacingLeft());
            } else {
                gc.setFill(Color.SADDLEBROWN);
                gc.fillRect(e.getX(), drawY, e.getWidth(), halfH);
            }
        }

        @Override
        public boolean hitsPlayer() {
            return false;
        }
    }
}
