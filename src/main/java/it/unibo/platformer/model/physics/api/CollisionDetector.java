package it.unibo.platformer.model.physics.api;

import it.unibo.platformer.model.physics.impl.CollisionResult;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;

public interface CollisionDetector {

    public boolean collision(GameObject a, GameObject b);

    public CollisionResult getCollisionResult(GameObjectImpl a, GameObjectImpl b);

}
