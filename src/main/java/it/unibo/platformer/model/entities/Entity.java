package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;
import java.util.List;
import java.util.stream.Collectors;

//Defines the position and size of any object in the game world.
public abstract class Entity {

        
    protected double x;
    protected double y;

           
    protected double width;
    protected double height;

    protected boolean active; //if the entity is alive or not, if false it will be removed from the game world
   
   
    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

 //Internal BoundingBox to handle AABB collisions. Simplifies the calculation of overlap between rectangles.
    public static final class BoundingBox {
 
        private final double x;
        private final double y;
        private final double width;
        private final double height;
 
        public BoundingBox(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
 
        public double getX()      { return x; }
        public double getY()      { return y; }
        public double getWidth()  { return width; }
        public double getHeight() { return height; }
 
      // Core method for detecting collisions between entities.
        public boolean overlaps(BoundingBox other) {
            return x < other.x + other.width
                && x + width  > other.x
                && y < other.y + other.height
                && y + height > other.y;
        }
    }
 
    // Returns the bounding box of this entity for collision detection. 
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, y, width, height);
    }

    // Methods to implement in subclasses: one for logic, one for drawing.
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
    
    //Utility method for clearing the entity list. Filters the passed list, keeping only the objects that are still active.
    //Uses Java's Stream API for efficient and readable processing.

    public static List<Entity> filterActive(List<Entity> entities) {
        return entities.stream()
                .filter(Entity::isActive)
                .collect(Collectors.toList());
    }
}