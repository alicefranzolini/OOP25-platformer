package it.unibo.platformer.model.level;

/**
 * Layout data for the first level.
 */
final class LevelOneDefinition implements LevelDefinition {

    private static final int LEVEL_NUMBER = 1;
    private static final int LEVEL_WIDTH = 3800;
    private static final int[][] GROUND = {
        {0, 700}, {880, 1500}, {1680, 2320}, {2520, 3180}, {3360, LEVEL_WIDTH},
    };
    private static final int[][] BLOCK_ROWS = {{300, 360, 3}};
    private static final int[][] QUESTION_PAIRS = {{520, 350}, {1420, 350}, {3100, 350}};
    private static final int[][] BRIDGES = {{720, 390, 2}, {2340, 380, 2}};
    private static final int[][] COIN_ARCS = {{690, 340, 4}};
    private static final int[][] CLIMBS = {{1750, 360, 4}};
    private static final int[][] PYRAMIDS = {{120, 3}, {3400, 5}};
    private static final int[][] ISLANDS = {{980, 270, 7}, {2700, 250, 7}};
    private static final int[][] PLATFORM_GOOMBAS = {{1080, 270}};
    private static final int[][] ENEMY_PAIRS = {{450}, {1900}};
    private static final int[][] ENEMY_GAUNTLETS = {{2860, 3}};

    @Override
    public int getLevelNumber() {
        return LEVEL_NUMBER;
    }

    @Override
    public double getWidth() {
        return LEVEL_WIDTH;
    }

    @Override
    public void build(final BasicLevel level, final LevelBuilder builder) {
        builder.addGroundSegments(level, GROUND);
        builder.addBlockRows(level, BLOCK_ROWS);
        builder.addQuestionPairs(level, QUESTION_PAIRS);
        builder.addBridges(level, BRIDGES);
        builder.addCoinArcs(level, COIN_ARCS);
        builder.addSkyClimbs(level, CLIMBS);
        builder.addPyramids(level, PYRAMIDS);
        builder.addFloatingIslands(level, ISLANDS);
        builder.addPlatformGoombas(level, PLATFORM_GOOMBAS);
        builder.addEnemyPairs(level, ENEMY_PAIRS);
        builder.addEnemyGauntlets(level, ENEMY_GAUNTLETS);
    }
}
