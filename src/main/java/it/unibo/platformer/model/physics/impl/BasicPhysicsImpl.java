package it.unibo.platformer.model.physics.impl;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.api.GameObject;

public class BasicPhysicsImpl implements BasicPhysics{
    /*This class create the basic physics of the game*/
    private static final float GRAVITY = 800.0f;

    @Override
    public void updatePosition(GameObject o, double DeltaTime){
        /*Update the speed*/
        if(!o.IsOnGround()){
        /*If the object isn't on the ground, it fall */
        o.getSpeed().setY(o.getSpeed().getY() + (GRAVITY * (float) DeltaTime));
        }
        /*Update the position*/
        o.getPosition().setX(o.getPosition().getX() + o.getSpeed().getX() * (float)DeltaTime);
        o.getPosition().setY(o.getPosition().getY() + o.getSpeed().getY() * (float)DeltaTime);
    }
}