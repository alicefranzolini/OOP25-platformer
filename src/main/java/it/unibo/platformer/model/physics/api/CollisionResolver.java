package it.unibo.platformer.model.physics.api;

import java.util.List;

import it.unibo.platformer.model.physics.NoSideException;
import it.unibo.platformer.model.physics.impl.CollisionResult;

/**
 * This class resolve the collisions.
*/

public interface CollisionResolver {

    /**
     * This method resolve all the collisions.
     * 
     * @param collisions the list of collision
     * @throws NoSideException
    */
    void resolveAll(List<CollisionResult> collisions) throws NoSideException;

    /** 
     * This method resolve one collision.
     * 
     * @param res 
     * @throws NoSideException
    */
    void resolveOne(CollisionResult res) throws NoSideException;
    
}
