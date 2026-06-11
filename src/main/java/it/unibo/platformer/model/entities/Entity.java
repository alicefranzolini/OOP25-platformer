package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;
import java.util.List;
import java.util.stream.Collectors;

/** Defines the position and size of any object in the game world. */
public abstract class Entity {

    private double x;
    private double y;
    private double width;
    private double height;

    /** True while the entity is alive; false means it will be removed next frame. */
    private boolean active;

    /**
     * Constructs an Entity with the given position and dimensions.
     *
     * @param x      the horizontal position
     * @param y      the vertical position
     * @param width  the width of the entity
     * @param height the height of the entity
     */
    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

    /**
     * Internal BoundingBox to handle AABB collisions.
     * Simplifies the calculation of overlap between rectangles.
     */
    public static final class BoundingBox {

        private final double x;
        private final double y;
        private final double width;
        private final double height;

        /**
         * Constructs a BoundingBox with the given position and dimensions.
         *
         * @param x      the horizontal position
         * @param y      the vertical position
         * @param width  the width of the box
         * @param height the height of the box
         */
        public BoundingBox(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /** @return the horizontal position of this box */
        public double getX()      { return x; }

        /** @return the vertical position of this box */
        public double getY()      { return y; }

        /** @return the width of this box */
        public double getWidth()  { return width; }

        /** @return the height of this box */
        public double getHeight() { return height; }

        /**
         * Core method for detecting collisions between entities.
         *
         * @param other the other BoundingBox to test against
         * @return true if this box overlaps with {@code other}
         */
        public boolean overlaps(BoundingBox other) {
            return x < other.x + other.width
                && x + width  > other.x
                && y < other.y + other.height
                && y + height > other.y;
        }
    }

    /**
     * Returns the bounding box of this entity for collision detection.
     *
     * @return the {@link BoundingBox} of this entity
     */
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, y, width, height);
    }

    /**
     * Updates the entity's logic for the current frame.
     *
     * @param deltaTime the time elapsed since the last frame, in seconds
     */
    public abstract void update(double deltaTime);

    /**
     * Draws this entity onto the given graphics context.
     *
     * @param gc the {@link GraphicsContext} to render onto
     */
    public abstract void render(GraphicsContext gc);

    /** @return the horizontal position of this entity */
    public double getX()      { return x; }

    /** @return the vertical position of this entity */
    public double getY()      { return y; }

    /** @return the width of this entity */
    public double getWidth()  { return width; }

    /** @return the height of this entity */
    public double getHeight() { return height; }

    /** @return true if this entity is still active */
    public boolean isActive() { return active; }

    /** @param x the new horizontal position */
    public void setX(double x)            { this.x = x; }

    /** @param y the new vertical position */
    public void setY(double y)            { this.y = y; }

    /** @param width the new width */
    public void setWidth(double width)    { this.width = width; }

    /** @param height the new height */
    public void setHeight(double height)  { this.height = height; }

    /** @param active the new active state */
    public void setActive(boolean active) { this.active = active; }

    /** Marks this entity for removal; it will be filtered out on the next frame. */
    public void destroy() {
        this.active = false;
    }

    /**
     * Filters a list of entities, retaining only those that are still active.
     *
     * @param entities the list to filter
     * @return a new list containing only the active entities
     */
    public static List<Entity> filterActive(List<Entity> entities) {
        return entities.stream()
                .filter(Entity::isActive)
                .collect(Collectors.toList());
    }
}