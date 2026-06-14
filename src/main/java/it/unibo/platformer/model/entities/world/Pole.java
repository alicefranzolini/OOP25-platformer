package it.unibo.platformer.model.entities.world;

import it.unibo.platformer.model.entities.AbstractStaticEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents the goal pole at the end of a level.
 * It is rendered as a silver vertical pole with a golden ball on top.
 */
public final class Pole extends AbstractStaticEntity {

    private static final Color POLE_COLOR = Color.SILVER;
    private static final Color BALL_COLOR = Color.GOLD;
    private static final double POLE_WIDTH = 6.0;
    private static final double BALL_RADIUS = 8.0;
    private static final double BALL_DIAMETER = BALL_RADIUS * 2;

    /**
     * Creates a new Pole at the specified position with the given height.
     *
     * @param x      the x coordinate of the pole
     * @param y      the y coordinate of the pole's top
     * @param height the height of the pole
     */
    public Pole(final double x, final double y, final double height) {
        super(x, y, POLE_WIDTH, height);
    }
    
 /** Draw the pole */
    @Override
    public void render(final GraphicsContext gc) {
       
        gc.setFill(POLE_COLOR);
        gc.fillRect(getX(), getY(), POLE_WIDTH, getHeight());

        /** Draw the golden ball on top */
        gc.setFill(BALL_COLOR);
        gc.fillOval(
            getX() + POLE_WIDTH / 2.0 - BALL_RADIUS,
            getY() - BALL_RADIUS,
            BALL_DIAMETER,
            BALL_DIAMETER
        );
    }
}
