package it.unibo.platformer.model.physics.impl;

import java.awt.Rectangle;
import java.lang.Math;

import it.unibo.platformer.model.physics.api.CollisionDetector;
import it.unibo.platformer.model.physics.api.GameObject;

public class CollisionDetectorImpl implements CollisionDetector{

    @Override
    public boolean collision(GameObject a, GameObject b){
        Rectangle r1 = new Rectangle((int)a.getPosition().getX(),(int)a.getPosition().getY(),(int)a.getWidth(),(int)a.getHeight());

        Rectangle r2 = new Rectangle((int)b.getPosition().getX(),(int)b.getPosition().getY(),(int)b.getWidth(),(int)b.getHeight());

        return r1.intersects(r2);
    }

    /*The method check the side of the collision
    @param DynamicEntity
    @param StaticEntity
    @return CollisionResult
    */
    @Override
    public CollisionResult getCollisionResult(GameObjectImpl a, GameObjectImpl b){
        if(!collision(a, b)){
            return null;
        }else{
            float centerAX = a.getPosition().getX() + a.getWidth() / 2;
            float centerAY = a.getPosition().getY() + a.getHeight() / 2;
            float centerBX = b.getPosition().getX() + b.getWidth() / 2;
            float centerBY = b.getPosition().getY() + b.getHeight() /2;

            float distanceA = centerAX - centerBX;
            float distanceB = centerAY - centerBY;

            float combineWidhts = (a.getWidth() + b.getWidth()) / 2;
            float combineHeights = (a.getHeight() + b.getHeight()) / 2;

            float insideX = combineWidhts - Math.abs(distanceA);
            float insideY = combineHeights - Math.abs(distanceB);

            if(insideX < insideY){
                if(distanceA > 0){
                    return new CollisionResult(a, b, CollisionSide.RIGHT);
                }else{
                    return new CollisionResult(a, b, CollisionSide.LEFT);
                }
            }else{
                if(distanceB > 0){
                    return new CollisionResult(a, b, CollisionSide.BOTTOM);
                }else{
                    return new CollisionResult(a, b, CollisionSide.TOP);
                }
            }
        }
    }


}