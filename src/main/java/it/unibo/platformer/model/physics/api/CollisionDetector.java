package it.unibo.platformer.model.physics.api;

import it.unibo.platformer.model.physics.impl.CollisionResult;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;

/* 
 * Represent the collision detector of the game
*/

public interface CollisionDetector {

    /*
     *This method check if two objects collide
     * @param a the first object
     * @param b the second object
     * @return true if they collide
    */
    boolean collision(GameObject a, GameObject b);

    /*
     *This method return the side of the collision for evry object created
     * @param a the first object
     * @param b the second object
     * @return the CollisionResult
    */
    CollisionResult getCollisionResult(GameObjectImpl a, GameObjectImpl b);

}
