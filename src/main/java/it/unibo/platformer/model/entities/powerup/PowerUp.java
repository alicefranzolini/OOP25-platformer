package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.physics.BasicPhysics;

public abstract class PowerUp extends DynamicEntity {

    // Horizontal speed used once the power-up has finished emerging
    protected static final double MOVE_SPEED = 60.0;
    // Upward velocity used while the power-up is emerging from a block
    protected static final double EMERGE_SPEED = -40.0; 

    private boolean emerging;   // True while the power-up is still emerging from its spawn block 
    private double emergeTarget;    // Y coordinate where the power-up should stop emerging (fully outside the block)

  /*  public PowerUp(double x, double y, double width, double height) {
        /*
         *   Initialize position and size, start emergence motion, 
         *   disable gravity until emergence completes
         */
    /*    super(x, y, width, height);
        setVelocityX(0);                 // stop horizontal movement initially
        setVelocityY(EMERGE_SPEED);         // set vertical velocity so the power-up moves out of the block
        setAffectedByGravity(false);    // disable gravity while emerging
        this.emerging     = true;
        this.emergeTarget = y - height;
    } */

    public PowerUp(double x, double y, double width, double height) {
        super(x, y, width, height, new BasicPhysics());

        setVelocityX(0);                 
        setVelocityY(EMERGE_SPEED);      
        setAffectedByGravity(false);     

        this.emerging     = true;
        this.emergeTarget = y - height;
    }

        
    @Override
    public void update(double deltaTime) {
        // Update base physics (position/velocity) once per frame
        super.update(deltaTime);

            // If still emerging, check if we've reached the target Y
        if (emerging) {
            // If reached or passed the target, snap to target and switch to normal physics
            // enable gravity and start horizontal movement
            if (getY() <= emergeTarget) {
                setY(emergeTarget);
                emerging = false;
                setAffectedByGravity(true);
                setVelocityX(MOVE_SPEED);
            }
        }
    }

    
     // Flip horizontal velocity when hitting an obstacle
    public void reverseDirection() {
        setVelocityX(-getVelocityX());
    }

   // Subclasses implement this to apply their specific effect to the player
    public abstract void applyEffect(Object player);

    // Called when player collects the power-up: apply effect then destroy this entity    
    public void collect(Object player) {
        applyEffect(player);
        destroy();
    }

    // Returns true while the power-up is still emerging (should not be collectible)
    public boolean isEmerging() { 
        return emerging; 
    }
}