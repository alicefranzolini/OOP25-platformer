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
    private static final double SHELL_SPEED = 300.0;

    /** Possible states for a Koopa. */
    public enum KoopaState {
        /** Koopa is walking. */
        WALK,
        /** Koopa is inside its shell, stationary. */
        SHELL,
        /** Koopa's shell is sliding at high speed. */
        SHELL_MOVING
    }

    /** Walk handler: manages the sprite's direction and physics. */
    private static final class WalkHandler implements EnemyImpl.WalkingHandler {

        @Override
        public void update(final EnemyImpl e, final double deltaTime) {
            final double vx = e.getVelocityX();
            if (vx < 0) {
                e.setFacingLeft(false);
            } else if (vx > 0) {
                e.setFacingLeft(true);
            }
            e.getAnim().play("walk");
            e.getAnim().update(deltaTime);
            e.physicsTick(deltaTime);
        }

        @Override
        public void render(final EnemyImpl e, final GraphicsContext gc) {
            if (e.getAnim().hasAnimation("walk")) {
                e.getAnim().render(gc, e.getX(), e.getY(),
                        e.getWidth(), e.getHeight(), e.isFacingLeft());
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

    /** Handler for the shell state: manages the shell's appearance and non-lethal nature. */
    private static final class ShellHandler implements EnemyImpl.EnemyStateHandler {

        private static final int SHELL_HEIGHT = 32;

        @Override
        public void update(final EnemyImpl e, final double deltaTime) {
            e.getAnim().play("shell");
            e.getAnim().update(deltaTime);
        }

        @Override
        public void render(final EnemyImpl e, final GraphicsContext gc) {
            if (e.getAnim().hasAnimation("shell")) {
                e.getAnim().render(gc, e.getX(), e.getY(),
                        e.getWidth(), SHELL_HEIGHT, e.isFacingLeft());
            } else {
                gc.setFill(Color.DARKGREEN);
                gc.fillRect(e.getX(), e.getY(), e.getWidth(), SHELL_HEIGHT);
            }
        }

        @Override
        public boolean hitsPlayer() {
            return false;
        }
    }

    /** Handler for the shell moving state: manages the shell's movement and lethal nature. */
    private static final class ShellMovingHandler implements EnemyImpl.EnemyStateHandler {

        private static final int SHELL_HEIGHT = 32;

        @Override
        public void update(final EnemyImpl e, final double deltaTime) {
            final double vx = e.getVelocityX();
            if (vx < 0) {
                e.setFacingLeft(true);
            } else if (vx > 0) {
                e.setFacingLeft(false);
            }
            e.getAnim().play("shell_moving");
            e.getAnim().update(deltaTime);
            e.physicsTick(deltaTime);
        }

        @Override
        public void render(final EnemyImpl e, final GraphicsContext gc) {
            if (e.getAnim().hasAnimation("shell_moving")) {
                e.getAnim().render(gc, e.getX(), e.getY(),
                        e.getWidth(), SHELL_HEIGHT, e.isFacingLeft());
            } else {
                gc.setFill(Color.YELLOWGREEN);
                gc.fillRect(e.getX(), e.getY(), e.getWidth(), SHELL_HEIGHT);
            }
        }

        @Override
        public boolean hitsPlayer() {
            return true;
        }
    }

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
        init();
    }

    private void init() {
        transitionTo(KoopaState.WALK);
        setVelocityX(-WALK_SPEED);
        getAnim().play("walk");
    }

    @Override
    protected void loadAnimations() {
        final Image walkFrame1 = AnimationManager.loadImage("/sprites/enemies/koopa1.png");
        final Image walkFrame2 = AnimationManager.loadImage("/sprites/enemies/koopa2.png");
        final Image shellSprite = AnimationManager.loadImage("/sprites/enemies/koopa_shell.png");

        if (walkFrame1 != null && walkFrame2 != null) {
            getAnim().register("walk",
                    new Animation(new Image[]{walkFrame1, walkFrame2}, 0.15, true));
        } else {
            System.err.println("[Koopa] Walk sprites not found – using fallback.");
        }
        if (shellSprite != null) {
            getAnim().register("shell",
                    new Animation(new Image[]{shellSprite}, 1.0, false));
            getAnim().register("shell_moving",
                    new Animation(new Image[]{shellSprite}, 0.08, true));
        } else {
            System.err.println("[Koopa] Shell sprite not found – using fallback.");
        }
    }

    private void transitionTo(final KoopaState newState) {
        this.state = newState;
        final EnemyStateHandler handler = switch (newState) {
            case WALK -> new WalkHandler();
            case SHELL -> new ShellHandler();
            case SHELL_MOVING -> new ShellMovingHandler();
        };
        transitionTo(handler);
    }

    /**
     * Stomps this Koopa, retracting it into its shell.
     */
    public void stomp() {
        if (state != KoopaState.WALK) {
            return;
        }
        transitionTo(KoopaState.SHELL);
        setVelocityX(0);
        setHeight(32);
        setY(getY() + 16);
        setAffectedByGravity(true);
    }

    /**
     * Kicks the shell, sending it sliding in the given direction.
     *
     * @param kickRight {@code true} to kick right, {@code false} to kick left
     */
    public void kick(final boolean kickRight) {
        if (state != KoopaState.SHELL) {
            return;
        }
        transitionTo(KoopaState.SHELL_MOVING);
        setVelocityX(kickRight ? SHELL_SPEED : -SHELL_SPEED);
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
     * Returns whether this Koopa's shell can kill other enemies.
     *
     * @return {@code true} if the shell is moving
     */
    public boolean canKillEnemies() {
        return state == KoopaState.SHELL_MOVING;
    }

    /**
     * Returns whether this Koopa is currently inside its shell.
     *
     * @return {@code true} if in shell or shell-moving state
     */
    public boolean isInShell() {
        return state == KoopaState.SHELL || state == KoopaState.SHELL_MOVING;
    }
}