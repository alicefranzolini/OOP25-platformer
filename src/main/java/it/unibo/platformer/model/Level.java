package it.unibo.platformer.model;

import java.util.ArrayList;
import java.util.List;

import it.unibo.platformer.model.entities.Entity;

public class Level {
    private final List<Entity> entities;

    public Level() {
        this.entities = new ArrayList<>();
    }

    public void addEntity(final Entity entity) {
        this.entities.remove(entity);
    }

    public void removeEntity(final Entity entity) {
        this.entities.remove(entity);
    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public void update(double deltaTime) {
        for (Entity entity : entities) {
            if (entity.isActive()) {
                entity.update(deltaTime);
            }
        }

        this.entities.removeIf(entity -> !entity.isActive());
    }
}
