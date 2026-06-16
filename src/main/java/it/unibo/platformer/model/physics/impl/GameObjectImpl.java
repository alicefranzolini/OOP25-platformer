package it.unibo.platformer.model.physics.impl;

import it.unibo.platformer.model.physics.api.GameObject;

/**
 * This class implements GameObject.
 */
public final class GameObjectImpl implements GameObject {
    private VectorImpl position;
    private VectorImpl speed;
    private float width;
    private float height;
    private boolean onGround;

    /**
     * The builder for Game Object.
     * 
     * @param x the x 
     * @param y the y
     * @param width the width 
     * @param height the height
     */
    public GameObjectImpl(final float x, final float y, final float width, final float height) {
        this.position = new VectorImpl(x, y);
        this.speed = new VectorImpl(0, 0);
        this.width = width;
        this.height = height;
        this.onGround = false;
    }

    @Override
    public void setPosition(final float x, final float y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    @Override
    public void setSpeed(final float x, final float y) {
        this.speed.setX(x);
        this.speed.setY(y);
    }

    @Override
    public void setWidth(final float width) {
        this.width = width;
    }

    @Override
    public void setHeight(final float height) {
        this.height = height;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public VectorImpl getPosition() {
        return this.position;
    }

    @Override
    public VectorImpl getSpeed() {
        return this.speed;
    }

    @Override
    public String toString() {
        return "Position: " + this.position + "- Speed: " + this.speed + "- Width: " + this.width + "- Height: " + this.height;
    }

    @Override
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public boolean isOnGround() {
        return this.onGround;
    }
}
