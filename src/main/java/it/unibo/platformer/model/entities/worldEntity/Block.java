package it.unibo.platformer.model.entities.worldEntity;

import it.unibo.platformer.model.entities.StaticEntity;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Block extends StaticEntity {

    public enum BlockType {
        NORMAL,
        BRICK,
        QUESTION,
    }

    private BlockType type;
    private boolean hit;
    private boolean hasContent;

    // Sprites (singoli, non spritesheet)
    private Image spriteNormal;
    private Image spriteBrick;
    private Image spriteBrickUsed;
    private Image spriteQuestion;

    public Block(double x, double y, BlockType type) {
        super(x, y, 32, 32);
        this.type = type;
        this.hit = false;
        this.hasContent = (type == BlockType.QUESTION);
        loadSprites();
    }

    public Block(double x, double y) {
        this(x, y, BlockType.NORMAL);
    }

    private void loadSprites() {
        spriteNormal       = AnimationManager.loadImage("src\\main\\resources\\sprites\\block.png");
        spriteBrick        = AnimationManager.loadImage("src\\main\\resources\\sprites\\brick.png");
        spriteBrickUsed    = AnimationManager.loadImage("src\\main\\resources\\sprites\\used_brick.png");
        spriteQuestion     = AnimationManager.loadImage("src\\main\\resources\\sprites\\surprise.png");
    }

    @Override
    public void update(double deltaTime) {
        // statico, niente da aggiornare
    }

    @Override
    public void render(GraphicsContext gc) {
        Image sprite = switch (type) {
            case BRICK    -> hit ? spriteBrickUsed : spriteBrick;
            case QUESTION -> hit ? spriteBrickUsed : spriteQuestion;
            default       -> spriteNormal;
        };

        if (sprite != null) {
            gc.drawImage(sprite, x, y, width, height);
        } else {
            // fallback colori se lo sprite manca
            switch (type) {
                case BRICK    -> gc.setFill(hit ? Color.GRAY : Color.SADDLEBROWN);
                case QUESTION -> gc.setFill(hit ? Color.GRAY : Color.GOLD);
                default       -> gc.setFill(Color.DARKGRAY);
            }
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height);

            if (type == BlockType.QUESTION && !hit) {
                gc.setFill(Color.BLACK);
                gc.fillText("?", x + 10, y + 22);
            }
        }
    }

    public boolean onHit() {
        if (hit) return false;
        if (hasContent) {
            hit = true;
            return true;
        }
        return false;
    }
}