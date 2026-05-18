package it.unibo.platformer.model.physics.api;

import it.unibo.platformer.model.physics.impl.VectorImpl;

public interface Vector {

    public void setX(float x);

    public void setY(float y);

    public float getX();

    public float getY();

    public void add(VectorImpl v);

    public void sub(VectorImpl v);

    public void scale(float num);

}
