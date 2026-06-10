package it.unibo.platformer.entity;
import org.junit.jupiter.api.Test;

import it.unibo.platformer.model.entities.world.Block;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    @Test
    void questionBlockOnHitReturnsTrue() {
        Block block = new Block(0, 0, Block.BlockType.QUESTION);
        assertTrue(block.onHit());
    }

    @Test
    void questionBlockSecondHitReturnsFalse() {
        Block block = new Block(0, 0, Block.BlockType.QUESTION);
        block.onHit();
        assertFalse(block.onHit());
    }

    @Test
    void normalBlockOnHitReturnsFalse() {
        Block block = new Block(0, 0, Block.BlockType.NORMAL);
        assertFalse(block.onHit());
    }

    @Test
    void brickBlockOnHitReturnsFalse() {
        Block block = new Block(0, 0, Block.BlockType.BRICK);
        assertFalse(block.onHit());
    }

    @Test
    void defaultConstructorCreatesNormalBlock() {
        Block block = new Block(0, 0);
        // normal block → onHit always false
        assertFalse(block.onHit());
    }

    @Test
    void blockHasCorrectDimensions() {
        Block block = new Block(64, 128);
        assertEquals(64, block.getX());
        assertEquals(128, block.getY());
        assertEquals(32, block.getWidth());
        assertEquals(32, block.getHeight());
    }
}
