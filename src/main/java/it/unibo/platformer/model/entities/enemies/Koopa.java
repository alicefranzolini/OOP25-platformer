package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class Koopa extends EnemyImpl {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final double WALK_SPEED  = 50.0;
    private static final double SHELL_SPEED = 300.0;

    // -------------------------------------------------------------------------
    // Public state enum
    // -------------------------------------------------------------------------

    public enum KoopaState { WALK, SHELL, SHELL_MOVING }

    // -------------------------------------------------------------------------
    // Private state handlers
    // -------------------------------------------------------------------------

    private static final class WalkHandler implements EnemyImpl.WalkingHandler {
        @Override
        public void update(EnemyImpl e, double deltaTime) {
            double vx = e.getVelocityX();
            if (vx < 0) e.facingLeft = true;
            else if (vx > 0) e.facingLeft = false;
            e.anim.play("walk");
            e.anim.update(deltaTime);
            e.physicsTick(deltaTime);
        }

        @Override
        public void render(EnemyImpl e, GraphicsContext gc) {
            if (e.anim.hasAnimation("walk")) {
                e.anim.render(gc, e.getX(), e.getY(), e.getWidth(), e.getHeight(), e.facingLeft);
            } else {
                gc.setFill(Color.GREEN);
                gc.fillRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
            }
        }

        @Override
        public boolean hitsPlayer() { return true; }
    }

    private static final class ShellHandler implements EnemyImpl.EnemyStateHandler {
        @Override
        public void update(EnemyImpl e, double deltaTime) {
            e.anim.play("shell");
            e.anim.update(deltaTime);
        }

        @Override
        public void render(EnemyImpl e, GraphicsContext gc) {
            if (e.anim.hasAnimation("shell")) {
                e.anim.render(gc, e.getX(), e.getY(), e.getWidth(), 32, e.facingLeft);
            } else {
                gc.setFill(Color.DARKGREEN);
                gc.fillRect(e.getX(), e.getY(), e.getWidth(), 32);
            }
        }

        @Override
        public boolean hitsPlayer() { return false; }
    }

    private static final class ShellMovingHandler implements EnemyImpl.EnemyStateHandler {
        @Override
        public void update(EnemyImpl e, double deltaTime) {
            double vx = e.getVelocityX();
            if (vx < 0) e.facingLeft = true;
            else if (vx > 0) e.facingLeft = false;
            e.anim.play("shell_moving");
            e.anim.update(deltaTime);
            e.physicsTick(deltaTime);
        }

        @Override
        public void render(EnemyImpl e, GraphicsContext gc) {
            if (e.anim.hasAnimation("shell_moving")) {
                e.anim.render(gc, e.getX(), e.getY(), e.getWidth(), 32, e.facingLeft);
            } else {
                gc.setFill(Color.YELLOWGREEN);
                gc.fillRect(e.getX(), e.getY(), e.getWidth(), 32);
            }
        }

        @Override
        public boolean hitsPlayer() { return true; }
    }

    // -------------------------------------------------------------------------
    // State tracking
    // -------------------------------------------------------------------------

    private KoopaState state;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Koopa(double x, double y, BasicPhysics physics) {
        super(x, y, 32, 48, physics);
        init();
    }

    private void init() {
        transitionTo(KoopaState.WALK);
        setVelocityX(-WALK_SPEED);
         facingLeft = true;
        anim.play("walk");
    }

    // -------------------------------------------------------------------------
    // Template Method implementation
    // -------------------------------------------------------------------------

    @Override
    protected void loadAnimations() {
        Image walkFrame1  = AnimationManager.loadImage("/sprites/enemies/koopa1.png");
        Image walkFrame2  = AnimationManager.loadImage("/sprites/enemies/koopa2.png");
        Image shellSprite = AnimationManager.loadImage("/sprites/enemies/koopa_shell.png");

        if (walkFrame1 != null && walkFrame2 != null) {
            anim.register("walk", new Animation(new Image[]{walkFrame1, walkFrame2}, 0.15, true));
        } else {
            System.err.println("[Koopa] Walk sprites not found – using fallback.");
        }
        if (shellSprite != null) {
            anim.register("shell",        new Animation(new Image[]{shellSprite}, 1.0,  false));
            anim.register("shell_moving", new Animation(new Image[]{shellSprite}, 0.08, true));
        } else {
            System.err.println("[Koopa] Shell sprite not found – using fallback.");
        }
    }

    // -------------------------------------------------------------------------
    // State transition
    // -------------------------------------------------------------------------

    private void transitionTo(KoopaState newState) {
        this.state = newState;
        EnemyStateHandler h = switch (newState) {
            case WALK         -> new WalkHandler();
            case SHELL        -> new ShellHandler();
            case SHELL_MOVING -> new ShellMovingHandler();
        };
        transitionTo(h);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public void stomp() {
        if (state != KoopaState.WALK) return;
        transitionTo(KoopaState.SHELL);
        setVelocityX(0);
        setHeight(32);
        setY(getY() + 16);
        affectedByGravity = true;
    }

    public void kick(boolean kickRight) {
        if (state != KoopaState.SHELL) return;
        transitionTo(KoopaState.SHELL_MOVING);
        setVelocityX(kickRight ? SHELL_SPEED : -SHELL_SPEED);
    }

    public KoopaState getState()       { return state; }
    public boolean canKillEnemies()    { return state == KoopaState.SHELL_MOVING; }
    public boolean isInShell()         { return state == KoopaState.SHELL || state == KoopaState.SHELL_MOVING; }
}