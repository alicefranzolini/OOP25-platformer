package it.unibo.platformer.model.entities.world;
 
import it.unibo.platformer.model.entities.StaticEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
 

public class Flag extends StaticEntity {

    private static final Color FLAG_COLOR   = Color.GREEN;
    private static final double FLAG_WIDTH  = 48.0;
    private static final double FLAG_HEIGHT = 32.0;
    private static final double LOWER_SPEED = 80.0; // pixels per second
 
    private final double poleBottomY;
 
    private boolean lowering;
 
//flag for the pole 
    public Flag(final Pole pole) {
        super(
            pole.getX() + pole.getWidth(),           // just to the right of the pole
            pole.getY(),                             // starts at pole top
            FLAG_WIDTH,
            FLAG_HEIGHT
        );
        setSolid(false);                    // Mario passes through it
        this.poleBottomY = pole.getY() + pole.getHeight() - FLAG_HEIGHT;
        this.lowering = false;
    }
 
    //When Mario touches the pole it lowers.
    public void lower() {
        lowering = true;
    }
 
    public boolean isLowering() {
        return lowering;
    }
 
    public boolean isDown() {
        return getY() >= poleBottomY;
    }
 
    @Override
    public void update(double deltaTime) {
        if (!lowering || isDown()) return;
        setY(Math.min(getY() + LOWER_SPEED * deltaTime, poleBottomY));
    }
 
    @Override
    public void render(GraphicsContext gc) {

        gc.setFill(FLAG_COLOR);
        gc.fillRect(getX(), getY(), FLAG_WIDTH, FLAG_HEIGHT);
 
    }
 
    }