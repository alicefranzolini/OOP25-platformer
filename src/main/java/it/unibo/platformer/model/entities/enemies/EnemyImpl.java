package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import java.util.Optional;

//Basic implementation that centralizes physics, animations, and state management.
public abstract class EnemyImpl extends DynamicEntity implements Enemy {

   //interface for different enemy states, each state has its own update/render logic and player interaction rules
    public interface EnemyStateHandler {
        
        void update(EnemyImpl e, double deltaTime);
        
        void render(EnemyImpl e, GraphicsContext gc);
        
        boolean hitsPlayer();
    }
    //** Marker interface to group all walking states. */
    public interface WalkingHandler extends EnemyStateHandler {}

    
    protected final AnimationManager anim = new AnimationManager();
    protected boolean facingLeft = true;

    // The active state handler; drives update() and render(). 
    private Optional<EnemyStateHandler> handler = Optional.empty();

   
    
    protected EnemyImpl(double x, double y, double width, double height, BasicPhysics physics) {
        super(x, y, width, height, physics);
        loadAnimations();
    }


    
    // subclasses register their own sprites
    protected abstract void loadAnimations();

    
    // subclasses must not override these
    
    @Override
    public final void update(double deltaTime) {
        handler.ifPresent(h -> h.update(this, deltaTime));
    }

    @Override
    public final void render(GraphicsContext gc) {
        if (!isActive()) return;
        handler.ifPresent(h -> h.render(this, gc));
    }

   
    protected void transitionTo(EnemyStateHandler newHandler) {
        this.handler = Optional.of(newHandler);
    }


    //Utility method to advance basic physics; can be called by state handlers as needed.
    protected void physicsTick(double deltaTime) {
        super.update(deltaTime);
    }

    
    
    @Override
    public boolean hitsPlayer() {
        return handler.map(EnemyStateHandler::hitsPlayer).orElse(false);
    }

    @Override
    public boolean isWalking() {
        return handler.map(h -> h instanceof WalkingHandler).orElse(false);
    }

    public void setHeight(double height) {
    this.height = height; // Ensure 'height' is the correct name of the field in your base class
}
}