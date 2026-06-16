package it.unibo.platformer.model.physics.api;

import it.unibo.platformer.model.physics.impl.VectorImpl;

/**
 * This class create the objects of the game.
 */
public interface GameObject {

    /**
     * set the position.
     * 
     * @param x the x
     * @param y the y
     */
    void setPosition(float x, float y);

    /** 
     * set the speed.
     * 
     * @param x the x
     * @param y the y
     */
    void setSpeed(float x, float y);

    /** 
     * set the width.
     * 
     * @param width the width
     */
    void setWidth(float width);

    /**
     * set the height.
     * 
     * @param height the height
     */
    void setHeight(float height);

    /** 
     * get the width.
     * 
     * @return width
     */
    float getWidth();

    /**
     * set the height.
     * 
     * @return height
     */
    float getHeight();

    /**
     * get the position.
     * 
     * @return VectorImpl
     */
    VectorImpl getPosition();

    /** 
     * get the speed.
     * 
     * @return VectorImpl
     */
    VectorImpl getSpeed();

    /** 
     * return if the object is on ground.
     * 
     * @return boolean 
     */
    boolean isOnGround();

    /** 
     * set the object on ground.
     * 
     * @param onGround if the object is on ground
     */
    void setOnGround(boolean onGround);

}
