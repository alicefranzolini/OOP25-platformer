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
import org.junit.jupiter.api.Test;

class BasicLevelLoaderTest {

    private final LevelLoader loader = new BasicLevelLoader();

    @Test
    void loadsLevelWithMetadataAndPlayerAtSpawn() {
        final Level level = loader.loadLevel(2);

        assertEquals(2, level.getLevelNumber());
        assertEquals(5400, level.getWidth());
        assertEquals(720, level.getHeight());
        assertEquals(100, level.getSpawnX());
        assertEquals(300, level.getSpawnY());
        assertNotNull(level.getPlayer());
        assertEquals(level.getSpawnX(), level.getPlayer().getX());
        assertEquals(level.getSpawnY(), level.getPlayer().getY());
    }

    @Test
    void unknownLevelFallsBackToFirstLevel() {
        final Level level = loader.loadLevel(99);

        assertEquals(1, level.getLevelNumber());
        assertEquals(3800, level.getWidth());
    }

    @Test
    void availableLevelsHaveDifferentLayouts() {
        final Level firstLevel = loader.loadLevel(1);
        final Level secondLevel = loader.loadLevel(2);
        final Level thirdLevel = loader.loadLevel(3);

        assertTrue(firstLevel.getEntities().size() < secondLevel.getEntities().size());
        assertTrue(secondLevel.getEntities().size() < thirdLevel.getEntities().size());
        assertTrue(countCoins(firstLevel) >= 10);
        assertTrue(countCoins(secondLevel) >= 25);
        assertTrue(countCoins(thirdLevel) >= 35);
    }

    @Test
    void enemiesStartOnSolidGround() {
        for (int levelNumber = 1; levelNumber <= 3; levelNumber++) {
            final Level level = loader.loadLevel(levelNumber);
            for (final AbstractEntity entity : level.getEntities()) {
                if (entity instanceof Enemy) {
                    assertTrue(
                        hasSolidGroundBelow(level, entity),
                        "Enemy has no ground below in level "
                            + levelNumber
                            + " at x="
                            + entity.getX()
                            + ", y="
                            + entity.getY()
                    );
                }
            }
        }
    }

    @Test
    void levelsContainGoalPoleAndFlag() {
        for (int levelNumber = 1; levelNumber <= 3; levelNumber++) {
            final Level level = loader.loadLevel(levelNumber);

            assertTrue(level.getEntities().stream().anyMatch(Pole.class::isInstance));
            assertTrue(level.getEntities().stream().anyMatch(Flag.class::isInstance));
        }
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
                && Math.abs(block.getY() - enemyBottom) < 0.001
                && horizontalOverlap(enemy, block) > 0);
    }

    private double horizontalOverlap(final AbstractEntity first, final AbstractEntity second) {
        return Math.min(first.getX() + first.getWidth(), second.getX() + second.getWidth())
            - Math.max(first.getX(), second.getX());
    }
}
