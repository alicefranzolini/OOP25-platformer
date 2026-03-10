package it.unibo.platformer.model.physics;

public class BasicPhysics {
    private static final float GRAVITY = 0.9f;

    public void update(GameObject o){
        /*Update the speed*/
        o.getSpeed().setY(o.getSpeed().getY() + GRAVITY);

        /*Update the position*/
        o.getPosition().add(o.getSpeed());
    }
}