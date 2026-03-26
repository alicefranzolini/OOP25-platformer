package it.unibo.platformer.model.entities.enemies;
 
import it.unibo.platformer.model.entities.DynamicEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
 

public class Goomba extends DynamicEntity {
 
    private static final double SPEED = 60.0;
    private static final double SQUISH_TIME  = 0.4; // how long the squished sprite stays before disappearing
 
    public enum GoombaState { 
        WALK, 
        SQUISHED, 
        DEAD 
    }
    private GoombaState state;
    private double squishTimer;
 
    // walking animations
    private double animTimer;
    private int animFrame;
    private static final double FRAME_DURATION = 0.2;
    private static final int FRAMES = 2;
 
    public Goomba(double x, double y) {
        super(x, y, 32, 32);
        this.state = GoombaState.WALK;
         setVelocityX(-SPEED);  //start walking left
        this.squishTimer = 0;
        this.animTimer = 0;
        this.animFrame = 0;
    }
 
    @Override
    public void update(double deltaTime) {
        switch (state) {
 
            case WALK:
                // update animations
                animTimer += deltaTime;
                if (animTimer >= FRAME_DURATION) {
                    animTimer = 0;
                    animFrame = (animFrame + 1) % FRAMES;
                }
                super.update(deltaTime); // update positions
                break;
 
            case SQUISHED:
                // stay flattened then disappear
                squishTimer += deltaTime;
                if (squishTimer >= SQUISH_TIME) {
                    destroy();
                }
                break;
 
            case DEAD:
                destroy();
                break;
        }
    }
 
    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;
 
        if (state == GoombaState.SQUISHED) {
            gc.setFill(Color.SADDLEBROWN);
            gc.fillRect(x, y + height / 2, width, height / 2);//squished goomba
            return;
        }
 
        // Corpo
        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(x, y, width, height);
 
        // Piedi (alternano frame)
        gc.setFill(Color.BROWN);
        if (animFrame == 0) {
            gc.fillRect(x, y + height - 8, 10, 8); // piede sinistro
        } else {
            gc.fillRect(x + width - 10, y + height - 8, 10, 8); // piede destro
        }
 
        // Occhi
        gc.setFill(Color.WHITE);
        gc.fillOval(x + 4,  y + 8, 8, 8);
        gc.fillOval(x + 20, y + 8, 8, 8);
        gc.setFill(Color.BLACK);
        gc.fillOval(x + 6,  y + 10, 4, 4);
        gc.fillOval(x + 22, y + 10, 4, 4);
    }
 
   
    public void squish() {
        if (state != GoombaState.WALK) return;
        state = GoombaState.SQUISHED;
        setVelocityX(0);
        affectedByGravity = false;
    }
 
    //if goomba touch Mario it damages him
    public boolean hitsPlayer() {
        return state == GoombaState.WALK;
    }
 
    public GoombaState getState() { return state; }
}