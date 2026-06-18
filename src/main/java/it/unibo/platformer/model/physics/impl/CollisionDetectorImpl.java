package it.unibo.platformer.model.physics.impl;

import java.awt.Rectangle;

import it.unibo.platformer.model.physics.api.CollisionDetector;
import it.unibo.platformer.model.physics.api.GameObject;

/**
 * Implementation of the collision detector.
 */
public final class CollisionDetectorImpl implements CollisionDetector {

    @Override
    public boolean collision(final GameObject a, final GameObject b) {
        final Rectangle r1 = new Rectangle((int) a.getPosition().getX(), (int) a.getPosition().getY(),
                (int) a.getWidth(), (int) a.getHeight());

        final Rectangle r2 = new Rectangle((int) b.getPosition().getX(), (int) b.getPosition().getY(),
                (int) b.getWidth(), (int) b.getHeight());

        return r1.intersects(r2);
    }

    @Override
    public CollisionResult getCollisionResult(final GameObjectImpl a, final GameObjectImpl b) {
        if (!collision(a, b)) {
            return null;
        } else {
            final float centerAX = a.getPosition().getX() + a.getWidth() / 2;
            final float centerAY = a.getPosition().getY() + a.getHeight() / 2;
            final float centerBX = b.getPosition().getX() + b.getWidth() / 2;
            final float centerBY = b.getPosition().getY() + b.getHeight() / 2;

            final float distanceA = centerAX - centerBX;
            final float distanceB = centerAY - centerBY;

            final float combineWidhts = (a.getWidth() + b.getWidth()) / 2;
            final float combineHeights = (a.getHeight() + b.getHeight()) / 2;

            final float insideX = combineWidhts - Math.abs(distanceA);
            final float insideY = combineHeights - Math.abs(distanceB);

            if (insideX < insideY) {
                if (distanceA > 0) {
                    return new CollisionResult(a, b, CollisionSide.RIGHT);
                } else {
                    return new CollisionResult(a, b, CollisionSide.LEFT);
                }
            } else {
                if (distanceB > 0) {
                    return new CollisionResult(a, b, CollisionSide.BOTTOM);
                } else {
                    return new CollisionResult(a, b, CollisionSide.TOP);
                }
            }
        }
    }

}
