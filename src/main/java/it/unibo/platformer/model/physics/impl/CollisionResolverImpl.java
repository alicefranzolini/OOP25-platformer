package it.unibo.platformer.model.physics.impl;

import java.util.List;

import it.unibo.platformer.model.physics.NoSideException;
import it.unibo.platformer.model.physics.api.CollisionResolver;

/**
 * Collision resolver implementation.
 */
public final class CollisionResolverImpl implements CollisionResolver {

    @Override
    public void resolveAll(final List<CollisionResult> collisions) throws NoSideException {
        for (final CollisionResult res : collisions) {
            resolveOne(res);
        }
    }

    @Override
    public void resolveOne(final CollisionResult res) throws NoSideException {
        final GameObjectImpl a = res.getDynamicObj();
        final GameObjectImpl b = res.getStaticObj();

        switch (res.getSide()) {
            case TOP:
                a.getPosition().setY(b.getPosition().getY() - a.getHeight());
                a.getSpeed().setY(0);
                a.setOnGround(true);
                break;
            case BOTTOM:
                a.getPosition().setY(b.getPosition().getY() + b.getHeight());
                a.getSpeed().setY(0);
                break;
            case LEFT:
                a.getPosition().setX(b.getPosition().getX() + b.getWidth());
                a.getSpeed().setX(0);
                break;
            case RIGHT:
                a.getPosition().setX(b.getPosition().getX() - a.getWidth());
                a.getSpeed().setX(0);
                break;
            case NONE:
                break;
        }
    }

}
