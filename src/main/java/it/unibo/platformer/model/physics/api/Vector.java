package it.unibo.platformer.model.physics.api;

import it.unibo.platformer.model.physics.impl.VectorImpl;

/**
 * Create the vectors for the physics.
*/

public interface Vector {

    /**
     * Set the x.
     * 
     * @param x
    */
    void setX(float x);

    /**
     * Set the y.
     * 
     * @param y
    */
    void setY(float y);

    /**
     * get the x.
     * 
     * @return x
    */
    float getX();

    /**
     * get the y.
     * 
     * @return y
    */
    float getY();

    /**
     * add a vector to an other vector.
     * 
     * @param v
    */
    void add(VectorImpl v);

    /**
     * sub a vector to an other vector.
     * 
     * @param v
    */
    void sub(VectorImpl v);

    /**
     * multiply a vector for a number.
     * 
     * @param num
    */
    void scale(float num);

}
