package it.unibo.platformer.model.level;

/**
 * Selects and builds the playable levels used by the game.
 */
public final class BasicLevelLoader implements LevelLoader {

    private static final int LEVEL_TWO = 2;
    private static final int LEVEL_THREE = 3;

    private final LevelDefinition firstLevel;
    private final LevelDefinition secondLevel;
    private final LevelDefinition thirdLevel;
    private final LevelBuilder builder;

    /**
     * Creates a loader for the three available levels.
     */
    public BasicLevelLoader() {
        this.firstLevel = new LevelOneDefinition();
        this.secondLevel = new LevelTwoDefinition();
        this.thirdLevel = new LevelThreeDefinition();
        this.builder = new LevelBuilder();
    }

    @Override
    public Level loadLevel(final int levelNumber) {
        switch (levelNumber) {
            case LEVEL_TWO:
                return this.builder.build(this.secondLevel);
            case LEVEL_THREE:
                return this.builder.build(this.thirdLevel);
            default:
                return this.builder.build(this.firstLevel);
        }
    }
}
