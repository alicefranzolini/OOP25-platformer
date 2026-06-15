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

// CHECKSTYLE: MagicNumber OFF
/**
 * Builds the three playable levels used by the game.
 */
public final class BasicLevelLoader implements LevelLoader {

    private static final int TILE_SIZE = 32;
    private static final double SCREEN_HEIGHT = 720;
    private static final double PLAYER_SPAWN_X = 100;
    private static final double PLAYER_SPAWN_Y = 300;
    private static final double FLOOR_Y = 500;
    private static final double GOOMBA_Y = FLOOR_Y - 32;
    private static final double KOOPA_Y = FLOOR_Y - 48;
    private static final double GOAL_DISTANCE_FROM_END = 96;
    private static final double GOAL_POLE_HEIGHT = 220;

    @Override
    public Level loadLevel(final int levelNumber) {
        switch (levelNumber) {
            case 1:
                return loadLevelOne();
            case 2:
                return loadLevelTwo();
            case 3:
                return loadLevelThree();
            default:
                return loadLevelOne();
        }
    }

    private Level loadLevelOne() {
        final BasicLevel level = createLevel(1, 3800);

        addGroundSegment(level, 0, 700);
        addGroundSegment(level, 880, 1500);
        addGroundSegment(level, 1680, 2320);
        addGroundSegment(level, 2520, 3180);
        addGroundSegment(level, 3360, 3800);

        addBlockRow(level, 300, 400, 3, BlockType.NORMAL);
        addQuestionPair(level, 500, 350);
        addBridgeOverPit(level, 720, 390, 3);
        addCoinArc(level, 690, 340, 5);

        addWallJumpShaft(level, 950, 7);
        addSkyTunnel(level, 1140, 270, 8);
        addCoinLine(level, 1160, 220, 5);
        addQuestionPair(level, 1320, 210);

        addSkyClimb(level, 1600, 410, 5);
        addBridgeOverPit(level, 2340, 380, 4);
        addWallJumpShaft(level, 2640, 6);
        addSkyTunnel(level, 2830, 300, 7);
        addQuestionPair(level, 3060, 240);
        addStairs(level, 3400, 4);

        addCoinLine(level, 900, 455, 4);
        addVerticalCoinLine(level, 1030, 440, 5);
        addCoinLine(level, 1710, 300, 5);
        addCoinLine(level, 2850, 250, 5);
        addVerticalCoinLine(level, 3490, 430, 4);

        addEnemyPair(level, 500);
        addEnemyPair(level, 1230);
        addEnemyPair(level, 1850);
        addEnemyGauntlet(level, 2860, 3);
        level.addEntity(createKoopa(3600));
        level.addEntity(createGoomba(3650));

        addGoal(level);

        return level;
    }

    private Level loadLevelTwo() {
        final BasicLevel level = createLevel(2, 5400);

        addGroundSegment(level, 0, 640);
        addGroundSegment(level, 850, 1420);
        addGroundSegment(level, 1620, 2160);
        addGroundSegment(level, 2380, 3040);
        addGroundSegment(level, 3320, 4040);
        addGroundSegment(level, 4300, 5400);

        addQuestionPair(level, 420, 360);
        addBridgeOverPit(level, 670, 390, 3);
        addCoinArc(level, 650, 345, 5);

        addWallJumpShaft(level, 920, 8);
        addSkyTunnel(level, 1120, 240, 9);
        addSkyClimb(level, 1500, 430, 6);
        addQuestionPair(level, 1850, 220);
        addBridgeOverPit(level, 2180, 380, 4);

        addWallJumpShaft(level, 2460, 8);
        addSkyTunnel(level, 2660, 230, 10);
        addQuestionPair(level, 2920, 190);
        addBridgeOverPit(level, 3060, 370, 5);

        addSkyClimb(level, 3380, 420, 7);
        addWallJumpShaft(level, 3820, 9);
        addSkyTunnel(level, 4030, 210, 9);
        addStairs(level, 4520, 5);
        addQuestionPair(level, 4920, 310);

        addCoinLine(level, 1130, 190, 6);
        addVerticalCoinLine(level, 1010, 440, 7);
        addCoinLine(level, 2680, 180, 7);
        addVerticalCoinLine(level, 2560, 440, 7);
        addCoinLine(level, 4050, 160, 6);
        addCoinLine(level, 4560, 300, 6);

        addEnemyPair(level, 430);
        addEnemyGauntlet(level, 1180, 2);
        addEnemyGauntlet(level, 1680, 3);
        addEnemyGauntlet(level, 2700, 3);
        addEnemyGauntlet(level, 3420, 3);
        level.addEntity(createKoopa(4740));
        level.addEntity(createGoomba(4890));
        level.addEntity(createKoopa(5100));

        addGoal(level);

        return level;
    }

    private Level loadLevelThree() {
        final BasicLevel level = createLevel(3, 7000);

        addGroundSegment(level, 0, 600);
        addGroundSegment(level, 840, 1280);
        addGroundSegment(level, 1540, 2100);
        addGroundSegment(level, 2360, 2960);
        addGroundSegment(level, 3240, 3880);
        addGroundSegment(level, 4160, 4840);
        addGroundSegment(level, 5120, 7000);

        addQuestionPair(level, 360, 380);
        addBridgeOverPit(level, 630, 390, 4);
        addWallJumpShaft(level, 900, 9);
        addSkyTunnel(level, 1120, 210, 10);
        addQuestionPair(level, 1330, 170);

        addSkyClimb(level, 1500, 430, 8);
        addBridgeOverPit(level, 2120, 370, 5);
        addWallJumpShaft(level, 2420, 10);
        addSkyTunnel(level, 2640, 185, 11);
        addQuestionPair(level, 2960, 150);

        addSkyClimb(level, 3260, 420, 8);
        addBridgeOverPit(level, 3900, 360, 5);
        addWallJumpShaft(level, 4240, 10);
        addSkyTunnel(level, 4460, 175, 11);
        addQuestionPair(level, 4820, 150);

        addWallJumpShaft(level, 5200, 9);
        addSkyClimb(level, 5450, 410, 7);
        addStairs(level, 6200, 6);
        addQuestionPair(level, 6580, 300);

        addCoinLine(level, 1130, 160, 7);
        addVerticalCoinLine(level, 1000, 440, 8);
        addCoinLine(level, 2660, 140, 8);
        addVerticalCoinLine(level, 2540, 440, 8);
        addCoinLine(level, 4480, 130, 8);
        addVerticalCoinLine(level, 4360, 440, 8);
        addCoinLine(level, 5480, 260, 8);
        addCoinLine(level, 6260, 290, 6);

        addEnemyPair(level, 400);
        addEnemyGauntlet(level, 1130, 2);
        addEnemyGauntlet(level, 1640, 4);
        addEnemyGauntlet(level, 2700, 2);
        addEnemyGauntlet(level, 3360, 4);
        addEnemyGauntlet(level, 4540, 2);
        addEnemyGauntlet(level, 5600, 3);
        level.addEntity(createKoopa(6500));
        level.addEntity(createGoomba(6660));

        addGoal(level);

        return level;
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
        for (int i = 0; i < blocks; i++) {
            level.addEntity(new Block(startX + TILE_SIZE * i, y, blockType));
        }
    }

    private void addQuestionPair(final BasicLevel level, final double x, final double y) {
        level.addEntity(new Block(x, y, BlockType.QUESTION));
        level.addEntity(new Block(x + TILE_SIZE, y, BlockType.BRICK));
        level.addEntity(new Block(x + TILE_SIZE * 2, y, BlockType.QUESTION));
    }

    private void addBridgeOverPit(final BasicLevel level, final double startX, final double y, final int platforms) {
        for (int i = 0; i < platforms; i++) {
            addBlockRow(level, startX + i * 95, y - (i % 2) * 35, 2, BlockType.NORMAL);
        }
    }

    private void addSkyClimb(final BasicLevel level, final double startX, final double startY, final int platforms) {
        for (int i = 0; i < platforms; i++) {
            final double x = startX + i * 130;
            final double y = Math.max(145, startY - i * 45);
            final BlockType blockType = i % 2 == 0 ? BlockType.NORMAL : BlockType.BRICK;
            addBlockRow(level, x, y, 3, blockType);
            level.addEntity(createCoin(x + 22, y - 36));
            level.addEntity(createCoin(x + 62, y - 36));
            if (i % 3 == 2) {
                level.addEntity(new Block(x + TILE_SIZE, y - 72, BlockType.QUESTION));
            }
        }
    }

    private void addSkyTunnel(final BasicLevel level, final double startX, final double y, final int blocks) {
        addBlockRow(level, startX, y, blocks, BlockType.NORMAL);
        addBlockRow(level, startX, y - TILE_SIZE * 3, blocks, BlockType.BRICK);
        for (int i = 1; i < blocks - 1; i++) {
            if (i % 2 == 0) {
                level.addEntity(createCoin(startX + TILE_SIZE * i + 8, y - 50));
            }
        }
    }

    private void addWallJumpShaft(final BasicLevel level, final double leftX, final int blocksHigh) {
        final double rightX = leftX + TILE_SIZE * 3;
        for (int i = 0; i < blocksHigh; i++) {
            final double y = FLOOR_Y - TILE_SIZE * (i + 1);
            level.addEntity(new Block(leftX, y, BlockType.BRICK));
            level.addEntity(new Block(rightX, y, BlockType.BRICK));
        }

        final double exitY = FLOOR_Y - TILE_SIZE * blocksHigh;
        addBlockRow(level, rightX + TILE_SIZE, exitY, 4, BlockType.NORMAL);
        level.addEntity(new Block(rightX + TILE_SIZE * 2, exitY - TILE_SIZE * 2, BlockType.QUESTION));
        addVerticalCoinLine(level, leftX + TILE_SIZE + 8, FLOOR_Y - TILE_SIZE, blocksHigh);
    }

    private void addStairs(final BasicLevel level, final double startX, final int steps) {
        for (int step = 0; step < steps; step++) {
            for (int block = 0; block <= step; block++) {
                level.addEntity(new Block(
                    startX + TILE_SIZE * step,
                    FLOOR_Y - TILE_SIZE * (block + 1),
                    BlockType.BRICK
                ));
            }
        }
    }

    private void addCoinLine(final BasicLevel level, final double startX, final double y, final int coins) {
        for (int i = 0; i < coins; i++) {
            level.addEntity(createCoin(startX + 40 * i, y));
        }
    }

    private void addVerticalCoinLine(final BasicLevel level, final double x, final double startY, final int coins) {
        for (int i = 0; i < coins; i++) {
            level.addEntity(createCoin(x, startY - 38 * i));
        }
    }

    private void addCoinArc(final BasicLevel level, final double startX, final double y, final int coins) {
        for (int i = 0; i < coins; i++) {
            final double offsetY = Math.abs(i - coins / 2.0) * 18;
            level.addEntity(createCoin(startX + 42 * i, y + offsetY));
        }
    }

    private void addEnemyPair(final BasicLevel level, final double x) {
        level.addEntity(createGoomba(x));
        level.addEntity(createKoopa(x + 150));
    }

    private void addEnemyGauntlet(final BasicLevel level, final double startX, final int enemies) {
        for (int i = 0; i < enemies; i++) {
            final double x = startX + i * 135;
            if (i % 3 == 1) {
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
// CHECKSTYLE: MagicNumber ON
