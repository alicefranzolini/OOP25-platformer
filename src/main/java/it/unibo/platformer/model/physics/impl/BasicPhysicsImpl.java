package it.unibo.platformer.model.physics.impl;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.api.GameObject;

public class BasicPhysicsImpl implements BasicPhysics{
    /*This class create the basic physics of the game*/
    private static final float GRAVITY = 0.9f;

    @Override
    public void update(GameObject o){
        /*Update the speed*/
        o.getSpeed().setY(o.getSpeed().getY() + GRAVITY);

        /*Update the position*/
        o.getPosition().add(o.getSpeed());
    }
}