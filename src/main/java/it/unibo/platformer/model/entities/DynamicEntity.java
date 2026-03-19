package it.unibo.platformer.model.entities;

//usata per nemici, monete animate, power-up che si muovono, e il player 
public abstract class DynamicEntity extends Entity {

  
    protected double velocityX;
    protected double velocityY;

    // Se true, il PhysicsEngine applica la gravità a questa entità
    protected boolean affectedByGravity;

    // Se true, l'entità è a contatto con il suolo
    protected boolean onGround;

    public DynamicEntity(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.velocityX = 0;
        this.velocityY = 0;
        this.affectedByGravity = true;
        this.onGround = false;
    }

    //Aggiorna posizione in base alla velocità.
    @Override
    public void update(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
    }



    public double getVelocityX() { 
        return velocityX; }
    public double getVelocityY() { 
        return velocityY; }
    public boolean isAffectedByGravity() { 
        return affectedByGravity; }
    public boolean isOnGround() { 
        return onGround; }

    public void setVelocityX(double vx) { 
        this.velocityX = vx; }
    public void setVelocityY(double vy) { 
        this.velocityY = vy; }
    public void setAffectedByGravity(boolean g) { 
        this.affectedByGravity = g; }
    public void setOnGround(boolean onGround) { 
        this.onGround = onGround; }
}
