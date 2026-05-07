package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;

 

public abstract class Entity {

        //position
    protected double x;
    protected double y;

            //sizes
    protected double width;
    protected double height;

    protected boolean active=true; //if the entity is alive

   
   //initialize variables for entity creation
    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

//Returns the rectangle used for collision detection.
    public double[] getBoundingBox() {
        return new double[]{ x, y, width, height };
    }


    public abstract void update(double deltaTime);//handles logic
    public abstract void render(GraphicsContext gc);//draws the entity 

   

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