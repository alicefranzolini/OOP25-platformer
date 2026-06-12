package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * A Koopa enemy that walks, can be stomped into a shell, and kicked.
 */
public class Koopa extends EnemyImpl {

    private static final double WALK_SPEED = 50.0;
    private static final double WALK_FRAME_DURATION = 0.15;
    private static final double SHELL_FRAME_DURATION = 0.08;

    private static final String WALK_ANIMATION = "walk";
    private static final String SHELL_ANIMATION = "shell";
    private static final String SHELL_MOVING_ANIMATION = "shell_moving";
    
    private KoopaState state;

    /**
     * Constructs a Koopa at the given position.
     *
     * @param x       the initial x coordinate
     * @param y       the initial y coordinate
     * @param physics the physics engine to use
     */
    public Koopa(final double x, final double y, final BasicPhysics physics) {
        super(x, y, 32, 48, physics);
        this.state = KoopaState.WALK;
        transitionTo(KoopaState.WALK);
        setVelocityX(-WALK_SPEED);
    }

    @Override
    protected void loadAnimations() {
        final Image walk1 = AnimationManager.loadImage("/sprites/enemies/koopa1.png");
        final Image walk2 = AnimationManager.loadImage("/sprites/enemies/koopa2.png");
        final Image shell = AnimationManager.loadImage("/sprites/enemies/koopa_shell.png");

       if (walk1 != null && walk2 != null) {
            getAnim().register(WALK_ANIMATION,
                    new Animation(new Image[]{walk1, walk2}, WALK_FRAME_DURATION, true));
        }
        if (shell != null) {
            getAnim().register(SHELL_ANIMATION,
                    new Animation(new Image[]{shell}, 1.0, false));
            getAnim().register(SHELL_MOVING_ANIMATION,
                    new Animation(new Image[]{shell}, SHELL_FRAME_DURATION, true));
        }
    }

    private void transitionTo(final KoopaState newState) {
        this.state = newState;
        switch (newState) {
            case WALK -> transitionTo(new WalkHandler());
            case SHELL -> transitionTo(new ShellHandler());
            case SHELL_MOVING -> transitionTo(new ShellMovingHandler());
        }
    }

    /**
     * Returns the current state of this Koopa.
     *
     * @return the current {@link KoopaState}
     */
    public KoopaState getState() {
        return state;
    }

    /**
     * Possible states for a Koopa.
     */
    public enum KoopaState {
        /** Koopa is walking. */
        WALK,
        /** Koopa is inside its shell, stationary. */
        SHELL,
        /** Koopa's shell is moving fast after being kicked. */
        SHELL_MOVING
    }

    /** Walk handler. */
    private static final class WalkHandler implements EnemyImpl.WalkingHandler {
        @Override
        public void update(final EnemyImpl e, final double deltaTime) {
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
        public void render(final EnemyImpl e, final GraphicsContext gc) {
            if (e.getAnim().hasAnimation(WALK_ANIMATION)) {
                e.getAnim().render(gc, e.getX(), e.getY(), e.getWidth(), e.getHeight(), e.isFacingLeft());
            } else {
                gc.setFill(Color.GREEN);
                gc.fillRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
            }
        }

        @Override
        public boolean hitsPlayer() {
            return true;
        }
    }

    /** Shell handler. */
    private static final class ShellHandler implements EnemyImpl.EnemyStateHandler {
        @Override
        public void update(final EnemyImpl e, final double deltaTime) {
            e.getAnim().play(SHELL_ANIMATION);
            e.getAnim().update(deltaTime);
            e.physicsTick(deltaTime);
        }

        @Override
        public void render(final EnemyImpl e, final GraphicsContext gc) {
            if (e.getAnim().hasAnimation(SHELL_ANIMATION)) {
                e.getAnim().render(gc, e.getX(), e.getY(), e.getWidth(), e.getHeight(), e.isFacingLeft());
            }
        }

        @Override
        public boolean hitsPlayer() {
            return false;
        }
    }

    /** Shell moving handler. */
    private static final class ShellMovingHandler implements EnemyImpl.EnemyStateHandler {
        @Override
        public void update(final EnemyImpl e, final double deltaTime) {
            e.getAnim().play(SHELL_MOVING_ANIMATION);
            e.getAnim().update(deltaTime);
            e.physicsTick(deltaTime);
        }

        @Override
        public void render(final EnemyImpl e, final GraphicsContext gc) {
            if (e.getAnim().hasAnimation(SHELL_MOVING_ANIMATION)) {
                e.getAnim().render(gc, e.getX(), e.getY(), e.getWidth(), e.getHeight(), e.isFacingLeft());
            }
        }

        @Override
        public boolean hitsPlayer() {
            return true;
        }
    }
}
