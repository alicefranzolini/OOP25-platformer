package it.unibo.platformer.model.entities;

import it.unibo.platformer.model.physics.impl.GameObjectImpl;
import it.unibo.platformer.model.physics.api.BasicPhysics;

/** Represents an entity capable of movement and subject to physical forces.*/
public abstract class AbstractDynamicEntity extends AbstractEntity {

    /** Stores position, size and velocity of this entity. */
    private final GameObjectImpl gameObject;

    /** Applies gravity and movement to this entity. */
    private final BasicPhysics physics;

    /** If true, this entity is subject to gravity and will fall. */
    private boolean affectedByGravity;

    /** If true, the entity is resting on the ground and gravity is suspended. */
    private boolean onGround;

    /**
     * Constructs a DynamicEntity with the given position, dimensions and physics engine.
     * Gravity is enabled and the entity starts airborne by default.
     *
     * @param x       the horizontal position
     * @param y       the vertical position
     * @param width   the width of the entity
     * @param height  the height of the entity
     * @param physics the {@link BasicPhysics} engine to use for movement
     */
    public AbstractDynamicEntity(final double x, final double y, final double width, final double height, final BasicPhysics physics) {
        super(x, y, width, height);
        this.gameObject = new GameObjectImpl((float) x, (float) y, (float) width, (float) height);
        this.affectedByGravity = true;
        this.onGround = false;
        this.physics = physics;
    }

    /** {@inheritDoc} */
    @Override
    public double getX() { 
        return gameObject.getPosition().getX();
    }

    /** {@inheritDoc} */
    @Override
    public double getY() { 
        return gameObject.getPosition().getY();
    }

    /** {@inheritDoc} */
    @Override
    public double getWidth() {
        return gameObject.getWidth();
    }

    /** {@inheritDoc} */
    @Override
    public double getHeight() {
        return gameObject.getHeight();
    }
    
    /** {@inheritDoc} */
    @Override
    public void setX(final double x) {
        gameObject.getPosition().setX((float) x);
     }

    /** {@inheritDoc} */
    @Override
    public void setY(final double y) {
        gameObject.getPosition().setY((float) y);
    }

     /** {@inheritDoc} */
    @Override
    public void setWidth(final double width) {
        gameObject.setWidth((float) width);
    }

    /** {@inheritDoc} */
    @Override
    public void setHeight(final double height) {
        gameObject.setHeight((float) height);
    }
    
    /**
     * Updates the entity's position based on its velocity and gravity state.
     * If affected by gravity and airborne, delegates to the physics engine.
     * If gravity-free, integrates position manually from the current speed.
     * If on the ground, position is managed by the collision resolver.
     *
     * @param deltaTime the time elapsed since the last frame, in seconds
     */
    @Override
    public void update(final double deltaTime) {
        if (affectedByGravity && !onGround) {
            physics.UpdatePosition(gameObject, deltaTime);
        } else if (!affectedByGravity) {
            final float dx = gameObject.getSpeed().getX() * (float) deltaTime;
            final float dy = gameObject.getSpeed().getY() * (float) deltaTime;
            gameObject.getPosition().setX(gameObject.getPosition().getX() + dx);
            gameObject.getPosition().setY(gameObject.getPosition().getY() + dy);
        }
    }

    /**
     * @return the horizontal velocity of this entity
     */
    public double getVelocityX() {
        return gameObject.getSpeed().getX();
    }

    /**
     * @return the vertical velocity of this entity
     */
    public double getVelocityY() {
        return gameObject.getSpeed().getY();
    }

    /**
     * @param vx the new horizontal velocity
     */
    public void setVelocityX(final double vx) {
        gameObject.getSpeed().setX((float) vx);
    }

    /**
     * @param vy the new vertical velocity
     */
    public void setVelocityY(final double vy) {
        gameObject.getSpeed().setY((float) vy);
    }

    /**
     * @return true if this entity is subject to gravity
     */
    public boolean isAffectedByGravity() {
        return affectedByGravity;
    }

    /**
     * @return true if this entity is currently on the ground
     */
    public boolean isOnGround() {
        return onGround;
    }

    /**
     * @param g true to enable gravity for this entity, false to disable it
     */
    public void setAffectedByGravity(final boolean g) {
        this.affectedByGravity = g;
    }

    /**
     * Sets whether the entity is on the ground.
     * Resets vertical velocity to zero on landing to avoid a bounce effect.
     *
     * @param onGround true if the entity has landed, false if airborne
     */
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
        this.gameObject.setOnGround(onGround);
        if (onGround) {
            gameObject.getSpeed().setY(0);
        }
    }

    /** {@inheritDoc} */
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * @return the underlying {@link GameObjectImpl} used by the physics engine
     */
    public GameObjectImpl getGameObject() {
        return gameObject;
    }
}
