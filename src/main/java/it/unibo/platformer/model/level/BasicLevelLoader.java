package it.unibo.platformer.model.level;

import it.unibo.platformer.model.entities.*;

public class BasicLevelLoader implements LevelLoader {

    @Override
    public Level loadLevel(int levelNumber) {
        BasicLevel level = new BasicLevel();

        switch (levelNumber) {
            case 1:
                //first lv
                break;
            case 2:
                //second lv
                break;
            case 3:
                //bossLevel
                break;
        }
        return level;
    }
    
}
