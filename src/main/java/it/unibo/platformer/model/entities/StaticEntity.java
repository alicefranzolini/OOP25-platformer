package it.unibo.platformer.model.entities;

import javafx.scene.canvas.GraphicsContext;

public class StaticEntity extends Entity {
    
    
    protected boolean solid;//indicates whether the entity should block movement.(if the players collides with it or can pass through)

    public StaticEntity(double x, double y, double width, double height) {
        super(x, y, width, height);
        this.solid = true;
    }

    
    @Override
    public void update(double deltaTime) {
        // empty because static entities do not move or animate by default
    }

    public boolean isSolid() { 
        return solid; }
    public void setSolid(boolean solid) { 
        this.solid = solid; }

    @Override
    public void render(GraphicsContext gc) {
    }
}

