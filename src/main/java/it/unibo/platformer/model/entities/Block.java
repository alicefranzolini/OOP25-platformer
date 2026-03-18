package it.unibo.platformer.model.entities;
 
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
 

public class Block extends StaticEntity {
 //Può essere un blocco normale, un blocco "?" con sorpresa, o un mattone.
    public enum BlockType {
        NORMAL,     // Blocco marrone standard
        BRICK,      // Mattone distruttibile
        QUESTION,   // Blocco "?" con power-up o moneta
    }
 
    private BlockType type;
    private boolean hit;           // Se il blocco è già stato colpito
    private boolean hasContent;    // Se c'è qualcosa dentro (per QUESTION )

 
    public Block(double x, double y, BlockType type) {
        super(x, y, 32, 32); // 32x32 pixel
        this.type = type;
        this.hit = false;
        this.hasContent = (type == BlockType.QUESTION );
    
    }
 
    // Costruttore blocco normale 
    public Block(double x, double y) {
        this(x, y, BlockType.NORMAL);
    }
 
    @Override
    public void update(double deltaTime) {
        
        
    }
 
    @Override
    public void render(GraphicsContext gc) {
        double renderY = y;
 
        // Placeholder grafico — sarà sostituito con sprite pixel-art
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
 
    //restituisce true se c'è contenuto da far uscire.
    public boolean onHit() {
        if (hit) return false;
 
        if (hasContent) {
            hit = true;
            // Il contenuto (moneta o power-up) viene gestito da PowerUpFactory / LevelLoader
            return true;
        }
 
 
        return false;
    }

}