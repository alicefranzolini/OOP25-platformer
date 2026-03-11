package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;

public class StaticEntity extends Entity {
    
    // Indica se l'entità è solida (genera collisioni fisiche)
    protected boolean solid;

    public StaticEntity(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.solid = true;
    }

    
    @Override
    public void update(double deltaTime) {
        // Nessun aggiornamento di default per entità statiche
    }

    public boolean isSolid() { 
        return solid; }
    public void setSolid(boolean solid) { 
        this.solid = solid; }

    @Override
    public void render(GraphicsContext gc) {
        throw new UnsupportedOperationException("Unimplemented method 'render'");
    }
}

