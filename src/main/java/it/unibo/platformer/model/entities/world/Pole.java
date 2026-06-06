package it.unibo.platformer.model.entities.world;
 
import it.unibo.platformer.model.entities.StaticEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
 
public class Pole extends StaticEntity {
 
    private static final Color POLE_COLOR = Color.SILVER;
    private static final Color BALL_COLOR = Color.GOLD;
    private static final double POLE_WIDTH = 6.0;
    private static final double BALL_RADIUS = 8.0;
 
    public Pole(double x, double y, double height) {
        super(x, y, POLE_WIDTH, height);
    }
 
    @Override
    public void render(GraphicsContext gc) {
        // Pole
        gc.setFill(POLE_COLOR);
        gc.fillRect(x, y, POLE_WIDTH, height);
 
        // Golden ball on top
        gc.setFill(BALL_COLOR);
        gc.fillOval(
            x + POLE_WIDTH / 2.0 - BALL_RADIUS,
            y - BALL_RADIUS,
            BALL_RADIUS * 2,
            BALL_RADIUS * 2
        );
    }
}
 