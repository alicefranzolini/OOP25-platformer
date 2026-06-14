package it.unibo.platformer.model.entities.world;

import it.unibo.platformer.model.entities.AbstractStaticEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents the flag at the top of the goal pole.
 * When Mario touches the pole, the flag lowers to the bottom.
 */
public final class Flag extends AbstractStaticEntity {

    private static final Color FLAG_COLOR = Color.GREEN;
    private static final double FLAG_WIDTH = 48.0;
    private static final double FLAG_HEIGHT = 32.0;
    private static final double LOWER_SPEED = 80.0; // pixels per second

    private final double poleBottomY;
    private boolean lowering;

    /**
     * Creates a flag attached to the given pole.
     * The flag starts at the top of the pole and is non-solid.
     *
     * @param pole the pole this flag belongs to
     */
    public Flag(final Pole pole) {
        super(
            pole.getX() + pole.getWidth(),  // just to the right of the pole
            pole.getY(),                    // starts at pole top
            FLAG_WIDTH,
            FLAG_HEIGHT
        );
        setSolid(false); // Mario passes through it
        this.poleBottomY = pole.getY() + pole.getHeight() - FLAG_HEIGHT;
    }

    /**
     * Triggers the lowering animation when Mario touches the pole.
     */
    public void lower() {
        lowering = true;
    }

    /**
     * Returns whether the flag is currently lowering.
     *
     * @return true if the flag is in the process of lowering
     */
    public boolean isLowering() {
        return lowering;
    }

    /**
     * Returns whether the flag has reached the bottom of the pole.
     *
     * @return true if the flag is fully lowered
     */
    public boolean isDown() {
        return getY() >= poleBottomY;
    }

    @Override
    public void update(final double deltaTime) {
        if (!lowering || isDown()) {
            return;
        }
        setY(Math.min(getY() + LOWER_SPEED * deltaTime, poleBottomY));
    }

    @Override
    public void render(final GraphicsContext gc) {
        gc.setFill(FLAG_COLOR);
        gc.fillRect(getX(), getY(), FLAG_WIDTH, FLAG_HEIGHT);
    }
}
