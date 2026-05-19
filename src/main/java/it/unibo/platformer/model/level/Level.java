package it.unibo.platformer.model.level;

import java.util.List;
import it.unibo.platformer.model.entities.Entity;
import it.unibo.platformer.model.entities.players.Player;
import javafx.scene.canvas.GraphicsContext;

public interface Level {

    int getLevelNumber();

    double getWidth();

    double getHeight();

    double getSpawnX();

    double getSpawnY();

    void setPlayer(Player player);

    Player getPlayer();

    void addEntity(Entity entity);

    void removeEntity(Entity entity);

    List<Entity> getEntities();

    int getCollectedCoins();

    void resetCollectedCoins();

    void update(double deltatime);

    void render(GraphicsContext gc);
}

