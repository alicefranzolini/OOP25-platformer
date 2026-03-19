package it.unibo.platformer.model.entities;



public abstract class PowerUp extends DynamicEntity {

    // I power-up escono dal blocco e poi si muovono lentamente
    protected static final double MOVE_SPEED = 60.0;
    protected static final double EMERGE_SPEED = -40.0; 

    private boolean emerging;    
    private double emergeTarget; 

    public PowerUp(double x, double y, double width, double height) {
        super(x, y, width, height);
        setVelocityX(0);                    // ← setter
        setVelocityY(EMERGE_SPEED);         // ← setter
        setAffectedByGravity(false);
        this.emerging     = true;
        this.emergeTarget = y - height;
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime); // BasicPhysics aggiorna la posizione
            if (y <= emergeTarget) {
                setY(emergeTarget);
                emerging = false;
                setAffectedByGravity(true);
                setVelocityX(MOVE_SPEED);   // ← setter
            }
         else {
            super.update(deltaTime);
        }
    }

    
     
    public void reverseDirection() {
        setVelocityX(-getVelocityX());
    }

   
    public abstract void applyEffect(Object player);

   
     //Applica l'effetto al player e si rimuove
     
    public void collect(Object player) {
        applyEffect(player);
        destroy();
    }

    public boolean isEmerging() { return emerging; }
}