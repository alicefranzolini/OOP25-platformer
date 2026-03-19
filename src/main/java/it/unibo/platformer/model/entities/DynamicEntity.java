package it.unibo.platformer.model.entities;


import it.unibo.platformer.model.physics.BasicPhysics;
import it.unibo.platformer.model.physics.GameObject;
//usata per nemici, monete animate, power-up che si muovono, e il player 
public abstract class DynamicEntity extends Entity {

  // Oggetto fisico di Gabriele — gestisce posizione e velocità
    protected final GameObject gameObject;
 
    // Motore fisico di Gabriele
    private static final BasicPhysics physics = new BasicPhysics();
 
    // Se true, BasicPhysics applica la gravità
    protected boolean affectedByGravity;
 
    // Se true, l'entità è a contatto con il suolo
    protected boolean onGround;

    public DynamicEntity(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.gameObject       = new GameObject((float) x, (float) y, (float) width, (float) height);
        this.affectedByGravity = true;
        this.onGround          = false;
    }

    //Aggiorna posizione in base alla velocità.
    @Override
    public void update(double deltaTime) {
        if (affectedByGravity && !onGround) {
            physics.update(gameObject);
        } else if (!affectedByGravity) {
            // Muoviti solo in base alla velocità, senza gravità
            gameObject.getPosition().add(gameObject.getSpeed());
    }
        x = gameObject.getPosition().getX();
        y = gameObject.getPosition().getY();
    }


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
 
    // -------------------------------------------------------------------------
    // Posizione — aggiorna sia Entity che GameObject
    // -------------------------------------------------------------------------
 
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
 
    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------
 
    public boolean isAffectedByGravity() { return affectedByGravity; }
    public boolean isOnGround()          { return onGround; }
 
    public void setAffectedByGravity(boolean g) { this.affectedByGravity = g; }
 
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
        if (onGround) {
            // Azzera la velocità Y quando tocca terra
            gameObject.getSpeed().setY(0);
        }
    }
 
    /** Espone il GameObject per il CollisionDetector di Gabriele. */
    public GameObject getGameObject() { return gameObject; }
}