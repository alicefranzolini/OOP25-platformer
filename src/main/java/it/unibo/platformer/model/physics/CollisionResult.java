package it.unibo.platformer.model.physics;

public class CollisionResult {
    private GameObject dynamicObj;
    private GameObject staticObj;
    private CollisionSide side;

    public CollisionResult(GameObject dynamicObj, GameObject staticObj, CollisionSide side){
        this.dynamicObj = dynamicObj;
        this.staticObj = staticObj;
        this.side = side;
    }

    public GameObject getDynamicObj(){
        return this.dynamicObj;
    }

    public GameObject getStaticObj(){
        return this.staticObj;
    }

    public CollisionSide getSide(){
        return this.side;
    }
}
