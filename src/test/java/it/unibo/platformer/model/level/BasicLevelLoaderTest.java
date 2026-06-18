package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.AbstractStaticEntity;
import it.unibo.platformer.model.entities.enemies.Enemy;
import it.unibo.platformer.model.entities.world.Coin;
import it.unibo.platformer.model.entities.world.Flag;
import it.unibo.platformer.model.entities.world.Pole;
import java.util.List;
import org.junit.jupiter.api.Test;

class BasicLevelLoaderTest {

    private static final int FIRST_LEVEL = 1;
    private static final int SECOND_LEVEL = 2;
    private static final int THIRD_LEVEL = 3;
    private static final int UNKNOWN_LEVEL = 99;
    private static final double LEVEL_ONE_WIDTH = 3800.0;
    private static final double LEVEL_TWO_WIDTH = 5400.0;
    private static final double SCREEN_HEIGHT = 720.0;
    private static final double PLAYER_SPAWN_X = 100.0;
    private static final double PLAYER_SPAWN_Y = 300.0;
    private static final long MIN_LEVEL_ONE_COINS = 10;
    private static final long MIN_LEVEL_TWO_COINS = 25;
    private static final long MIN_LEVEL_THREE_COINS = 35;
    private static final double EPSILON = 0.001;
    private static final double TILE_SIZE = 32.0;
    private static final double BIG_PLAYER_WIDTH = 16.0;
    private static final double BIG_PLAYER_HEIGHT = 48.0;
    private static final double MIN_VERTICAL_CLEARANCE = BIG_PLAYER_HEIGHT * 2.0;
    private static final double MIN_ENEMY_WALKABLE_WIDTH = TILE_SIZE * 3.0;
    private static final int FIRST_INDEX = 0;
    private static final int NEXT_INDEX_OFFSET = 1;
    private static final String Y_COORDINATE_MESSAGE = ", y=";

    private final LevelLoader loader = new BasicLevelLoader();

    @Test
    void loadsLevelWithMetadataAndPlayerAtSpawn() {
        final Level level = loader.loadLevel(SECOND_LEVEL);

        assertEquals(SECOND_LEVEL, level.getLevelNumber());
        assertEquals(LEVEL_TWO_WIDTH, level.getWidth());
        assertEquals(SCREEN_HEIGHT, level.getHeight());
        assertEquals(PLAYER_SPAWN_X, level.getSpawnX());
        assertEquals(PLAYER_SPAWN_Y, level.getSpawnY());
        assertNotNull(level.getPlayer());
        assertEquals(level.getSpawnX(), level.getPlayer().getX());
        assertEquals(level.getSpawnY(), level.getPlayer().getY());
    }

    @Test
    void unknownLevelFallsBackToFirstLevel() {
        final Level level = loader.loadLevel(UNKNOWN_LEVEL);

        assertEquals(FIRST_LEVEL, level.getLevelNumber());
        assertEquals(LEVEL_ONE_WIDTH, level.getWidth());
    }

    @Test
    void availableLevelsHaveDifferentLayouts() {
        final Level firstLevel = loader.loadLevel(FIRST_LEVEL);
        final Level secondLevel = loader.loadLevel(SECOND_LEVEL);
        final Level thirdLevel = loader.loadLevel(THIRD_LEVEL);

        assertTrue(firstLevel.getEntities().size() < secondLevel.getEntities().size());
        assertTrue(secondLevel.getEntities().size() < thirdLevel.getEntities().size());
        assertTrue(countCoins(firstLevel) >= MIN_LEVEL_ONE_COINS);
        assertTrue(countCoins(secondLevel) >= MIN_LEVEL_TWO_COINS);
        assertTrue(countCoins(thirdLevel) >= MIN_LEVEL_THREE_COINS);
    }

    @Test
    void enemiesStartOnSolidGround() {
        for (int levelNumber = FIRST_LEVEL; levelNumber <= THIRD_LEVEL; levelNumber++) {
            final Level level = loader.loadLevel(levelNumber);
            for (final AbstractEntity entity : level.getEntities()) {
                if (entity instanceof Enemy) {
                    assertTrue(
                        hasSolidGroundBelow(level, entity),
                        "Enemy has no ground below in level "
                            + levelNumber
                            + " at x="
                            + entity.getX()
                            + Y_COORDINATE_MESSAGE
                            + entity.getY()
                    );
                }
            }
        }
    }

    @Test
    void solidBlocksDoNotOverlap() {
        for (int levelNumber = FIRST_LEVEL; levelNumber <= THIRD_LEVEL; levelNumber++) {
            final Level level = loader.loadLevel(levelNumber);
            final List<AbstractStaticEntity> solidBlocks = solidBlocks(level);

            for (int i = FIRST_INDEX; i < solidBlocks.size(); i++) {
                for (int j = i + NEXT_INDEX_OFFSET; j < solidBlocks.size(); j++) {
                    final AbstractStaticEntity first = solidBlocks.get(i);
                    final AbstractStaticEntity second = solidBlocks.get(j);
                    assertTrue(
                        !overlaps(first, second),
                        "Overlapping blocks in level "
                            + levelNumber
                            + " at first x="
                            + first.getX()
                            + Y_COORDINATE_MESSAGE
                            + first.getY()
                            + " and second x="
                            + second.getX()
                            + Y_COORDINATE_MESSAGE
                            + second.getY()
                    );
                }
            }
        }
    }

    @Test
    void stackedPlayablePlatformsLeaveRoomForBigMario() {
        for (int levelNumber = FIRST_LEVEL; levelNumber <= THIRD_LEVEL; levelNumber++) {
            final Level level = loader.loadLevel(levelNumber);
            final List<AbstractStaticEntity> solidBlocks = solidBlocks(level);

            for (final AbstractStaticEntity lowerBlock : solidBlocks) {
                for (final AbstractStaticEntity upperBlock : solidBlocks) {
                    final double clearance = lowerBlock.getY() - (upperBlock.getY() + upperBlock.getHeight());
                    if (clearance > EPSILON
                        && !hasSolidBlockDirectlyBelow(solidBlocks, upperBlock)
                        && horizontalOverlap(lowerBlock, upperBlock) >= BIG_PLAYER_WIDTH) {
                        assertTrue(
                            clearance >= MIN_VERTICAL_CLEARANCE,
                            "Low ceiling in level "
                                + levelNumber
                                + " between lower x="
                                + lowerBlock.getX()
                                + Y_COORDINATE_MESSAGE
                                + lowerBlock.getY()
                                + " and upper x="
                                + upperBlock.getX()
                                + Y_COORDINATE_MESSAGE
                                + upperBlock.getY()
                        );
                    }
                }
            }
        }
    }

    @Test
    void enemiesHaveEnoughFlatSpaceToMove() {
        for (int levelNumber = FIRST_LEVEL; levelNumber <= THIRD_LEVEL; levelNumber++) {
            final Level level = loader.loadLevel(levelNumber);
            for (final AbstractEntity entity : level.getEntities()) {
                if (entity instanceof Enemy) {
                    assertTrue(
                        walkableWidthBelow(level, entity) >= MIN_ENEMY_WALKABLE_WIDTH,
                        "Enemy has too little walking space in level "
                            + levelNumber
                            + " at x="
                            + entity.getX()
                            + Y_COORDINATE_MESSAGE
                            + entity.getY()
                    );
                }
            }
        }
    }

    @Test
    void levelsContainGoalPoleAndFlag() {
        for (int levelNumber = FIRST_LEVEL; levelNumber <= THIRD_LEVEL; levelNumber++) {
            final Level level = loader.loadLevel(levelNumber);

            assertTrue(level.getEntities().stream().anyMatch(Pole.class::isInstance));
            assertTrue(level.getEntities().stream().anyMatch(Flag.class::isInstance));
        }
    }

    private List<AbstractStaticEntity> solidBlocks(final Level level) {
        return level.getEntities().stream()
            .filter(AbstractStaticEntity.class::isInstance)
            .map(AbstractStaticEntity.class::cast)
            .filter(AbstractStaticEntity::isSolid)
            .toList();
    }

    private long countCoins(final Level level) {
        return level.getEntities().stream()
            .filter(Coin.class::isInstance)
            .count();
    }

    private boolean hasSolidGroundBelow(final Level level, final AbstractEntity enemy) {
        final double enemyBottom = enemy.getY() + enemy.getHeight();
        return level.getEntities().stream()
            .filter(AbstractStaticEntity.class::isInstance)
            .map(AbstractStaticEntity.class::cast)
            .anyMatch(block -> block.isSolid()
                && Math.abs(block.getY() - enemyBottom) < EPSILON
                && horizontalOverlap(enemy, block) > EPSILON);
    }

    private double walkableWidthBelow(final Level level, final AbstractEntity enemy) {
        final double enemyBottom = enemy.getY() + enemy.getHeight();
        final List<AbstractStaticEntity> sameRow = solidBlocks(level).stream()
            .filter(block -> Math.abs(block.getY() - enemyBottom) < EPSILON)
            .sorted((first, second) -> Double.compare(first.getX(), second.getX()))
            .toList();

        double currentStart = enemy.getX();
        double currentEnd = enemy.getX();
        for (final AbstractStaticEntity block : sameRow) {
            if (currentEnd <= currentStart) {
                currentStart = block.getX();
                currentEnd = block.getX() + block.getWidth();
            } else if (block.getX() <= currentEnd + EPSILON) {
                currentEnd = Math.max(currentEnd, block.getX() + block.getWidth());
            } else {
                if (enemyStandsOn(enemy, currentStart, currentEnd)) {
                    return currentEnd - currentStart;
                }
                currentStart = block.getX();
                currentEnd = block.getX() + block.getWidth();
            }
        }
        if (enemyStandsOn(enemy, currentStart, currentEnd)) {
            return currentEnd - currentStart;
        }
        return EPSILON;
    }

    private boolean enemyStandsOn(final AbstractEntity enemy, final double startX, final double endX) {
        return enemy.getX() < endX && enemy.getX() + enemy.getWidth() > startX;
    }

    private boolean hasSolidBlockDirectlyBelow(
        final List<AbstractStaticEntity> solidBlocks,
        final AbstractStaticEntity upperBlock
    ) {
        return solidBlocks.stream().anyMatch(block -> block != upperBlock
            && Math.abs(block.getY() - (upperBlock.getY() + upperBlock.getHeight())) < EPSILON
            && horizontalOverlap(block, upperBlock) > EPSILON);
    }

    private boolean overlaps(final AbstractEntity first, final AbstractEntity second) {
        return first.getX() < second.getX() + second.getWidth()
            && first.getX() + first.getWidth() > second.getX()
            && first.getY() < second.getY() + second.getHeight()
            && first.getY() + first.getHeight() > second.getY();
    }

    private double horizontalOverlap(final AbstractEntity first, final AbstractEntity second) {
        return Math.min(first.getX() + first.getWidth(), second.getX() + second.getWidth())
            - Math.max(first.getX(), second.getX());
    }
}
