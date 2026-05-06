package it.unibo.platformer.model.entities;


import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;

public abstract class DynamicEntity extends Entity {


    protected final GameObjectImpl gameObject;//stores position size and velocity
    private static final BasicPhysicsImpl physics = new BasicPhysicsImpl();//applies gravity and movement
 
    protected boolean affectedByGravity; //if true the entity falls
    protected boolean onGround;//if on the ground the gravity stops

    public DynamicEntity(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.gameObject       = new GameObjectImpl((float) x, (float) y, (float) width, (float) height);
        this.affectedByGravity = true;
        this.onGround          = false;
    }

    @Override
    public void update(double deltaTime) {
        if (affectedByGravity && !onGround) {//if in air updates the physics
            physics.update(gameObject);
        } else if (!affectedByGravity) { // moves only according to velocity
            gameObject.getPosition().add(gameObject.getSpeed());
        }
        x = gameObject.getPosition().getX();
        y = gameObject.getPosition().getY();
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
 
    // for positions
    @Override
    public void setX(double x) {
        this.x = x;
        gameObject.getPosition().setX((float) x);
    }
 
    @Override
    public void setY(double y) {
        this.y = y;
        gameObject.getPosition().setY((float) y);
    }
 
   
    public boolean isAffectedByGravity() { return affectedByGravity; }
    public boolean isOnGround()          { return onGround; }
 
    public void setAffectedByGravity(boolean g) { this.affectedByGravity = g; }
 
 
    //to prevent error with fall or bounce 
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
        if (onGround) {
            // Azzera la velocità Y quando tocca terra
            gameObject.getSpeed().setY(0);
        }
    }
 
    
    public GameObjectImpl getGameObject() { return gameObject; }
}