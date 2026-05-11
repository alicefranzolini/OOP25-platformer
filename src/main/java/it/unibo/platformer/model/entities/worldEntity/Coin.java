package it.unibo.platformer.model.entities.worldEntity;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Coin extends DynamicEntity {

    // Animazione rotazione: 4 frame simulati scalando la larghezza
    private double animTimer;
    private int animFrame;
    private static final double FRAME_DURATION = 0.15;
    private static final int TOTAL_FRAMES = 4;

    // Fattori di scala X per simulare la rotazione della moneta
    // frame 0: piena, frame 1: stretta, frame 2: sottilissima, frame 3: stretta
    private static final double[] FRAME_SCALE_X = { 1.0, 0.6, 0.15, 0.6 };

    private boolean isPopping;
    private double popStartY;
    private static final double POP_VELOCITY = -300.0;

    // Sprite singolo della moneta
    private Image coinSprite;

    public Coin(double x, double y) {
        super(x, y, 16, 16);
        this.affectedByGravity = false;
        this.animTimer = 0;
        this.animFrame = 0;
        this.isPopping = false;
        loadSprite();
    }

    private void loadSprite() {
        coinSprite = AnimationManager.loadImage("/sprites/coin.png");
    }

    public static Coin createPopping(double x, double y) {
        Coin coin = new Coin(x, y);
        coin.isPopping = true;
        coin.popStartY = y;
        coin.setVelocityY(POP_VELOCITY);
        coin.affectedByGravity = false;
        return coin;
    }

    @Override
    public void update(double deltaTime) {
        // aggiorna frame animazione rotazione
        animTimer += deltaTime;
        if (animTimer >= FRAME_DURATION) {
            animTimer = 0;
            animFrame = (animFrame + 1) % TOTAL_FRAMES;
        }

        // movimento pop con gravità manuale
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
            // fallback ovale giallo
            gc.setFill(Color.GOLD);
            gc.fillOval(x + offsetX, y, drawW, height);
            gc.setStroke(Color.DARKORANGE);
            gc.strokeOval(x + offsetX, y, drawW, height);
        }
    }

    public boolean isPopping() { return isPopping; }
}