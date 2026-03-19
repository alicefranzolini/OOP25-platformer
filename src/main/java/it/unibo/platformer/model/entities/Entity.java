package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;


 //Classe base per tutte le entità del gioco. 

public abstract class Entity {

    protected double x;
    protected double y;

    protected double width;
    protected double height;

    protected boolean active;

    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

    // Aggiorna lo stato dell'entità ad ogni frame.
    public abstract void update(double deltaTime);


    // Disegna l'entità
    public abstract void render(GraphicsContext gc);

   //Restituisce il rettangolo di collisione dell'entità.
     
    public double[] getBoundingBox() {
        return new double[]{ x, y, width, height };
    }


    public double getX() { 
        return x; }
    public double getY() { 
        return y; }
    public double getWidth() { 
        return width; }
    public double getHeight() { 
        return height; }
    public boolean isActive() { 
        return active; }

    public void setX(double x) { 
        this.x = x; }
    public void setY(double y) { 
        this.y = y; }
    public void setActive(boolean active) { 
        this.active = active; }

    //Drimuove l'entità al prossimo frame
    public void destroy() {
        this.active = false;
    }
}