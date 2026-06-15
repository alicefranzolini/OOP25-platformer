package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;
import java.util.List;
import java.util.stream.Collectors;

/** 
 * Defines the position and size of any object in the game world. 
 */
public abstract class AbstractEntity {

    private double x;
    private double y;
    private double width;
    private double height;

    /** 
     * True while the entity is alive; false means it will be removed next frame. 
     */
    private boolean active;

    /**
     * Constructs an Entity with the given position and dimensions.
     *
     * @param x      the horizontal position
     * @param y      the vertical position
     * @param width  the width of the entity
     * @param height the height of the entity
     */
    public AbstractEntity(final double x, final double y, final double width, final double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
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

    /** 
     * Returns the horizontal position of this entity.
     * 
     * @return the horizontal position of this entity 
     */
    public double getX() {
        return x;
    }

    /** 
     * Returns the vertical position of this entity.
     * 
     * @return the vertical position of this entity 
     */
    public double getY() {
        return y;
    }

    /** 
     * Returns the width of this entity.
     * 
     * @return the width of this entity 
     */
    public double getWidth() {
        return width;
    }

    /** 
     * Returns the height of this entity.
     * 
     * @return the height of this entity 
     */
    public double getHeight() {
        return height;
    }

    /** 
     * Returns whether this entity is still active.
     * 
     * @return true if this entity is still active
     */
    public boolean isActive() {
        return active;
    }

    /** 
     * Sets the horizontal position of this entity.
     * 
     * @param x the new horizontal position 
     */
    public void setX(final double x) {
        this.x = x;
    }

    /** 
     * Sets the vertical position of this entity.
     * 
     * @param y the new vertical position 
     */
    public void setY(final double y) {
        this.y = y;
    }

    /** 
     * Sets the width of this entity.
     * 
     * @param width the new width 
     */
    public void setWidth(final double width) {
        this.width = width;
    }

    /** 
     * Sets the height of this entity.
     * 
     * @param height the new height 
     */
    public void setHeight(final double height) {
        this.height = height;
    }

    /** 
     * Sets the active state of this entity.
     * 
     * @param active the new active state 
     */
    public void setActive(final boolean active) {
        this.active = active;
    }

    /** Marks this entity for removal; it will be filtered out on the next frame. */
    public void destroy() {
        this.active = false;
    }

    /**
     * Filters a list of entities, retaining only those that are still active.
     *
     * @param entities the list to filter
     * 
     * @return a new list containing only the active entities
     */
    public static List<AbstractEntity> filterActive(final List<AbstractEntity> entities) {
        return entities.stream()
                .filter(AbstractEntity::isActive)
                .collect(Collectors.toList());
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
        public BoundingBox(final double x, final double y, final double width, final double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        /** 
         * Returns the horizontal position of this box.
         * 
         * @return the horizontal position of this box 
         */
        public double getX() {
            return x;
        }

        /** 
         * Returns the vertical position of this box.
         * 
         * @return the vertical position of this box
         */
        public double getY() {
            return y;
        }

        /** 
         * Returns the width of this box.
         * 
         *  @return the width of this box
         */
        public double getWidth() {
            return width;
        }

        /** 
         * Returns the height of this box.
         * 
         * @return the height of this box
         */
        public double getHeight() {
            return height;
        }

        /**
         * Core method for detecting collisions between entities.
         *
         * @param other the other BoundingBox to test against
         * 
         * @return true if this box overlaps with {@code other}
         */
        public boolean overlaps(final BoundingBox other) {
            return x < other.x + other.width
                && x + width > other.x
                && y < other.y + other.height
                && y + height > other.y;
        }

    }

}
