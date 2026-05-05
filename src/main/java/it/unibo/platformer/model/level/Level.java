package it.unibo.platformer.model.level;

import java.util.List;
import it.unibo.platformer.model.entities.Entity;

public interface Level {

    void addEntity(Entity entity);

    void removeEntity(Entity entity);

    List<Entity> getEntities();

    void update(double deltatime);
}

