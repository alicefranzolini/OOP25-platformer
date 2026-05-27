package it.unibo.platformer.model.entities.enemies;

import javafx.scene.canvas.GraphicsContext;

//It defines the behaviors observable by the game system (loops, collisions, HUD) without exposing implementation details.
public interface Enemy {

    
    void update(double deltaTime);
    void render(GraphicsContext gc);

    //true if this enemy can currently hurt or kill the player
    boolean hitsPlayer();

   //true if this enemy is currently moving 
    boolean isWalking();
    //true if this enemy is currently active in the game world   
    boolean isActive();

    double getX();

    double getY();

    double getWidth();

    double getHeight();
}