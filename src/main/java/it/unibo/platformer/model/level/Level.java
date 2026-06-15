package it.unibo.platformer.model.level;

import java.util.List;
import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.players.Player;
import javafx.scene.canvas.GraphicsContext;

/**
 * Common interface for a playable level.
 */
public interface Level {

    /**
     * Gets the level number.
     *
     * @return the level number
     */
    int getLevelNumber();

    /**
     * Gets the level width.
     *
     * @return the level width
     */
    double getWidth();

    /**
     * Gets the level height.
     *
     * @return the level height
     */
    double getHeight();

    /**
     * Gets the player spawn x coordinate.
     *
     * @return the spawn x coordinate
     */
    double getSpawnX();

    /**
     * Gets the player spawn y coordinate.
     *
     * @return the spawn y coordinate
     */
    double getSpawnY();

    /**
     * Sets the player used in this level.
     *
     * @param player the player to place in the level
     */
    void setPlayer(Player player);

    /**
     * Gets the current player.
     *
     * @return the current player
     */
    Player getPlayer();

    /**
     * Adds an entity to the level.
     *
     * @param entity the entity to add
     */
    void addEntity(AbstractEntity entity);

    /**
     * Removes an entity from the level.
     *
     * @param entity the entity to remove
     */
    void removeEntity(AbstractEntity entity);

    /**
     * Gets all entities currently in the level.
     *
     * @return the level entities
     */
    List<AbstractEntity> getEntities();

    /**
     * Gets the coins collected during the latest update.
     *
     * @return the collected coins
     */
    int getCollectedCoins();

    /**
     * Clears the collected coin counter after the score reads it.
     */
    void resetCollectedCoins();

    /**
     * Handles jump actions that are pressed once, like a wall jump.
     *
     * @param jumpPressed true if jump was just pressed
     */
    default void handleJumpPressed(final boolean jumpPressed) {
        // Only levels that support special jump actions need to use this.
    }

    /**
     * Updates the level logic.
     *
     * @param deltaTime elapsed time in seconds
     */
    void update(double deltaTime);

    /**
     * Renders the level.
     *
     * @param gc the canvas graphics context
     */
    void render(GraphicsContext gc);
}
