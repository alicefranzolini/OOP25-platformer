package it.unibo.platformer.model.entities.worldEntity;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Coin extends DynamicEntity {

    // rotation animation: changing width to simulate rotation
    private double animTimer;
    private int animFrame;
    private static final double FRAME_DURATION = 0.15;
    private static final int TOTAL_FRAMES = 4;

    private static final double[] FRAME_SCALE_X = { 1.0, 0.6, 0.15, 0.6 };

    private boolean isPopping;
    private double popStartY;
    private static final double POP_VELOCITY = -300.0;


    private Image coinSprite;

    public Coin(double x, double y, BasicPhysics physics) {
        super(x, y, 16, 16, physics);
        this.affectedByGravity = false;
        this.animTimer = 0;
        this.animFrame = 0;
        this.isPopping = false;
        loadSprite();
    }

    private void loadSprite() {
        coinSprite = AnimationManager.loadImage("/sprites/coin.png");
    }

    //Factory method to create a coin that pops out of a block.
    public static Coin createPopping(double x, double y, BasicPhysics physics) {
        Coin coin = new Coin(x, y, physics );
        coin.isPopping = true;
        coin.popStartY = y;
        coin.setVelocityY(POP_VELOCITY);
        coin.affectedByGravity = false;
        return coin;
    }

    @Override
    public void update(double deltaTime) {
        
        animTimer += deltaTime;
        if (animTimer >= FRAME_DURATION) {
            animTimer = 0;
            animFrame = (animFrame + 1) % TOTAL_FRAMES;
        }

        
        if (isPopping) {
            setY(getY() + getVelocityY() * deltaTime);
            setVelocityY(getVelocityY() + 600 * deltaTime);
            if (getY() >= popStartY) {
                destroy();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;

        double scale = FRAME_SCALE_X[animFrame];
        double drawW = width * scale;
        double offsetX = (width - drawW) / 2.0;

        if (coinSprite != null) {
            gc.drawImage(coinSprite, x + offsetX, y, drawW, height);
        } else {
            // if the sprite fails to load, fallback to a simple golden oval
            gc.setFill(Color.GOLD);
            gc.fillOval(x + offsetX, y, drawW, height);
            gc.setStroke(Color.DARKORANGE);
            gc.strokeOval(x + offsetX, y, drawW, height);
        }
    }

    public boolean isPopping() { return isPopping; }
}