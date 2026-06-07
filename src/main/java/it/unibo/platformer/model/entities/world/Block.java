package it.unibo.platformer.model.entities.world;

import it.unibo.platformer.model.entities.StaticEntity;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Block extends StaticEntity {

    public enum BlockType {
        NORMAL,
        BRICK,
        QUESTION
    }

    private BlockType type;
    private boolean hit;
    private boolean hasContent;

    // Sprites 
    private Image spriteNormal;
    private Image spriteBrick;
    private Image spriteBrickUsed;
    private Image spriteQuestion;

    public Block(double x, double y, BlockType type) {
        super(x, y, 32, 32);
        this.type = type;
        this.hit = false;
        this.hasContent = (type == BlockType.QUESTION); // Only QUESTION blocks are created with default content 
        loadSprites();
    }

    //Creates a normal block at the specified position.
    
    public Block(double x, double y) {
        this(x, y, BlockType.NORMAL);
    }

    private void loadSprites() {
        spriteNormal       = AnimationManager.loadImage("/sprites/block.png");
        spriteBrick        = AnimationManager.loadImage("/sprites/brick.png");
        spriteBrickUsed    = AnimationManager.loadImage("/sprites/used_brick.png");
        spriteQuestion     = AnimationManager.loadImage("/sprites/surprise.png");
    }

    @Override
    public void update(double deltaTime) {
        // static, nothing to update
    }

    @Override
    public void render(GraphicsContext gc) {
        final double bx = getX();
        final double by = getY();
        final double bwidth = getWidth();
        final double bheight = getHeight();

        Image sprite = switch (type) {
            case BRICK    -> hit ? spriteBrickUsed : spriteBrick;
            case QUESTION -> hit ? spriteBrickUsed : spriteQuestion;
            default       -> spriteNormal;
        };

        if (sprite != null) {
            gc.drawImage(sprite, bx, by, bwidth, bheight);
        } else {
            // if the sprite fails to load, fallback to a simple colored block
            switch (type) {
                case BRICK    -> gc.setFill(hit ? Color.GRAY : Color.SADDLEBROWN);
                case QUESTION -> gc.setFill(hit ? Color.GRAY : Color.GOLD);
                default       -> gc.setFill(Color.DARKGRAY);
            }
            gc.fillRect(bx, by, bwidth, bheight);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(bx, by, bwidth, bheight);

            if (type == BlockType.QUESTION && !hit) {
                gc.setFill(Color.BLACK);
                gc.fillText("?", bx + 10, by + 22);
            }
        }
    }
//Method called when a Player hits the block from below.
    public boolean onHit() {
        if (hit) return false;
        if (hasContent) {
            hit = true;
            return true;
        }
        return false;
    }
}