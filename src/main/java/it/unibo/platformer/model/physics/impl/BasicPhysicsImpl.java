package it.unibo.platformer.model.physics.impl;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.api.GameObject;

/**
 * Implementation of the basic physics.
 */
public final class BasicPhysicsImpl implements BasicPhysics{

    private static final float GRAVITY = 800.0f;

    @Override
    public void updatePosition(final GameObject o, final double deltaTime){
        /*Update the speed*/
        if(!o.isOnGround()){
        /*If the object isn't on the ground, it fall */
        o.getSpeed().setY(o.getSpeed().getY() + (GRAVITY * (float) deltaTime));
        }
        /*Update the position*/
        o.getPosition().setX(o.getPosition().getX() + o.getSpeed().getX() * (float)deltaTime);
        o.getPosition().setY(o.getPosition().getY() + o.getSpeed().getY() * (float)deltaTime);
    }
}