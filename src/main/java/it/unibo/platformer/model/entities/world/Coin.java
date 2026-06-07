package it.unibo.platformer.model.entities.world;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Coin extends DynamicEntity {

    // rotation animation: changing width to simulate rotation
   private static final double FRAME_DURATION = 0.15;
    private static final int    TOTAL_FRAMES   = 4;
    private static final double POP_VELOCITY   = -300.0;
    private static final double POP_GRAVITY    = 600.0;
 
    /** Horizontal scale per frame, simulating a rotation effect. */
    private static final double[] FRAME_SCALE_X = { 1.0, 0.6, 0.15, 0.6 };
 
    private double animTimer;
    private int    animFrame;
 
    private boolean isPopping;
    private double  popStartY;
 
    private Image coinSprite;
 
    public Coin(double x, double y, BasicPhysics physics) {
        super(x, y, 16, 16, physics);
        setAffectedByGravity(false);
        this.animTimer  = 0;
        this.animFrame  = 0;
        this.isPopping  = false;
        loadSprite();
    }
 
    private void loadSprite() {
        coinSprite = AnimationManager.loadImage("/sprites/coin.png");
    }
 
    public static Coin createPopping(double x, double y, BasicPhysics physics) {
        final Coin coin = new Coin(x, y, physics);
        coin.isPopping        = true;
        coin.popStartY        = y;
        coin.setAffectedByGravity(false);
        coin.setVelocityY(POP_VELOCITY);
        return coin;
    }
 
    @Override
    public void update(final double deltaTime) {
        animTimer += deltaTime;
        if (animTimer >= FRAME_DURATION) {
            animTimer = 0;
            animFrame = (animFrame + 1) % TOTAL_FRAMES;
        }
 
        if (isPopping) {
            setY(getY() + getVelocityY() * deltaTime);
            setVelocityY(getVelocityY() + POP_GRAVITY * deltaTime);
            if (getY() >= popStartY) {
                destroy();
            }
        }
    }
 
    @Override
    public void render(final GraphicsContext gc) {
        if (!isActive()) return;
 
        final double scale   = FRAME_SCALE_X[animFrame];
        final double drawW   = getWidth() * scale;
        final double offsetX = (getWidth() - drawW) / 2.0;
        final double cx      = getX() + offsetX;
        final double cy      = getY();
 
        if (coinSprite != null) {
            gc.drawImage(coinSprite, cx, cy, drawW, getHeight());
        } else {
            gc.setFill(Color.GOLD);
            gc.fillOval(cx, cy, drawW, getHeight());
            gc.setStroke(Color.DARKORANGE);
            gc.strokeOval(cx, cy, drawW, getHeight());
        }
    }
 
    public boolean isPopping() { return isPopping; }
}
