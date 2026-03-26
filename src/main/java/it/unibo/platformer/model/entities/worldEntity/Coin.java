package it.unibo.platformer.model.entities.worldEntity;

import it.unibo.platformer.model.entities.DynamicEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Coin extends DynamicEntity {

  //for the rotations, cycling of 4 frames
    private double animTimer;
    private int animFrame;
    private static final double FRAME_DURATION = 0.15;
    private static final int TOTAL_FRAMES = 4;

    
    private boolean isPopping;       
    private double popStartY;        
    private static final double POP_VELOCITY = -300.0;

    public Coin(double x, double y) {
        super(x, y, 16, 16);
        this.affectedByGravity = false; // La moneta statica non cade
        this.animTimer = 0;
        this.animFrame = 0;
        this.isPopping = false;
    }

   //creations of the popping coin
    public static Coin createPopping(double x, double y) {
        Coin coin = new Coin(x, y);
        coin.isPopping = true;
        coin.popStartY = y;
        coin.setVelocityY(POP_VELOCITY);
        coin.affectedByGravity = false; // Gestiamo noi il movimento del pop
        return coin;
    }

    @Override
    public void update(double deltaTime) {
        // rotation amation frame by frame
        animTimer += deltaTime;
        if (animTimer >= FRAME_DURATION) {
            animTimer = 0;
            animFrame = (animFrame + 1) % TOTAL_FRAMES;
        }

        //manual gravity because it follows its own path
        if (isPopping) {
           // Move vertically
            setY(getY() + getVelocityY() * deltaTime);

        // Apply manual gravity
            setVelocityY(getVelocityY() + 600 * deltaTime);

        // When the coin returns to its starting height, remove it
            if (getY() >= popStartY) {
                destroy();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;

        //different width of tre frame for the different sprite
        double[] frameWidths = { 16, 10, 4, 10 };
        double frameWidth = frameWidths[animFrame];
        double offsetX = (16 - frameWidth) / 2;

        // change with sprite pixel-art
        gc.setFill(Color.GOLD);
        gc.fillOval(x + offsetX, y, frameWidth, height);
        gc.setStroke(Color.DARKORANGE);
        gc.strokeOval(x + offsetX, y, frameWidth, height);
    }

    public boolean isPopping() { return isPopping; }
}