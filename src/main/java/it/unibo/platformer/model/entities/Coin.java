package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
 

public class Coin extends DynamicEntity {
 
    

    // Animazione uscita dal blocco
    private boolean isPopping;       // Sta saltando fuori dal blocco?
    private double popStartY;        // Y di partenza del pop
    private static final double POP_VELOCITY = -300.0;
 
    public Coin(double x, double y) {
        super(x, y, 16, 16);
        this.affectedByGravity = false; // La moneta statica non cade
        this.isPopping = false;
    }
 
    
    //Crea una moneta che salta fuori da un blocco.
     
    public static Coin createPopping(double x, double y) {
        Coin coin = new Coin(x, y);
        coin.isPopping = true;
        coin.popStartY = y;
        coin.velocityY = POP_VELOCITY;
        coin.affectedByGravity = false; // Gestiamo noi il movimento del pop
        return coin;
    }
 
    @Override
    public void update(double deltaTime) {
       
 
        // Se sta saltando fuori da un blocco
        if (isPopping) {
            y += velocityY * deltaTime;
            velocityY += 600 * deltaTime; // Gravità manuale per il pop
 
            
            if (y >= popStartY) {
                destroy();    //quando mario tocca il blocco da sotto la moneta salta fuori e svanisce
            }
        }
    }
 
    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;


    // da sostituire con sprite 
        gc.setFill(Color.GOLD);
        gc.fillOval(x, y, width, height);
        gc.setStroke(Color.DARKORANGE);
        gc.strokeOval(x, y, width, height);
 
        
    }
 
    public boolean isPopping() { return isPopping; }
}