package it.unibo.platformer.model.entities;


import it.unibo.platformer.model.physics.impl.GameObjectImpl;
import it.unibo.platformer.model.physics.api.BasicPhysics;

public abstract class DynamicEntity extends Entity {


    protected final GameObjectImpl gameObject;//stores position size and velocity
    private  final BasicPhysics physics ;//applies gravity and movement
 
    protected boolean affectedByGravity; //if true the entity falls
    protected boolean onGround;//if on the ground the gravity stops
    public DynamicEntity(double x, double y, double width, double height , BasicPhysics physics){
        super(x, y, width, height);
        this.gameObject       = new GameObjectImpl((float) x, (float) y, (float) width, (float) height);
        this.affectedByGravity = true;
        this.onGround          = false;
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
    public void update(final double deltaTime) {
        if (affectedByGravity && !onGround) {
            // Full physics step: gravity accumulation + position integration
            physics.update(gameObject, deltaTime);
        } else if (!affectedByGravity) {
            // Gravity-free movement (e.g. moving shell): integrate X and Y manually
            final float dx = gameObject.getSpeed().getX() * (float) deltaTime;
            final float dy = gameObject.getSpeed().getY() * (float) deltaTime;
            gameObject.getPosition().setX(gameObject.getPosition().getX() + dx);
            gameObject.getPosition().setY(gameObject.getPosition().getY() + dy);
        }
        // else: on ground with gravity — position managed by collision resolver
    }

    //for velocity 
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
 
 /**
     * Sets whether the entity is resting on the ground.
     * Zeroes vertical velocity when landing to prevent bouncing artefacts.
     */
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
        if (onGround) {
            // Azzera la velocità Y quando tocca terra
            gameObject.getSpeed().setY(0);
        }
    }
 
    
    public GameObjectImpl getGameObject() { return gameObject; }
}