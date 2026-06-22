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
 * Builds common structures and entities used by level definitions.
 */
final class LevelBuilder {

    private static final int TILE_SIZE = 32;
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
    private static final int SKY_CLIMB_COIN_Y_OFFSET = 36;
    private static final int SKY_CLIMB_QUESTION_PERIOD = 3;
    private static final int SKY_CLIMB_QUESTION_SLOT = 2;
    private static final int MAX_SKY_TUNNEL_COINS = 2;
    private static final int MAX_WALL_SHAFT_COINS = 3;
    private static final int MAX_FLOATING_ISLAND_COINS = 3;
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
    private static final double SCREEN_HEIGHT = 720.0;
    private static final double PLAYER_SPAWN_X = 100.0;
    private static final double PLAYER_SPAWN_Y = 300.0;
    private static final double FLOOR_Y = 500.0;
    private static final double GOOMBA_Y = FLOOR_Y - GOOMBA_HEIGHT;
    private static final double KOOPA_Y = FLOOR_Y - KOOPA_HEIGHT;
    private static final double GOAL_DISTANCE_FROM_END = 96.0;
    private static final double GOAL_POLE_HEIGHT = 220.0;

    BasicLevel build(final LevelDefinition definition) {
        final BasicLevel level = new BasicLevel(
            definition.getLevelNumber(),
            definition.getWidth(),
            SCREEN_HEIGHT,
            PLAYER_SPAWN_X,
            PLAYER_SPAWN_Y
        );
        level.setPlayer(new PlayerImpl(level.getSpawnX(), level.getSpawnY(), createPhysics()));
        definition.build(level, this);
        addGoal(level);
        return level;
    }

    void addGroundSegments(final BasicLevel level, final int[][] segments) {
        for (final int[] segment : segments) {
            addGroundSegment(level, segment[START_X_INDEX], segment[END_X_INDEX]);
        }
    }

    void addBlockRows(final BasicLevel level, final int[][] rows) {
        for (final int[] row : rows) {
            addBlockRow(level, row[X_INDEX], row[Y_INDEX], row[COUNT_INDEX], BlockType.NORMAL);
        }
    }

    void addQuestionPairs(final BasicLevel level, final int[][] pairs) {
        for (final int[] pair : pairs) {
            addQuestionPair(level, pair[X_INDEX], pair[Y_INDEX]);
        }
    }

    void addBridges(final BasicLevel level, final int[][] bridges) {
        for (final int[] bridge : bridges) {
            addBridgeOverPit(level, bridge[X_INDEX], bridge[Y_INDEX], bridge[COUNT_INDEX]);
        }
    }

    void addCoinArcs(final BasicLevel level, final int[][] arcs) {
        for (final int[] arc : arcs) {
            addCoinArc(level, arc[X_INDEX], arc[Y_INDEX], arc[COUNT_INDEX]);
        }
    }

    void addWallJumpShafts(final BasicLevel level, final int[][] shafts) {
        for (final int[] shaft : shafts) {
            addWallJumpShaft(level, shaft[X_INDEX], shaft[STEPS_INDEX]);
        }
    }

    void addSkyTunnels(final BasicLevel level, final int[][] tunnels) {
        for (final int[] tunnel : tunnels) {
            addSkyTunnel(level, tunnel[X_INDEX], tunnel[Y_INDEX], tunnel[COUNT_INDEX]);
        }
    }

    void addSkyClimbs(final BasicLevel level, final int[][] climbs) {
        for (final int[] climb : climbs) {
            addSkyClimb(level, climb[X_INDEX], climb[Y_INDEX], climb[COUNT_INDEX]);
        }
    }

    void addCoinLines(final BasicLevel level, final int[][] lines) {
        for (final int[] line : lines) {
            addCoinLine(level, line[X_INDEX], line[Y_INDEX], line[COUNT_INDEX]);
        }
    }

    void addVerticalCoinLines(final BasicLevel level, final int[][] lines) {
        for (final int[] line : lines) {
            addVerticalCoinLine(level, line[X_INDEX], line[Y_INDEX], line[COUNT_INDEX]);
        }
    }

    void addPyramids(final BasicLevel level, final int[][] pyramids) {
        for (final int[] pyramid : pyramids) {
            addStepPyramid(level, pyramid[X_INDEX], pyramid[STEPS_INDEX], BlockType.BRICK);
        }
    }

    void addFloatingIslands(final BasicLevel level, final int[][] islands) {
        for (final int[] island : islands) {
            addFloatingIsland(level, island[X_INDEX], island[Y_INDEX], island[COUNT_INDEX]);
        }
    }

    void addPlatformGoombas(final BasicLevel level, final int[][] goombas) {
        for (final int[] goomba : goombas) {
            level.addEntity(createGoombaOnPlatform(goomba[X_INDEX], goomba[Y_INDEX]));
        }
    }

    void addPlatformKoopas(final BasicLevel level, final int[][] koopas) {
        for (final int[] koopa : koopas) {
            level.addEntity(createKoopaOnPlatform(koopa[X_INDEX], koopa[Y_INDEX]));
        }
    }

    void addEnemyPairs(final BasicLevel level, final int[][] pairs) {
        for (final int[] pair : pairs) {
            addEnemyPair(level, pair[X_INDEX]);
        }
    }

    void addEnemyGauntlets(final BasicLevel level, final int[][] gauntlets) {
        for (final int[] gauntlet : gauntlets) {
            addEnemyGauntlet(level, gauntlet[X_INDEX], gauntlet[STEPS_INDEX]);
        }
    }

    void addGoombas(final BasicLevel level, final int[] goombas) {
        for (final int goombaX : goombas) {
            level.addEntity(createGoomba(goombaX));
        }
    }

    void addKoopas(final BasicLevel level, final int[] koopas) {
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
            final int yOffset = i % EVEN_DIVISOR == FIRST_LOOP_INDEX
                ? FIRST_LOOP_INDEX
                : BRIDGE_Y_STEP;
            addBlockRow(
                level,
                startX + i * BRIDGE_X_STEP,
                y - yOffset,
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
        int coinsAdded = FIRST_LOOP_INDEX;
        for (int i = FIRST_INNER_COIN_INDEX; i < blocks - SECOND_ITEM_OFFSET; i++) {
            if (i % EVEN_DIVISOR == FIRST_LOOP_INDEX && coinsAdded < MAX_SKY_TUNNEL_COINS) {
                level.addEntity(createCoin(
                    startX + TILE_SIZE * i + SKY_TUNNEL_COIN_X_OFFSET,
                    y - SKY_TUNNEL_COIN_Y_OFFSET
                ));
                coinsAdded++;
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
        addVerticalCoinLine(
            level,
            leftX + TILE_SIZE + SKY_TUNNEL_COIN_X_OFFSET,
            FLOOR_Y - TILE_SIZE,
            Math.min(blocksHigh, MAX_WALL_SHAFT_COINS)
        );
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
            Math.min(blocks - SECOND_ITEM_OFFSET, MAX_FLOATING_ISLAND_COINS)
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
