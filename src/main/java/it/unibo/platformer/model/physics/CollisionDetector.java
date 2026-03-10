package it.unibo.platformer.model.physics;

import java.awt.Rectangle;

public class CollisionDetector {

    public boolean collision(GameObject a, GameObject b){
        Rectangle r1 = new Rectangle((int)a.getPosition().getX(),(int)a.getPosition().getY(),(int)a.getWidth(),(int)a.getHeight());

        Rectangle r2 = new Rectangle((int)b.getPosition().getX(),(int)b.getPosition().getY(),(int)b.getWidth(),(int)b.getHeight());

        return r1.intersects(r2);
    }

}