package it.unibo.platformer.model.entities.enemies;
 
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
 

public class Goomba extends EnemyImpl {
 
    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------
 
    private static final double WALK_SPEED     = 60.0;
    private static final double SQUISH_TIME    = 0.4;
    private static final double FRAME_DURATION = 0.2;
 
    // -------------------------------------------------------------------------
    // Public state enum
    // -------------------------------------------------------------------------
 
    public enum GoombaState { WALK, SQUISHED }
 
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
                renderFallback(e, gc);
            }
        }
 
        private void renderFallback(EnemyImpl e, GraphicsContext gc) {
            gc.setFill(Color.SADDLEBROWN);
            gc.fillRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());
            gc.setFill(Color.WHITE);
            gc.fillOval(e.getX() + 4,  e.getY() + 8, 8, 8);
            gc.fillOval(e.getX() + 20, e.getY() + 8, 8, 8);
            gc.setFill(Color.BLACK);
            gc.fillOval(e.getX() + 6,  e.getY() + 10, 4, 4);
            gc.fillOval(e.getX() + 22, e.getY() + 10, 4, 4);
        }
 
        @Override
        public boolean hitsPlayer() { return true; }
    }
 
    private static final class SquishHandler implements EnemyImpl.EnemyStateHandler {
        private double squishTimer = 0;
 
        @Override
        public void update(EnemyImpl e, double deltaTime) {
            e.anim.play("squished");
            e.anim.update(deltaTime);
            squishTimer += deltaTime;
            if (squishTimer >= SQUISH_TIME) {
                e.destroy();
            }
        }
 
        @Override
        public void render(EnemyImpl e, GraphicsContext gc) {
            double halfH = e.getHeight() / 2.0;
            if (e.anim.hasAnimation("squished")) {
                e.anim.render(gc, e.getX(), e.getY() + halfH, e.getWidth(), halfH, e.facingLeft);
            } else {
                gc.setFill(Color.SADDLEBROWN);
                gc.fillRect(e.getX(), e.getY() + halfH, e.getWidth(), halfH);
            }
        }
 
        @Override
        public boolean hitsPlayer() { return false; }
    }
 
    // -------------------------------------------------------------------------
    // State tracking
    // -------------------------------------------------------------------------
 
    private GoombaState state;
 
    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------
 
    public Goomba(double x, double y, BasicPhysics physics) {
        super(x, y, 32, 32, physics);
        init();
    }
 
    
    private void init() {
        transitionTo(GoombaState.WALK);
        setVelocityX(-WALK_SPEED);
        anim.play("walk");
    }
 
    // -------------------------------------------------------------------------
    // Template Method implementation
    // -------------------------------------------------------------------------
 
    @Override
    protected void loadAnimations() {
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
    // State transition
    // -------------------------------------------------------------------------
 
    private void transitionTo(GoombaState newState) {
        this.state = newState;
        transitionTo(newState == GoombaState.WALK
            ? new WalkHandler()
            : new SquishHandler());
    }
 
    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------
 
    public void squish() {
        if (state != GoombaState.WALK) return;
        transitionTo(GoombaState.SQUISHED);
        setVelocityX(0);
        affectedByGravity = false;
    }
 
    public GoombaState getState() { return state; }
}