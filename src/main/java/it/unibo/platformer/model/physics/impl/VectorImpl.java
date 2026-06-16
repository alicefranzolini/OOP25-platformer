package it.unibo.platformer.model.physics.impl;

import it.unibo.platformer.model.physics.api.Vector;

/**
 * The implementation of Vector.
 */
public final class VectorImpl implements Vector {

    private float x;
    private float y;

    /**
     * The empty costructor.
     */
    public VectorImpl() {
    }

    /**
     * The costructor.
     * 
     * @param x the x
     * @param y the y
     */
    public VectorImpl(final float x, final float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setX(final float x) {
        this.x = x;
    }

    @Override
    public void setY(final float y) {
        this.y = y;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public void add(final VectorImpl v) {
        this.x += v.getX();
        this.y += v.getY();
    }

    @Override
    public void sub(final VectorImpl v) {
        this.x -= v.getX();
        this.y -= v.getY();
    }

    @Override
    public void scale(final float num) { 
        this.x = this.x * num;
        this.y = this.y * num;
    }

    /**
     * Create a new VectorImpl equal to the original.
     * 
     *  @return VectorImpl
     */
    public VectorImpl clone() {
        return new VectorImpl(this.x, this.y);
    }

    @Override
    public String toString() {
        return "Coord. X = " + this.x + " - Coord. Y = " + this.y;
    }

}
