package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.AbstractDynamicEntity;
import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.physics.api.BasicPhysics;

/**
 * Base implementation shared by all power-ups.
 */
public abstract class AbstractPowerUp extends AbstractDynamicEntity implements PowerUp {

    /**
     * Horizontal speed used after the power-up has emerged.
     */
    protected static final double MOVE_SPEED = 60.0;

    /**
     * Upward velocity used while the power-up is emerging.
     */
    protected static final double EMERGE_SPEED = -40.0;

    private boolean emerging;
    private final double emergeTarget;

    /**
     * Creates a power-up with emergence movement enabled.
     *
     * @param x the initial x coordinate
     * @param y the initial y coordinate
     * @param width the power-up width
     * @param height the power-up height
     * @param physics the physics engine used to update the power-up
     */
    public AbstractPowerUp(
            final double x,
            final double y,
            final double width,
            final double height,
            final BasicPhysics physics
    ) {
        super(x, y, width, height, physics);
        setVelocityX(0);
        setVelocityY(EMERGE_SPEED);
        setAffectedByGravity(false);
        this.emerging = true;
        this.emergeTarget = y - height;
    }

    /**
     * Updates emergence and regular physics.
     *
     * @param deltaTime the elapsed time since the previous update
     */
    @Override
    public void update(final double deltaTime) {
        super.update(deltaTime);
        if (emerging && getY() <= emergeTarget) {
            setY(emergeTarget);
            emerging = false;
            setAffectedByGravity(true);
            setVelocityX(MOVE_SPEED);
        }
    }

    /**
     * Reverses the horizontal movement direction.
     */
    @Override
    public void reverseDirection() {
        setVelocityX(-getVelocityX());
    }

    @Override
    public abstract void applyEffect(Player player);

    /**
     * Applies the power-up effect to the player and destroys this power-up.
     *
     * @param player the player collecting the power-up
     */
    @Override
    public void collect(final Player player) {
        applyEffect(player);
        destroy();
    }

    /**
     * Checks whether the power-up is still emerging from its spawn block.
     *
     * @return true if the power-up is emerging
     */
    @Override
    public boolean isEmerging() {
        return emerging;
    }
}
