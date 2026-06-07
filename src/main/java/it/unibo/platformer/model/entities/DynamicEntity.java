package it.unibo.platformer.model.entities;


import it.unibo.platformer.model.physics.impl.GameObjectImpl;
import it.unibo.platformer.model.physics.api.BasicPhysics;

/**It represents an entity capable of movement and subject to physical forces. */
public abstract class DynamicEntity extends Entity {

    private final GameObjectImpl gameObject;/**stores position size and velocity*/
    private  final BasicPhysics physics; /**applies gravity and movement*/
    private boolean affectedByGravity; /**if true the entity falls*/
    private boolean onGround; /**if on the ground the gravity stops*/

    public DynamicEntity(double x, double y, double width, double height , BasicPhysics physics){
        super(x, y, width, height);
        this.gameObject = new GameObjectImpl((float) x, (float) y, (float) width, (float) height);
        this.affectedByGravity = true;
        this.onGround = false;
        this.physics = physics;
    }

    @Override
    public double getX() { return gameObject.getPosition().getX(); }
 
    @Override
    public double getY() { return gameObject.getPosition().getY(); }
 
    @Override
    public void setX(double x) { gameObject.getPosition().setX((float) x); }
 
    @Override
    public void setY(double y) { gameObject.getPosition().setY((float) y); }
 


   @Override
    public void update( final double deltaTime) {
        if (affectedByGravity && !onGround) {
            /**  Use the physics engine to calculate fall and friction.*/
            physics.UpdatePosition(gameObject, deltaTime);
        } else if (!affectedByGravity) {
            /**  Gravity-free movement (for example moving shell): integrate X and Y manually*/
            final float dx = gameObject.getSpeed().getX() * (float) deltaTime;
            final float dy = gameObject.getSpeed().getY() * (float) deltaTime;
            gameObject.getPosition().setX(gameObject.getPosition().getX() + dx);
            gameObject.getPosition().setY(gameObject.getPosition().getY() + dy);
        }
        /**else:position managed by collision resolver*/
    }

    /**for velocity */
    public double getVelocityX() {
        return gameObject.getSpeed().getX();
    }
 
    public double getVelocityY() {
        return gameObject.getSpeed().getY();
    }
 
    public void setVelocityX(double vx) {
        gameObject.getSpeed().setX((float) vx);
    }
 
    public void setVelocityY(double vy) {
        gameObject.getSpeed().setY((float) vy);
    }
 
 
   
    public boolean isAffectedByGravity() { return affectedByGravity; }
    public boolean isOnGround()          { return onGround; }
 
    public void setAffectedByGravity(boolean g) { this.affectedByGravity = g; }
    
    /** Avoid the bounce effect by resetting the vertical speed to zero upon landing. */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
        this.gameObject.SetOnGround(onGround);
        if (onGround) {
            gameObject.getSpeed().setY(0);
        }
    }
 
    
    public GameObjectImpl getGameObject() { return gameObject; }
}