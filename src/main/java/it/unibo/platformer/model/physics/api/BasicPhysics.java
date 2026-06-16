package it.unibo.platformer.model.physics.api;

public interface BasicPhysics {
    
    /*
     * This method update the position of an object with the delta time
     * @param obj the object
     * @param deltaTime the delta time
    */
    void updatePosition(GameObject obj, double deltaTime);

}
