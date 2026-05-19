package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.worldEntity.Coin;
import org.junit.jupiter.api.Test;

class BasicLevelLoaderTest {

    private final LevelLoader loader = new BasicLevelLoader();

    @Test
    void loadsLevelWithMetadataAndPlayerAtSpawn() {
        final Level level = loader.loadLevel(2);

        assertEquals(2, level.getLevelNumber());
        assertEquals(2400, level.getWidth());
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
        assertEquals(2000, level.getWidth());
    }

    @Test
    void availableLevelsHaveDifferentLayouts() {
        final Level firstLevel = loader.loadLevel(1);
        final Level secondLevel = loader.loadLevel(2);
        final Level thirdLevel = loader.loadLevel(3);

        assertTrue(firstLevel.getEntities().size() < secondLevel.getEntities().size());
        assertTrue(secondLevel.getEntities().size() < thirdLevel.getEntities().size());
        assertEquals(1, countCoins(firstLevel));
        assertEquals(3, countCoins(secondLevel));
        assertEquals(4, countCoins(thirdLevel));
    }

    private long countCoins(final Level level) {
        return level.getEntities().stream()
            .filter(Coin.class::isInstance)
            .count();
    }
}
