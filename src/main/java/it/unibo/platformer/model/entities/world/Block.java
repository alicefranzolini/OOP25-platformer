package it.unibo.platformer.model.entities.world;

import it.unibo.platformer.model.entities.StaticEntity;
import it.unibo.platformer.view.AnimationManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * A static block in the world, which may be normal, brick, or a question block.
 */
public class Block extends StaticEntity {

    /** The visual and behavioural type of this block. */
    public enum BlockType {
        /** A plain decorative block. */
        NORMAL,
        /** A brick block that can be broken. */
        BRICK,
        /** A question block that yields a reward when hit. */
        QUESTION
    }

    private static final int QUESTION_MARK_OFFSET_X = 10;
    private static final int QUESTION_MARK_OFFSET_Y = 22;

    private BlockType type;
    private boolean hit;
    private boolean hasContent;

    private Image spriteNormal;
    private Image spriteBrick;
    private Image spriteBrickUsed;
    private Image spriteQuestion;

    /**
     * Constructs a block of the given type at the specified position.
     *
     * @param x    the x coordinate
     * @param y    the y coordinate
     * @param type the block type
     */
    public Block(final double x, final double y, final BlockType type) {
        super(x, y, 32, 32);
        this.type = type;
        this.hasContent = type == BlockType.QUESTION;
        loadSprites();
    }

    /**
     * Constructs a normal block at the specified position.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Block(final double x, final double y) {
        this(x, y, BlockType.NORMAL);
    }

    private void loadSprites() {
        spriteNormal = AnimationManager.loadImage("/sprites/block.png");
        spriteBrick = AnimationManager.loadImage("/sprites/brick.png");
        spriteBrickUsed = AnimationManager.loadImage("/sprites/used_brick.png");
        spriteQuestion = AnimationManager.loadImage("/sprites/surprise.png");
    }

    @Override
    public void update(final double deltaTime) {
        // static block — nothing to update
    }

    @Override
    public void render(final GraphicsContext gc) {
        final double bx = getX();
        final double by = getY();
        final double bwidth = getWidth();
        final double bheight = getHeight();

        final Image sprite = switch (type) {
            case BRICK -> hit ? spriteBrickUsed : spriteBrick;
            case QUESTION -> hit ? spriteBrickUsed : spriteQuestion;
            default -> spriteNormal;
        };

        if (sprite != null) {
            gc.drawImage(sprite, bx, by, bwidth, bheight);
        } else {
            renderFallback(gc, bx, by, bwidth, bheight);
        }
    }

    private void renderFallback(final GraphicsContext gc,
            final double bx, final double by,
            final double bwidth, final double bheight) {
        switch (type) {
            case BRICK -> gc.setFill(hit ? Color.GRAY : Color.SADDLEBROWN);
            case QUESTION -> gc.setFill(hit ? Color.GRAY : Color.GOLD);
            default -> gc.setFill(Color.DARKGRAY);
        }
        gc.fillRect(bx, by, bwidth, bheight);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(bx, by, bwidth, bheight);

        if (type == BlockType.QUESTION && !hit) {
            gc.setFill(Color.BLACK);
            gc.fillText("?", bx + QUESTION_MARK_OFFSET_X, by + QUESTION_MARK_OFFSET_Y);
        }
    }

    /**
     * Called when a player hits this block from below.
     *
     * @return {@code true} if the block had content and was triggered
     */
    public boolean onHit() {
        if (hit) {
            return false;
        }
        if (hasContent) {
            hit = true;
            return true;
        }
        return false;
    }
}