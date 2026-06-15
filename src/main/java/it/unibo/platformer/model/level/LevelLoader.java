package it.unibo.platformer.model.level;

/**
 * Loads playable levels by level number.
 */
public interface LevelLoader {

    /**
     * Creates the requested level.
     *
     * @param levelNumber the level number to load
     * @return the loaded level
     */
    Level loadLevel(int levelNumber);
}
