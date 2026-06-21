package it.unibo.platformer.model.level;

/**
 * Layout data for the second level.
 */
final class LevelTwoDefinition implements LevelDefinition {

    private static final int LEVEL_NUMBER = 2;
    private static final int LEVEL_WIDTH = 5400;
    private static final int[][] GROUND = {
        {0, 640}, {850, 1420}, {1620, 2160}, {2380, 3040}, {3320, 4040}, {4300, LEVEL_WIDTH},
    };
    private static final int[][] QUESTION_PAIRS = {{420, 350}, {2380, 350}, {4300, 350}, {5200, 350}};
    private static final int[][] BRIDGES = {{670, 390, 2}, {2180, 380, 2}, {3060, 370, 3}};
    private static final int[][] COIN_ARCS = {{650, 340, 3}};
    private static final int[][] SHAFTS = {{2500, 6}};
    private static final int[][] TUNNELS = {{1180, 260, 7}};
    private static final int[][] CLIMBS = {{1700, 360, 4}, {3650, 360, 4}};
    private static final int[][] PYRAMIDS = {{120, 4}, {4950, 4}};
    private static final int[][] ISLANDS = {{900, 280, 7}, {2800, 260, 8}, {4500, 250, 8}};
    private static final int[][] PLATFORM_KOOPAS = {{1000, 280}, {4620, 250}};
    private static final int[][] ENEMY_PAIRS = {{430}, {1850}};
    private static final int[][] ENEMY_GAUNTLETS = {{3420, 3}};
    private static final int[] GOOMBAS = {3900};
    private static final int[] KOOPAS = {4750};

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
        builder.addQuestionPairs(level, QUESTION_PAIRS);
        builder.addBridges(level, BRIDGES);
        builder.addCoinArcs(level, COIN_ARCS);
        builder.addWallJumpShafts(level, SHAFTS);
        builder.addSkyTunnels(level, TUNNELS);
        builder.addSkyClimbs(level, CLIMBS);
        builder.addPyramids(level, PYRAMIDS);
        builder.addFloatingIslands(level, ISLANDS);
        builder.addPlatformKoopas(level, PLATFORM_KOOPAS);
        builder.addEnemyPairs(level, ENEMY_PAIRS);
        builder.addEnemyGauntlets(level, ENEMY_GAUNTLETS);
        builder.addGoombas(level, GOOMBAS);
        builder.addKoopas(level, KOOPAS);
    }
}
