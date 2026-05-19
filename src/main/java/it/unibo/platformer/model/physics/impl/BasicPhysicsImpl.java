package it.unibo.platformer.model.physics.impl;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.api.GameObject;

public class BasicPhysicsImpl implements BasicPhysics{
    /*This class create the basic physics of the game*/
    private static final float GRAVITY = 800.0f;

    @Override
    public void update(GameObject o, float DeltaTime){
        /*Update the speed*/
        if(!o.IsOnGround()){
        o.getSpeed().setY(o.getSpeed().getY() + (GRAVITY * DeltaTime));
        }else{
        /*Update the position*/
        o.getPosition().setX(o.getPosition().getX() + o.getSpeed().getX() * (float)DeltaTime);
        o.getPosition().setY(o.getPosition().getY() + o.getSpeed().getY() * (float)DeltaTime);
        }
    }
}