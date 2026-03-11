package it.unibo.platformer.model.entities;



public abstract class PowerUp extends DynamicEntity {

    // I power-up escono dal blocco e poi si muovono lentamente
    protected static final double MOVE_SPEED = 60.0;
    protected static final double EMERGE_SPEED = -40.0; 

    private boolean emerging;    
    private double emergeTarget; 

    public PowerUp(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.velocityX = 0;
        this.velocityY = EMERGE_SPEED;
        this.emerging = true;
        this.emergeTarget = y - height; 
        this.affectedByGravity = false;
    }

    @Override
    public void update(double deltaTime) {
        if (emerging) {
            y += velocityY * deltaTime;
            if (y <= emergeTarget) {
                y = emergeTarget;
                emerging = false;
                affectedByGravity = true;   
                velocityX = MOVE_SPEED;     
            }
        } else {
            x += velocityX * deltaTime;
        }
    }

    
     
    public void reverseDirection() {
        velocityX = -velocityX;
    }

   
    public abstract void applyEffect(Object player);

   
     //Applica l'effetto al player e si rimuove
     
    public void collect(Object player) {
        applyEffect(player);
        destroy();
    }

    public boolean isEmerging() { return emerging; }
}