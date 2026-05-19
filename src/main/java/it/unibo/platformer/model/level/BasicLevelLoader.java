package it.unibo.platformer.model.level;

import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.entities.enemies.Koopa;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.entities.worldEntity.Block;
import it.unibo.platformer.model.entities.worldEntity.Block.BlockType;
import it.unibo.platformer.model.entities.worldEntity.Coin;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;

public class BasicLevelLoader implements LevelLoader {

    private static final int TILE_SIZE = 32;
    private static final double SCREEN_HEIGHT = 720;
    private static final double PLAYER_SPAWN_X = 100;
    private static final double PLAYER_SPAWN_Y = 300;
    private static final double FLOOR_Y = 500;

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
        final BasicLevel level = createLevel(1, 2000);

        addFloor(level);

        level.addEntity(new Block(300, 400));
        level.addEntity(new Block(332, 400));
        level.addEntity(new Block(364, 400));
        level.addEntity(new Block(500, 350, BlockType.QUESTION));

        // coin
        level.addEntity(createCoin(700, 300));

        // enemies
        level.addEntity(createGoomba(900, 450));
        level.addEntity(createKoopa(1200, 450));

        return level;
    }

    private Level loadLevelTwo() {
        final BasicLevel level = createLevel(2, 2400);

        addFloor(level);

        level.addEntity(new Block(260, 420, BlockType.BRICK));
        level.addEntity(new Block(292, 420, BlockType.BRICK));
        level.addEntity(new Block(520, 360, BlockType.QUESTION));
        level.addEntity(new Block(780, 390));
        level.addEntity(new Block(812, 390));
        level.addEntity(new Block(844, 390));

        level.addEntity(createCoin(560, 320));
        level.addEntity(createCoin(820, 350));
        level.addEntity(createCoin(1120, 300));

        level.addEntity(createGoomba(980, 450));
        level.addEntity(createGoomba(1260, 450));
        level.addEntity(createKoopa(1600, 450));

        return level;
    }

    private Level loadLevelThree() {
        final BasicLevel level = createLevel(3, 2800);

        addFloor(level);

        level.addEntity(new Block(360, 390, BlockType.QUESTION));
        level.addEntity(new Block(640, 340, BlockType.BRICK));
        level.addEntity(new Block(672, 340, BlockType.BRICK));
        level.addEntity(new Block(704, 340, BlockType.QUESTION));
        level.addEntity(new Block(1100, 410));
        level.addEntity(new Block(1132, 410));
        level.addEntity(new Block(1450, 360, BlockType.BRICK));
        level.addEntity(new Block(1482, 360, BlockType.BRICK));

        level.addEntity(createCoin(370, 350));
        level.addEntity(createCoin(720, 300));
        level.addEntity(createCoin(1140, 370));
        level.addEntity(createCoin(1490, 320));

        level.addEntity(createGoomba(900, 450));
        level.addEntity(createKoopa(1320, 450));
        level.addEntity(createKoopa(1900, 450));

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

    private void addFloor(final BasicLevel level) {
        for (int x = 0; x < level.getWidth(); x += TILE_SIZE) {
            level.addEntity(new Block(x, FLOOR_Y));
        }
    }

    private Coin createCoin(final double x, final double y) {
        return new Coin(x, y, createPhysics());
    }

    private Goomba createGoomba(final double x, final double y) {
        return new Goomba(x, y, createPhysics());
    }

    private Koopa createKoopa(final double x, final double y) {
        return new Koopa(x, y, createPhysics());
    }

    private BasicPhysics createPhysics() {
        return new BasicPhysicsImpl();
    }
    
}
