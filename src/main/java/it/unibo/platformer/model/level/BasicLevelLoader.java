package it.unibo.platformer.model.level;

import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.entities.enemies.Koopa;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.entities.world.Block.BlockType;
import it.unibo.platformer.model.entities.world.Coin;
import it.unibo.platformer.model.entities.world.Flag;
import it.unibo.platformer.model.entities.world.Pole;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;

/**
 * Builds the three playable levels used by the game.
 */
public final class BasicLevelLoader implements LevelLoader {

    private static final int LEVEL_ONE = 1;
    private static final int LEVEL_TWO = 2;
    private static final int LEVEL_THREE = 3;
    private static final int TILE_SIZE = 32;
    private static final int DEFAULT_ARRAY_SIZE = 0;
    private static final int X_INDEX = 0;
    private static final int Y_INDEX = 1;
    private static final int COUNT_INDEX = 2;
    private static final int START_X_INDEX = 0;
    private static final int END_X_INDEX = 1;
    private static final int STEPS_INDEX = 1;
    private static final int FIRST_LOOP_INDEX = 0;
    private static final int FIRST_INNER_COIN_INDEX = 1;
    private static final int SECOND_ITEM_OFFSET = 1;
    private static final int THIRD_ITEM_OFFSET = 2;
    private static final int EVEN_DIVISOR = 2;
    private static final int KOOPA_GAUNTLET_SLOT = 1;
    private static final int KOOPA_GAUNTLET_SPACING = 3;
    private static final int PYRAMID_WIDTH_MULTIPLIER = 2;
    private static final int QUESTION_MIN_ISLAND_BLOCKS = 5;
    private static final int QUESTION_CLEARANCE_TILES = 4;
    private static final int SKY_TUNNEL_CLEARANCE_TILES = 4;
    private static final int WALL_SHAFT_WIDTH_TILES = 3;
    private static final int WALL_SHAFT_EXIT_BLOCKS = 4;
    private static final int BRIDGE_PLATFORM_BLOCKS = 2;
    private static final int BRIDGE_X_STEP = 120;
    private static final int BRIDGE_Y_STEP = 40;
    private static final int SKY_CLIMB_X_STEP = 170;
    private static final int SKY_CLIMB_Y_STEP = 80;
    private static final int SKY_CLIMB_MIN_Y = 145;
    private static final int SKY_CLIMB_BLOCKS = 3;
    private static final int SKY_CLIMB_COIN_ONE_X_OFFSET = 22;
    private static final int SKY_CLIMB_COIN_TWO_X_OFFSET = 62;
    private static final int SKY_CLIMB_COIN_Y_OFFSET = 36;
    private static final int SKY_CLIMB_QUESTION_PERIOD = 3;
    private static final int SKY_CLIMB_QUESTION_SLOT = 2;
    private static final int SKY_TUNNEL_COIN_X_OFFSET = 8;
    private static final int SKY_TUNNEL_COIN_Y_OFFSET = 50;
    private static final int PYRAMID_COIN_X_OFFSET = 20;
    private static final int PYRAMID_EXTRA_COIN_COUNT = 3;
    private static final int FLOATING_ISLAND_COIN_X_OFFSET = 20;
    private static final int FLOATING_ISLAND_COIN_Y_OFFSET = 45;
    private static final int COIN_LINE_SPACING = 40;
    private static final int VERTICAL_COIN_SPACING = 38;
    private static final int COIN_ARC_SPACING = 42;
    private static final int COIN_ARC_Y_STEP = 18;
    private static final int ENEMY_PAIR_SPACING = 150;
    private static final int ENEMY_GAUNTLET_SPACING = 135;
    private static final int GOOMBA_HEIGHT = 32;
    private static final int KOOPA_HEIGHT = 48;
    private static final double SCREEN_HEIGHT = 720;
    private static final double PLAYER_SPAWN_X = 100;
    private static final double PLAYER_SPAWN_Y = 300;
    private static final double FLOOR_Y = 500;
    private static final double GOOMBA_Y = FLOOR_Y - GOOMBA_HEIGHT;
    private static final double KOOPA_Y = FLOOR_Y - KOOPA_HEIGHT;
    private static final double GOAL_DISTANCE_FROM_END = 96;
    private static final double GOAL_POLE_HEIGHT = 220;
    private static final int LEVEL_ONE_WIDTH = 3800;
    private static final int LEVEL_TWO_WIDTH = 5400;
    private static final int LEVEL_THREE_WIDTH = 7000;

    private static final int[][] LEVEL_ONE_GROUND = {
        {0, 700}, {880, 1500}, {1680, 2320}, {2520, 3180}, {3360, LEVEL_ONE_WIDTH},
    };
    private static final int[][] LEVEL_TWO_GROUND = {
        {0, 640}, {850, 1420}, {1620, 2160}, {2380, 3040}, {3320, 4040}, {4300, LEVEL_TWO_WIDTH},
    };
    private static final int[][] LEVEL_THREE_GROUND = {
        {0, 600}, {840, 1280}, {1540, 2100}, {2360, 2960}, {3240, 3880}, {4160, 4840},
        {5120, LEVEL_THREE_WIDTH},
    };

    private static final int[][] LEVEL_ONE_BLOCK_ROWS = {{300, 360, 3}};
    private static final int[][] LEVEL_ONE_QUESTION_PAIRS = {{500, 350}, {1520, 210}, {3220, 240}};
    private static final int[][] LEVEL_ONE_BRIDGES = {{720, 390, 2}, {2340, 380, 2}};
    private static final int[][] LEVEL_ONE_COIN_ARCS = {{690, 340, 5}};
    private static final int[][] LEVEL_ONE_SHAFTS = {{950, 7}, {2640, 6}};
    private static final int[][] LEVEL_ONE_TUNNELS = {{1240, 270, 8}, {2940, 300, 7}};
    private static final int[][] LEVEL_ONE_CLIMBS = {{1600, 360, 5}};
    private static final int[][] LEVEL_ONE_COIN_LINES = {
        {1260, 220, 5}, {900, 455, 4}, {1710, 300, 5}, {2960, 250, 5},
    };
    private static final int[][] LEVEL_ONE_VERTICAL_COINS = {{1030, 440, 5}, {3490, 430, 4}};
    private static final int[][] LEVEL_ONE_PYRAMIDS = {{120, 3}, {3400, 5}};
    private static final int[][] LEVEL_ONE_ISLANDS = {{620, 220, 8}};
    private static final int[][] LEVEL_ONE_PLATFORM_GOOMBAS = {{700, 220}};
    private static final int[][] LEVEL_ONE_PLATFORM_KOOPAS = new int[DEFAULT_ARRAY_SIZE][DEFAULT_ARRAY_SIZE];
    private static final int[][] LEVEL_ONE_ENEMY_PAIRS = {{500}, {1230}, {1850}};
    private static final int[][] LEVEL_ONE_ENEMY_GAUNTLETS = {{2860, 3}};
    private static final int[] LEVEL_ONE_GOOMBAS = new int[DEFAULT_ARRAY_SIZE];
    private static final int[] LEVEL_ONE_KOOPAS = new int[DEFAULT_ARRAY_SIZE];

    private static final int[][] LEVEL_TWO_BLOCK_ROWS = new int[DEFAULT_ARRAY_SIZE][DEFAULT_ARRAY_SIZE];
    private static final int[][] LEVEL_TWO_QUESTION_PAIRS = {{420, 360}, {3180, 190}, {4920, 310}};
    private static final int[][] LEVEL_TWO_BRIDGES = {{670, 390, 2}, {2180, 380, 2}, {3060, 370, 3}};
    private static final int[][] LEVEL_TWO_COIN_ARCS = {{650, 345, 5}};
    private static final int[][] LEVEL_TWO_SHAFTS = {{920, 8}, {2460, 8}, {3820, 9}};
    private static final int[][] LEVEL_TWO_TUNNELS = {{1220, 240, 9}, {2760, 230, 10}, {4140, 210, 9}};
    private static final int[][] LEVEL_TWO_CLIMBS = {{1500, 360, 6}, {3380, 360, 3}};
    private static final int[][] LEVEL_TWO_COIN_LINES = {
        {1230, 190, 6}, {2780, 180, 7}, {4160, 160, 6}, {4560, 300, 6},
    };
    private static final int[][] LEVEL_TWO_VERTICAL_COINS = {{1010, 440, 7}, {2560, 440, 7}};
    private static final int[][] LEVEL_TWO_PYRAMIDS = {{120, 4}, {4300, 6}};
    private static final int[][] LEVEL_TWO_ISLANDS = {{4550, 245, 9}};
    private static final int[][] LEVEL_TWO_PLATFORM_GOOMBAS = new int[DEFAULT_ARRAY_SIZE][DEFAULT_ARRAY_SIZE];
    private static final int[][] LEVEL_TWO_PLATFORM_KOOPAS = {{4670, 245}};
    private static final int[][] LEVEL_TWO_ENEMY_PAIRS = {{430}};
    private static final int[][] LEVEL_TWO_ENEMY_GAUNTLETS = {{1180, 2}, {1680, 3}, {2700, 3}, {3420, 3}};
    private static final int[] LEVEL_TWO_GOOMBAS = {4890};
    private static final int[] LEVEL_TWO_KOOPAS = {4740, 5100};

    private static final int[][] LEVEL_THREE_BLOCK_ROWS = new int[DEFAULT_ARRAY_SIZE][DEFAULT_ARRAY_SIZE];
    private static final int[][] LEVEL_THREE_QUESTION_PAIRS = {
        {360, 360}, {1540, 170}, {3060, 150}, {4960, 150}, {6580, 300},
    };
    private static final int[][] LEVEL_THREE_BRIDGES = {{630, 390, 2}, {2120, 370, 2}, {3900, 360, 3}};
    private static final int[][] LEVEL_THREE_COIN_ARCS = new int[DEFAULT_ARRAY_SIZE][DEFAULT_ARRAY_SIZE];
    private static final int[][] LEVEL_THREE_SHAFTS = {{900, 9}, {2420, 10}, {4240, 10}, {5200, 9}};
    private static final int[][] LEVEL_THREE_TUNNELS = {{1180, 210, 10}, {2720, 185, 11}, {4580, 175, 11}};
    private static final int[][] LEVEL_THREE_CLIMBS = {{1500, 360, 5}, {3260, 360, 2}, {5450, 360, 3}};
    private static final int[][] LEVEL_THREE_COIN_LINES = {
        {1130, 160, 7}, {2660, 140, 8}, {4480, 130, 8}, {5480, 260, 8}, {6260, 290, 6},
    };
    private static final int[][] LEVEL_THREE_VERTICAL_COINS = {{1000, 440, 8}, {2540, 440, 8}, {4360, 440, 8}};
    private static final int[][] LEVEL_THREE_PYRAMIDS = {{140, 4}, {6040, 7}};
    private static final int[][] LEVEL_THREE_ISLANDS = {{3640, 245, 8}, {6000, 145, 8}};
    private static final int[][] LEVEL_THREE_PLATFORM_GOOMBAS = {{3710, 245}};
    private static final int[][] LEVEL_THREE_PLATFORM_KOOPAS = {{3830, 245}, {6120, 145}};
    private static final int[][] LEVEL_THREE_ENEMY_PAIRS = {{400}};
    private static final int[][] LEVEL_THREE_ENEMY_GAUNTLETS = {
        {1130, 2}, {1640, 4}, {2700, 2}, {3360, 4}, {4540, 2}, {5600, 3},
    };
    private static final int[] LEVEL_THREE_GOOMBAS = {6660};
    private static final int[] LEVEL_THREE_KOOPAS = {6500};

    @Override
    public Level loadLevel(final int levelNumber) {
        switch (levelNumber) {
            case LEVEL_ONE:
                return loadLevelOne();
            case LEVEL_TWO:
                return loadLevelTwo();
            case LEVEL_THREE:
                return loadLevelThree();
            default:
                return loadLevelOne();
        }
    }

    private Level loadLevelOne() {
        final BasicLevel level = createLevel(LEVEL_ONE, LEVEL_ONE_WIDTH);

        buildLevel(
            level,
            LEVEL_ONE_GROUND,
            LEVEL_ONE_BLOCK_ROWS,
            LEVEL_ONE_QUESTION_PAIRS,
            LEVEL_ONE_BRIDGES,
            LEVEL_ONE_COIN_ARCS,
            LEVEL_ONE_SHAFTS,
            LEVEL_ONE_TUNNELS,
            LEVEL_ONE_CLIMBS,
            LEVEL_ONE_COIN_LINES,
            LEVEL_ONE_VERTICAL_COINS,
            LEVEL_ONE_PYRAMIDS,
            LEVEL_ONE_ISLANDS,
            LEVEL_ONE_PLATFORM_GOOMBAS,
            LEVEL_ONE_PLATFORM_KOOPAS,
            LEVEL_ONE_ENEMY_PAIRS,
            LEVEL_ONE_ENEMY_GAUNTLETS,
            LEVEL_ONE_GOOMBAS,
            LEVEL_ONE_KOOPAS
        );

        addGoal(level);

        return level;
    }

    private Level loadLevelTwo() {
        final BasicLevel level = createLevel(LEVEL_TWO, LEVEL_TWO_WIDTH);

        buildLevel(
            level,
            LEVEL_TWO_GROUND,
            LEVEL_TWO_BLOCK_ROWS,
            LEVEL_TWO_QUESTION_PAIRS,
            LEVEL_TWO_BRIDGES,
            LEVEL_TWO_COIN_ARCS,
            LEVEL_TWO_SHAFTS,
            LEVEL_TWO_TUNNELS,
            LEVEL_TWO_CLIMBS,
            LEVEL_TWO_COIN_LINES,
            LEVEL_TWO_VERTICAL_COINS,
            LEVEL_TWO_PYRAMIDS,
            LEVEL_TWO_ISLANDS,
            LEVEL_TWO_PLATFORM_GOOMBAS,
            LEVEL_TWO_PLATFORM_KOOPAS,
            LEVEL_TWO_ENEMY_PAIRS,
            LEVEL_TWO_ENEMY_GAUNTLETS,
            LEVEL_TWO_GOOMBAS,
            LEVEL_TWO_KOOPAS
        );

        addGoal(level);

        return level;
    }

    private Level loadLevelThree() {
        final BasicLevel level = createLevel(LEVEL_THREE, LEVEL_THREE_WIDTH);

        buildLevel(
            level,
            LEVEL_THREE_GROUND,
            LEVEL_THREE_BLOCK_ROWS,
            LEVEL_THREE_QUESTION_PAIRS,
            LEVEL_THREE_BRIDGES,
            LEVEL_THREE_COIN_ARCS,
            LEVEL_THREE_SHAFTS,
            LEVEL_THREE_TUNNELS,
            LEVEL_THREE_CLIMBS,
            LEVEL_THREE_COIN_LINES,
            LEVEL_THREE_VERTICAL_COINS,
            LEVEL_THREE_PYRAMIDS,
            LEVEL_THREE_ISLANDS,
            LEVEL_THREE_PLATFORM_GOOMBAS,
            LEVEL_THREE_PLATFORM_KOOPAS,
            LEVEL_THREE_ENEMY_PAIRS,
            LEVEL_THREE_ENEMY_GAUNTLETS,
            LEVEL_THREE_GOOMBAS,
            LEVEL_THREE_KOOPAS
        );

        addGoal(level);

        return level;
    }

    private void buildLevel(
        final BasicLevel level,
        final int[][] groundSegments,
        final int[][] blockRows,
        final int[][] questionPairs,
        final int[][] bridges,
        final int[][] coinArcs,
        final int[][] wallJumpShafts,
        final int[][] skyTunnels,
        final int[][] skyClimbs,
        final int[][] coinLines,
        final int[][] verticalCoinLines,
        final int[][] pyramids,
        final int[][] floatingIslands,
        final int[][] platformGoombas,
        final int[][] platformKoopas,
        final int[][] enemyPairs,
        final int[][] enemyGauntlets,
        final int[] goombas,
        final int[] koopas
    ) {
        addGroundSegments(level, groundSegments);
        addBlockRows(level, blockRows);
        addQuestionPairs(level, questionPairs);
        addBridges(level, bridges);
        addCoinArcs(level, coinArcs);
        addWallJumpShafts(level, wallJumpShafts);
        addSkyTunnels(level, skyTunnels);
        addSkyClimbs(level, skyClimbs);
        addCoinLines(level, coinLines);
        addVerticalCoinLines(level, verticalCoinLines);
        addPyramids(level, pyramids);
        addFloatingIslands(level, floatingIslands);
        addPlatformGoombas(level, platformGoombas);
        addPlatformKoopas(level, platformKoopas);
        addEnemyPairs(level, enemyPairs);
        addEnemyGauntlets(level, enemyGauntlets);
        addGoombas(level, goombas);
        addKoopas(level, koopas);
    }

    private BasicLevel createLevel(final int levelNumber, final double width) {
        final BasicLevel level = new BasicLevel(
            levelNumber,
            width,
            SCREEN_HEIGHT,
            PLAYER_SPAWN_X,
            PLAYER_SPAWN_Y
        );

        level.setPlayer(new PlayerImpl(level.getSpawnX(), level.getSpawnY(), createPhysics()));
        return level;
    }

    private void addGroundSegments(final BasicLevel level, final int[][] segments) {
        for (final int[] segment : segments) {
            addGroundSegment(level, segment[START_X_INDEX], segment[END_X_INDEX]);
        }
    }

    private void addBlockRows(final BasicLevel level, final int[][] rows) {
        for (final int[] row : rows) {
            addBlockRow(level, row[X_INDEX], row[Y_INDEX], row[COUNT_INDEX], BlockType.NORMAL);
        }
    }

    private void addQuestionPairs(final BasicLevel level, final int[][] pairs) {
        for (final int[] pair : pairs) {
            addQuestionPair(level, pair[X_INDEX], pair[Y_INDEX]);
        }
    }

    private void addBridges(final BasicLevel level, final int[][] bridges) {
        for (final int[] bridge : bridges) {
            addBridgeOverPit(level, bridge[X_INDEX], bridge[Y_INDEX], bridge[COUNT_INDEX]);
        }
    }

    private void addCoinArcs(final BasicLevel level, final int[][] arcs) {
        for (final int[] arc : arcs) {
            addCoinArc(level, arc[X_INDEX], arc[Y_INDEX], arc[COUNT_INDEX]);
        }
    }

    private void addWallJumpShafts(final BasicLevel level, final int[][] shafts) {
        for (final int[] shaft : shafts) {
            addWallJumpShaft(level, shaft[X_INDEX], shaft[STEPS_INDEX]);
        }
    }

    private void addSkyTunnels(final BasicLevel level, final int[][] tunnels) {
        for (final int[] tunnel : tunnels) {
            addSkyTunnel(level, tunnel[X_INDEX], tunnel[Y_INDEX], tunnel[COUNT_INDEX]);
        }
    }

    private void addSkyClimbs(final BasicLevel level, final int[][] climbs) {
        for (final int[] climb : climbs) {
            addSkyClimb(level, climb[X_INDEX], climb[Y_INDEX], climb[COUNT_INDEX]);
        }
    }

    private void addCoinLines(final BasicLevel level, final int[][] lines) {
        for (final int[] line : lines) {
            addCoinLine(level, line[X_INDEX], line[Y_INDEX], line[COUNT_INDEX]);
        }
    }

    private void addVerticalCoinLines(final BasicLevel level, final int[][] lines) {
        for (final int[] line : lines) {
            addVerticalCoinLine(level, line[X_INDEX], line[Y_INDEX], line[COUNT_INDEX]);
        }
    }

    private void addPyramids(final BasicLevel level, final int[][] pyramids) {
        for (final int[] pyramid : pyramids) {
            addStepPyramid(level, pyramid[X_INDEX], pyramid[STEPS_INDEX], BlockType.BRICK);
        }
    }

    private void addFloatingIslands(final BasicLevel level, final int[][] islands) {
        for (final int[] island : islands) {
            addFloatingIsland(level, island[X_INDEX], island[Y_INDEX], island[COUNT_INDEX]);
        }
    }

    private void addPlatformGoombas(final BasicLevel level, final int[][] goombas) {
        for (final int[] goomba : goombas) {
            level.addEntity(createGoombaOnPlatform(goomba[X_INDEX], goomba[Y_INDEX]));
        }
    }

    private void addPlatformKoopas(final BasicLevel level, final int[][] koopas) {
        for (final int[] koopa : koopas) {
            level.addEntity(createKoopaOnPlatform(koopa[X_INDEX], koopa[Y_INDEX]));
        }
    }

    private void addEnemyPairs(final BasicLevel level, final int[][] pairs) {
        for (final int[] pair : pairs) {
            addEnemyPair(level, pair[X_INDEX]);
        }
    }

    private void addEnemyGauntlets(final BasicLevel level, final int[][] gauntlets) {
        for (final int[] gauntlet : gauntlets) {
            addEnemyGauntlet(level, gauntlet[X_INDEX], gauntlet[STEPS_INDEX]);
        }
    }

    private void addGoombas(final BasicLevel level, final int[] goombas) {
        for (final int goombaX : goombas) {
            level.addEntity(createGoomba(goombaX));
        }
    }

    private void addKoopas(final BasicLevel level, final int[] koopas) {
        for (final int koopaX : koopas) {
            level.addEntity(createKoopa(koopaX));
        }
    }

    private void addGroundSegment(final BasicLevel level, final int startX, final int endX) {
        for (int x = startX; x < endX; x += TILE_SIZE) {
            level.addEntity(new Block(x, FLOOR_Y));
        }
    }

    private void addBlockRow(
        final BasicLevel level,
        final double startX,
        final double y,
        final int blocks,
        final BlockType blockType
    ) {
        for (int i = FIRST_LOOP_INDEX; i < blocks; i++) {
            level.addEntity(new Block(startX + TILE_SIZE * i, y, blockType));
        }
    }

    private void addQuestionPair(final BasicLevel level, final double x, final double y) {
        level.addEntity(new Block(x, y, BlockType.QUESTION));
        level.addEntity(new Block(x + TILE_SIZE * SECOND_ITEM_OFFSET, y, BlockType.BRICK));
        level.addEntity(new Block(x + TILE_SIZE * THIRD_ITEM_OFFSET, y, BlockType.QUESTION));
    }

    private void addBridgeOverPit(final BasicLevel level, final double startX, final double y, final int platforms) {
        for (int i = FIRST_LOOP_INDEX; i < platforms; i++) {
            addBlockRow(
                level,
                startX + i * BRIDGE_X_STEP,
                y - (i % EVEN_DIVISOR) * BRIDGE_Y_STEP,
                BRIDGE_PLATFORM_BLOCKS,
                BlockType.NORMAL
            );
        }
    }

    private void addSkyClimb(final BasicLevel level, final double startX, final double startY, final int platforms) {
        for (int i = FIRST_LOOP_INDEX; i < platforms; i++) {
            final double x = startX + i * SKY_CLIMB_X_STEP;
            final double y = Math.max(SKY_CLIMB_MIN_Y, startY - i * SKY_CLIMB_Y_STEP);
            final BlockType blockType = i % EVEN_DIVISOR == FIRST_LOOP_INDEX ? BlockType.NORMAL : BlockType.BRICK;
            addBlockRow(level, x, y, SKY_CLIMB_BLOCKS, blockType);
            level.addEntity(createCoin(x + SKY_CLIMB_COIN_ONE_X_OFFSET, y - SKY_CLIMB_COIN_Y_OFFSET));
            level.addEntity(createCoin(x + SKY_CLIMB_COIN_TWO_X_OFFSET, y - SKY_CLIMB_COIN_Y_OFFSET));
            if (i % SKY_CLIMB_QUESTION_PERIOD == SKY_CLIMB_QUESTION_SLOT) {
                level.addEntity(new Block(
                    x + TILE_SIZE,
                    y - TILE_SIZE * QUESTION_CLEARANCE_TILES,
                    BlockType.QUESTION
                ));
            }
        }
    }

    private void addSkyTunnel(final BasicLevel level, final double startX, final double y, final int blocks) {
        addBlockRow(level, startX, y, blocks, BlockType.NORMAL);
        addBlockRow(level, startX, y - TILE_SIZE * SKY_TUNNEL_CLEARANCE_TILES, blocks, BlockType.BRICK);
        for (int i = FIRST_INNER_COIN_INDEX; i < blocks - SECOND_ITEM_OFFSET; i++) {
            if (i % EVEN_DIVISOR == FIRST_LOOP_INDEX) {
                level.addEntity(createCoin(startX + TILE_SIZE * i + SKY_TUNNEL_COIN_X_OFFSET, y - SKY_TUNNEL_COIN_Y_OFFSET));
            }
        }
    }

    private void addWallJumpShaft(final BasicLevel level, final double leftX, final int blocksHigh) {
        final double rightX = leftX + TILE_SIZE * WALL_SHAFT_WIDTH_TILES;
        for (int i = FIRST_LOOP_INDEX; i < blocksHigh; i++) {
            final double y = FLOOR_Y - TILE_SIZE * (i + SECOND_ITEM_OFFSET);
            level.addEntity(new Block(leftX, y, BlockType.BRICK));
            level.addEntity(new Block(rightX, y, BlockType.BRICK));
        }

        final double exitY = FLOOR_Y - TILE_SIZE * blocksHigh;
        addBlockRow(level, rightX + TILE_SIZE, exitY, WALL_SHAFT_EXIT_BLOCKS, BlockType.NORMAL);
        level.addEntity(new Block(
            rightX + TILE_SIZE * THIRD_ITEM_OFFSET,
            exitY - TILE_SIZE * QUESTION_CLEARANCE_TILES,
            BlockType.QUESTION
        ));
        addVerticalCoinLine(level, leftX + TILE_SIZE + SKY_TUNNEL_COIN_X_OFFSET, FLOOR_Y - TILE_SIZE, blocksHigh);
    }

    private void addStepPyramid(
        final BasicLevel level,
        final double startX,
        final int steps,
        final BlockType blockType
    ) {
        for (int column = FIRST_LOOP_INDEX; column < steps * PYRAMID_WIDTH_MULTIPLIER - SECOND_ITEM_OFFSET; column++) {
            final int columnHeight = column < steps
                ? column + SECOND_ITEM_OFFSET
                : steps * PYRAMID_WIDTH_MULTIPLIER - SECOND_ITEM_OFFSET - column;
            for (int block = FIRST_LOOP_INDEX; block < columnHeight; block++) {
                level.addEntity(new Block(
                    startX + TILE_SIZE * column,
                    FLOOR_Y - TILE_SIZE * (block + SECOND_ITEM_OFFSET),
                    blockType
                ));
            }
        }
        addCoinLine(
            level,
            startX + TILE_SIZE * (steps - SECOND_ITEM_OFFSET) - PYRAMID_COIN_X_OFFSET,
            FLOOR_Y - TILE_SIZE * (steps + SECOND_ITEM_OFFSET),
            PYRAMID_EXTRA_COIN_COUNT
        );
    }

    private void addFloatingIsland(final BasicLevel level, final double startX, final double y, final int blocks) {
        addBlockRow(level, startX, y, blocks, BlockType.NORMAL);
        addCoinLine(
            level,
            startX + FLOATING_ISLAND_COIN_X_OFFSET,
            y - FLOATING_ISLAND_COIN_Y_OFFSET,
            blocks - SECOND_ITEM_OFFSET
        );
        if (blocks >= QUESTION_MIN_ISLAND_BLOCKS) {
            level.addEntity(new Block(
                startX + TILE_SIZE * (blocks / EVEN_DIVISOR),
                y - TILE_SIZE * QUESTION_CLEARANCE_TILES,
                BlockType.QUESTION
            ));
        }
    }

    private void addCoinLine(final BasicLevel level, final double startX, final double y, final int coins) {
        for (int i = FIRST_LOOP_INDEX; i < coins; i++) {
            level.addEntity(createCoin(startX + COIN_LINE_SPACING * i, y));
        }
    }

    private void addVerticalCoinLine(final BasicLevel level, final double x, final double startY, final int coins) {
        for (int i = FIRST_LOOP_INDEX; i < coins; i++) {
            level.addEntity(createCoin(x, startY - VERTICAL_COIN_SPACING * i));
        }
    }

    private void addCoinArc(final BasicLevel level, final double startX, final double y, final int coins) {
        for (int i = FIRST_LOOP_INDEX; i < coins; i++) {
            final double offsetY = Math.abs(i - coins / (double) EVEN_DIVISOR) * COIN_ARC_Y_STEP;
            level.addEntity(createCoin(startX + COIN_ARC_SPACING * i, y + offsetY));
        }
    }

    private void addEnemyPair(final BasicLevel level, final double x) {
        level.addEntity(createGoomba(x));
        level.addEntity(createKoopa(x + ENEMY_PAIR_SPACING));
    }

    private void addEnemyGauntlet(final BasicLevel level, final double startX, final int enemies) {
        for (int i = FIRST_LOOP_INDEX; i < enemies; i++) {
            final double x = startX + i * ENEMY_GAUNTLET_SPACING;
            if (i % KOOPA_GAUNTLET_SPACING == KOOPA_GAUNTLET_SLOT) {
                level.addEntity(createKoopa(x));
            } else {
                level.addEntity(createGoomba(x));
            }
        }
    }

    private Coin createCoin(final double x, final double y) {
        return new Coin(x, y, createPhysics());
    }

    private Goomba createGoomba(final double x) {
        return new Goomba(x, GOOMBA_Y, createPhysics());
    }

    private Koopa createKoopa(final double x) {
        return new Koopa(x, KOOPA_Y, createPhysics());
    }

    private Goomba createGoombaOnPlatform(final double x, final double platformY) {
        return new Goomba(x, platformY - GOOMBA_HEIGHT, createPhysics());
    }

    private Koopa createKoopaOnPlatform(final double x, final double platformY) {
        return new Koopa(x, platformY - KOOPA_HEIGHT, createPhysics());
    }

    private void addGoal(final BasicLevel level) {
        final Pole pole = new Pole(
            level.getWidth() - GOAL_DISTANCE_FROM_END,
            FLOOR_Y - GOAL_POLE_HEIGHT,
            GOAL_POLE_HEIGHT
        );
        level.addEntity(pole);
        level.addEntity(new Flag(pole));
    }

    private BasicPhysics createPhysics() {
        return new BasicPhysicsImpl();
    }

}
