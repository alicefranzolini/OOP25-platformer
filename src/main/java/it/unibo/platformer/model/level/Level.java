package it.unibo.platformer.model.level;

import java.util.List;
import it.unibo.platformer.model.entities.Entity;
import it.unibo.platformer.model.entities.players.Player;

public interface Level {

    void setPlayer(Player player);

    Player getPlayer();

    void addEntity(Entity entity);

    void removeEntity(Entity entity);

    List<Entity> getEntities();

    void update(double deltatime);
}

