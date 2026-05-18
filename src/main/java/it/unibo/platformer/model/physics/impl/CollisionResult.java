package it.unibo.platformer.model.physics.impl;

public class CollisionResult {
    private GameObjectImpl dynamicObj;
    private GameObjectImpl staticObj;
    private CollisionSide side;

    public CollisionResult(GameObjectImpl dynamicObj, GameObjectImpl staticObj, CollisionSide side){
        this.dynamicObj = dynamicObj;
        this.staticObj = staticObj;
        this.side = side;
    }

    public GameObjectImpl getDynamicObj(){
        return this.dynamicObj;
    }

    public GameObjectImpl getStaticObj(){
        return this.staticObj;
    }

    public CollisionSide getSide(){
        return this.side;
    }
}
