package it.unibo.platformer.model.physics;

import java.util.List;

public class CollisionResolver {

    public void ResolveAll(List<CollisionResult> collisions) throws NoSideException{
        for(CollisionResult res : collisions){
            ResolveOne(res);
        }
    }

    public void ResolveOne(CollisionResult res) throws NoSideException{
        GameObject a = res.getDynamicObj();
        GameObject b = res.getStaticObj();

        switch (res.getSide()) {
            case TOP:
                a.getPosition().setY(b.getPosition().getY() - a.getHeight());
                a.getSpeed().setY(0);
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
