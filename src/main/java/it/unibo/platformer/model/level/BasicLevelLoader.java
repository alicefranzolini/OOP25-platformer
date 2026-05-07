package it.unibo.platformer.model.level;

import it.unibo.platformer.model.entities.*;
import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.entities.enemies.Koopa;
import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.entities.worldEntity.Block;
import it.unibo.platformer.model.entities.worldEntity.Coin;
import it.unibo.platformer.model.entities.worldEntity.Block.BlockType;

public class BasicLevelLoader implements LevelLoader {

    @Override
    public Level loadLevel(int levelNumber) {
        BasicLevel level = new BasicLevel();

        switch (levelNumber) {
            case 1:
                return loadLevelOne();
            case 2:
                return loadLevelTwo();
            case 3:
                return loadLevelthree();
            default:
                return loadLevelOne();
        }
        return level;
    }

    private Level loadLevelOne() {
        BasicLevel level = new BasicLevel();

        Player player = new Player(100, 300);

        level.setPlayer(player);

        //floor
        for (int x = 0; x < 2000; x += 32) {
            level.addEntity(new Block(x, 500));
        }

        //blocks
        level.addEntity(new Block(300, 400));
        level.addEntity(new Block(332, 400));
        level.addEntity(new Block(364, 400));
        level.addEntity(new Block(500, 350, BlockType.QUESTION));

        // coin
        level.addEntity(new Coin(700, 300));

        // enemies
        level.addEntity(new Goomba(900, 450));
        level.addEntity(new Koopa(1200, 450));

        return level;
    }
    
}
