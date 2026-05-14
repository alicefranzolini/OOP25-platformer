package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.players.Player;

/**
 * Contract for all power-up entities.
 * Defines emergence behaviour, effect application, and collection.
 * Concrete power-ups (Mushroom, Star) implement applyEffect differently
 * — this is the polymorphism entry point.
 */
public interface PowerUp {

    // Called every frame
    void update(double deltaTime);

    // Applies the specific effect to the player (polymorphic)
    void applyEffect(Player player);

    // Collects the power-up: applies effect and destroys it
    void collect(Player player);

    // Flips horizontal direction when hitting a wall
    void reverseDirection();

    // True while still emerging from the block (not yet collectible)
    boolean isEmerging();

    // Position and size
    double getX();
    double getY();
    double getWidth();
    double getHeight();
    boolean isActive();
}