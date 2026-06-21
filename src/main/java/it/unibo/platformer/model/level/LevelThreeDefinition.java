package it.unibo.platformer.model.level;

/**
 * Layout data for the third level.
 */
final class LevelThreeDefinition implements LevelDefinition {

    private static final int LEVEL_NUMBER = 3;
    private static final int LEVEL_WIDTH = 7000;
    private static final int[][] GROUND = {
        {0, 600}, {840, 1280}, {1540, 2100}, {2360, 2960}, {3240, 3880}, {4160, 4840},
        {5120, LEVEL_WIDTH},
    };
    private static final int[][] QUESTION_PAIRS = {
        {360, 350}, {2000, 350}, {3800, 350}, {4500, 350}, {6400, 350},
    };
    private static final int[][] BRIDGES = {
        {620, 390, 2}, {1300, 380, 2}, {2120, 380, 2}, {2980, 390, 2}, {3900, 370, 2},
        {4860, 410, 2},
    };
    private static final int[][] SHAFTS = {{900, 7}, {5200, 7}};
    private static final int[][] TUNNELS = {{2600, 260, 8}, {4700, 240, 8}};
    private static final int[][] CLIMBS = {{1600, 360, 4}, {3300, 360, 4}, {5600, 360, 3}};
    private static final int[][] PYRAMIDS = {{140, 4}, {6500, 5}};
    private static final int[][] ISLANDS = {{4200, 250, 8}, {6100, 230, 8}};
    private static final int[][] PLATFORM_GOOMBAS = {{4300, 250}};
    private static final int[][] PLATFORM_KOOPAS = {{6200, 230}};
    private static final int[][] ENEMY_PAIRS = {{420}, {1750}, {3400}};
    private static final int[][] ENEMY_GAUNTLETS = {
        {2650, 3}, {4500, 3},
    };

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
        builder.addWallJumpShafts(level, SHAFTS);
        builder.addSkyTunnels(level, TUNNELS);
        builder.addSkyClimbs(level, CLIMBS);
        builder.addPyramids(level, PYRAMIDS);
        builder.addFloatingIslands(level, ISLANDS);
        builder.addPlatformGoombas(level, PLATFORM_GOOMBAS);
        builder.addPlatformKoopas(level, PLATFORM_KOOPAS);
        builder.addEnemyPairs(level, ENEMY_PAIRS);
        builder.addEnemyGauntlets(level, ENEMY_GAUNTLETS);
    }
}
