package it.unibo.platformer.model.entities.worldEntity;
 
import it.unibo.platformer.model.entities.StaticEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
 

public class Block extends StaticEntity {
 
    public enum BlockType {//it can have different type
        NORMAL,     
        BRICK,      
        QUESTION,   
    }
 
    private BlockType type;
    private boolean hit;           // if it's already been hitted
    private boolean hasContent;    // used for question block
    public Block(double x, double y, BlockType type) {
        super(x, y, 32, 32); // 32x32 pixel
        this.type = type;
        this.hit = false;
        this.hasContent = (type == BlockType.QUESTION );//only for question block
    
    }
 
    //constructor for normal block
    public Block(double x, double y) {
        this(x, y, BlockType.NORMAL);
    }
 
    @Override
    public void update(double deltaTime) {
       
    }
 
    @Override
    public void render(GraphicsContext gc) {
        double renderY = y;
 
        // change with sprite pixel-art
        switch (type) {
            case BRICK:
                gc.setFill(hit ? Color.GRAY : Color.SADDLEBROWN);
                break;
            case QUESTION:
                gc.setFill(hit ? Color.GRAY : Color.GOLD);
                break;
            default:
                gc.setFill(Color.DARKGRAY);
        }
 
        gc.fillRect(x, renderY, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, renderY, width, height);
 
        if (type == BlockType.QUESTION && !hit) {
            gc.setFill(Color.BLACK);
            gc.fillText("?", x + 10, renderY + 22);
        }
    }
 //for the spawning(not here)
    public boolean onHit() {
        if (hit) 
            return false;
 
        if (hasContent) {
            hit = true;
           
            return true;
        }
 
 
        return false;
    }

}