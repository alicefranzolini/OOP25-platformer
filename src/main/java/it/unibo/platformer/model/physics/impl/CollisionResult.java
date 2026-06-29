package it.unibo.platformer.model.physics.impl;

/**
 * This class give the side of the collision.
 */
public final class CollisionResult {
    private final GameObjectImpl dynamicObj;
    private final GameObjectImpl staticObj;
    private final CollisionSide side;

    /**
     * the builder of the collision result.
     * 
     * @param dynamicObj the first object
     * @param staticObj the second object
     * @param side the side of the collision
     */
    public CollisionResult(final GameObjectImpl dynamicObj, final GameObjectImpl staticObj, final CollisionSide side) {
        this.dynamicObj = dynamicObj;
        this.staticObj = staticObj;
        this.side = side;
    }

    /**
     * @return the GameObjectImpl
     */
    public GameObjectImpl getDynamicObj() {
        return this.dynamicObj;
    }

    /**
     * @return the static object
     */
    public GameObjectImpl getStaticObj() {
        return this.staticObj;
    }

    /**
     * @return the side
     */
    public CollisionSide getSide() {
        return this.side;
    }

}
