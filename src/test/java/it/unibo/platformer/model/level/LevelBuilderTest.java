package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.world.Flag;
import it.unibo.platformer.model.entities.world.Pole;
import org.junit.jupiter.api.Test;

class LevelBuilderTest {

    private static final int FIRST_LEVEL = 1;
    private static final double FIRST_LEVEL_WIDTH = 3800.0;

    @Test
    void buildsLevelFromDefinitionWithPlayerAndGoal() {
        final LevelBuilder builder = new LevelBuilder();
        final BasicLevel level = builder.build(new LevelOneDefinition());

        assertEquals(FIRST_LEVEL, level.getLevelNumber());
        assertEquals(FIRST_LEVEL_WIDTH, level.getWidth());
        assertNotNull(level.getPlayer());
        assertTrue(level.getEntities().stream().anyMatch(Pole.class::isInstance));
        assertTrue(level.getEntities().stream().anyMatch(Flag.class::isInstance));
    }

    @Test
    void definitionsUseDifferentNumbersAndWidths() {
        final LevelDefinition first = new LevelOneDefinition();
        final LevelDefinition second = new LevelTwoDefinition();
        final LevelDefinition third = new LevelThreeDefinition();

        assertTrue(first.getLevelNumber() < second.getLevelNumber());
        assertTrue(second.getLevelNumber() < third.getLevelNumber());
        assertTrue(first.getWidth() < second.getWidth());
        assertTrue(second.getWidth() < third.getWidth());
    }
}
