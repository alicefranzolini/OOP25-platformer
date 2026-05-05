package it.unibo.platformer.model.level;

import java.util.ArrayList;
import java.util.List;

import it.unibo.platformer.model.entities.Entity;

public class BasicLevel implements Level {
    private final List<Entity> entities;

    public BasicLevel() {
        this.entities = new ArrayList<>();
    }

    @Override
    public void addEntity(final Entity entity) {
        this.entities.add(entity);
    }

    @Override
    public void removeEntity(final Entity entity) {
        this.entities.remove(entity);
    }

    @Override
    public List<Entity> getEntities() {
        return this.entities;
    }

    @Override
    public void update(double deltaTime) {
        for (Entity entity : this.entities) {
            if (entity.isActive()) {
                entity.update(deltaTime);
            }
        }

        //remove if entity die
        this.entities.removeIf(entity -> !entity.isActive());
    }
}
