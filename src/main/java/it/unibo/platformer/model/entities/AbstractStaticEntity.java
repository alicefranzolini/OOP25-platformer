package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;

/**
 * Represents an entity that does not possess its own physical dynamics
 * (no speed or gravity).
 */
public abstract class AbstractStaticEntity extends AbstractEntity {

    /**
     * Indicates whether this entity blocks movement.
     * If true, other entities cannot pass through it; defaults to true.
     */
    private boolean solid;

    /**
     * Constructs a StaticEntity with the given position and dimensions.
     * The entity is solid by default.
     *
     * @param x      the horizontal position
     * @param y      the vertical position
     * @param width  the width of the entity
     * @param height the height of the entity
     */
    public AbstractStaticEntity(final double x, final double y, final double width, final double height) {
        super(x, y, width, height);
        this.solid = true;
    }

    /**
     * Does nothing by default, since static entities do not move or animate.
     *
     * @param deltaTime the time elapsed since the last frame, in seconds
     */
    @Override
    public void update(final double deltaTime) {
    }

    /**
     * @return true if this entity blocks movement
     */
    public boolean isSolid() {
        return solid;
    }

    /**
     * @param solid true to make this entity block movement, false to allow passing through
     */
    public void setSolid(final boolean solid) {
        this.solid = solid;
    }

    /**
     * Draws this static entity onto the given graphics context.
     * Subclasses should override this method to provide a visual representation.
     *
     * @param gc the {@link GraphicsContext} to render onto
     */
    @Override
    public void render(final GraphicsContext gc) {
    }
}
