package it.unibo.platformer.model.entities.world;

import it.unibo.platformer.model.entities.world.Block.BlockType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link Block}.
 */
class BlockTest {

    /** X coordinate used for all test blocks. */
    private static final double BLOCKX = 10.0;

    /** Y coordinate used for all test blocks. */
    private static final double BLOCKY = 20.0;

    /** Expected side length of a block, in pixels. */
    private static final double BLOCKSIZE = 32.0;

    private static final double DELTATIME = 0.016;

    /**
     * Verifies that position and size are stored correctly on construction.
     */
    @Test
    void constructorNormalTypesetsPositionAndSize() {
        final Block block = new Block(BLOCKX, BLOCKY, BlockType.NORMAL);
        assertTrue(block.getX() == BLOCKX);
        assertTrue(block.getY() == BLOCKY);
        assertTrue(block.getWidth() == BLOCKSIZE);
        assertTrue(block.getHeight() == BLOCKSIZE);
    }

    /**
     * Verifies that the convenience constructor creates a block with no content.
     */
    @Test
    void convenienceConstructorcreatesNormalBlock() {
        final Block block = new Block(0, 0);
        assertFalse(block.onHit());
    }

    // -----------------------------------------------------------------------
    // NORMAL block
    // -----------------------------------------------------------------------

    /**
     * A normal block has no content and must return {@code false} on hit.
     */
    @Test
    void normalBlockonHitreturnsFalse() {
        final Block block = new Block(0, 0, BlockType.NORMAL);
        assertFalse(block.onHit());
    }

    /**
     * Hitting a normal block multiple times must always return {@code false}.
     */
    @Test
    void normalBlockonHitTwicealwaysReturnsFalse() {
        final Block block = new Block(0, 0, BlockType.NORMAL);
        assertFalse(block.onHit());
        assertFalse(block.onHit());
    }

    // -----------------------------------------------------------------------
    // BRICK block
    // -----------------------------------------------------------------------

    /**
     * A brick block has no content, so {@code onHit()} must return {@code false}.
     */
    @Test
    void brickBlockfirstHitreturnsFalse() {
        final Block block = new Block(0, 0, BlockType.BRICK);
        assertFalse(block.onHit());
    }

    /**
     * Hitting a brick block a second time must still return {@code false}.
     */
    @Test
    void brickBlocksecondHitstillReturnsFalse() {
        final Block block = new Block(0, 0, BlockType.BRICK);
        block.onHit();
        assertFalse(block.onHit());
    }

    // -----------------------------------------------------------------------
    // QUESTION block
    // -----------------------------------------------------------------------

    /**
     * The first hit on a question block must return {@code true},
     * signalling that content was released.
     */
    @Test
    void questionBlockfirstHitreturnsTrue() {
        final Block block = new Block(0, 0, BlockType.QUESTION);
        assertTrue(block.onHit());
    }

    /**
     * After the content is consumed, subsequent hits must return {@code false}.
     */
    @Test
    void questionBlocksecondHitreturnsFalse() {
        final Block block = new Block(0, 0, BlockType.QUESTION);
        block.onHit();
        assertFalse(block.onHit());
    }

    /**
     * Only the first hit yields content; every further hit must return {@code false}.
     */
    @Test
    void questionBlockmultipleHitsneverReturnTrueAgain() {
        final Block block = new Block(0, 0, BlockType.QUESTION);
        block.onHit();
        final int extraHits = 5;
        for (int i = 0; i < extraHits; i++) {
            assertFalse(block.onHit());
        }
    }

    // -----------------------------------------------------------------------
    // update() is a no-op – must not throw
    // -----------------------------------------------------------------------

    /**
     * Calling {@code update()} on any block must not throw an exception.
     */
    @Test
    void updatedoesNotThrow() {
        final Block block = new Block(5, 5, BlockType.BRICK);
        assertDoesNotThrow(() -> block.update(DELTATIME));
    }
}
