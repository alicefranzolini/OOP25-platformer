package it.unibo.platformer.model.physics.api;

import it.unibo.platformer.model.physics.impl.VectorImpl;

public interface GameObject {

    public void setPosition(float x, float y);

    public void setSpeed(float x, float y);

    public void setWidth(float width);

    public void setHeight(float height);

    public float getWidth();

    public float getHeight();

    public VectorImpl getPosition();

    public VectorImpl getSpeed();

    public boolean IsOnGround();

    public void setOnGround(boolean onGround);
}