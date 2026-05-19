package it.unibo.platformer.model.physics.impl;

import java.util.List;

import it.unibo.platformer.model.physics.NoSideException;
import it.unibo.platformer.model.physics.api.CollisionResolver;

public class CollisionResolverImpl implements CollisionResolver{
    /*This class resolve all the collision cases*/

    /*This method check the collisions
    @Param List<CollisionResult> collision
    */
    @Override
    public void ResolveAll(List<CollisionResult> collisions) throws NoSideException{
        for(CollisionResult res : collisions){
            ResolveOne(res);
        }
    }

    /*This method check where is the collision
    @Param CollisionResult res
    */
    @Override
    public void ResolveOne(CollisionResult res) throws NoSideException{
        GameObjectImpl a = res.getDynamicObj();
        GameObjectImpl b = res.getStaticObj();

        switch (res.getSide()) {
            case TOP:
                a.getPosition().setY(b.getPosition().getY() - a.getHeight());
                a.getSpeed().setY(0);
                a.SetOnGround(true);
                break;
            case BOTTOM:
                a.getPosition().setY(b.getPosition().getY() + b.getHeight());
                a.getSpeed().setY(0);
                break;
            case LEFT:
                a.getPosition().setX(b.getPosition().getX() - b.getWidth());
                a.getSpeed().setX(0);
                break;
            case RIGHT:
                a.getPosition().setX(b.getPosition().getX() + b.getWidth());
                a.getSpeed().setX(0);
                break;
            case NONE:
                break;
            default:
                throw new NoSideException();
        }
    }
}
