package it.unibo.platformer.model.physics.api;

/*
 * This class create the physics for the objects
*/

public interface BasicPhysics {
    
    /*
     * This method update the position of an object with the delta time
     * @param obj the object
     * @param deltaTime the delta time
    */
    void updatePosition(GameObject obj, double deltaTime);

}
