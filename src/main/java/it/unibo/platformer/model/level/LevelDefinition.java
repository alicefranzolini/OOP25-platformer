package it.unibo.platformer.model.level;

/**
 * Contains the data needed to build one playable level.
 */
interface LevelDefinition {

    /**
     * Returns the number used to select this level.
     *
     * @return the level number
     */
    int getLevelNumber();

    /**
     * Returns the complete width of this level.
     *
     * @return the level width
     */
    double getWidth();

    /**
     * Adds this level layout to the given level.
     *
     * @param level the level receiving the entities
     * @param builder the helper used to build common structures
     */
    void build(BasicLevel level, LevelBuilder builder);
}
