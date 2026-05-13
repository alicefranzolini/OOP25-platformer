package it.unibo.platformer.model.level;

import java.util.ArrayList;
import java.util.List;

import it.unibo.platformer.model.entities.Entity;
import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.entities.worldEntity.Coin;
import it.unibo.platformer.model.physics.*;
import javafx.scene.canvas.GraphicsContext;
import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.entities.StaticEntity;

public class BasicLevel implements Level {
    private final List<Entity> entities;

    private Player player;

    private final CollisionDetector detector;
    private final CollisionResolver resolver;

    public BasicLevel() {
        this.entities = new ArrayList<>();
        this.detector = new CollisionDetector();
        this.resolver = new CollisionResolver();
    }

    @Override
    public void setPlayer(Player player) {
        this.player = player;
        addEntity(player);
    }

    @Override
    public Player getPlayer() {
        return this.player;
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

        for (Entity dynamicEntity : this.entities) {
            if (!(dynamicEntity instanceof DynamicEntity)) {
                continue;
            }

            for (Entity staticEntity : this.entities) {
                if (!(staticEntity instanceof StaticEntity)) {
                    continue;
                }

                GameObject staticObj = new GameObject(
                    (float) staticEntity.getX(),
                    (float) staticEntity.getY(),
                    (float) staticEntity.getWidth(),
                    (float) staticEntity.getHeight()
                );

                CollisionResult result = detector.getCollisionResult(((DynamicEntity) dynamicEntity).getGameObject(), staticObj);

                if (result != null) {
                    try {
                        resolver.ResolveOne(result);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        if (this.player != null) {
            for (Entity entity : this.entities) {
                if (entity instanceof Coin) {
                    Coin coin = (Coin) entity;

                    boolean collideX = 
                        player.getX() < coin.getX() + coin.getWidth() && player.getX() + player.getWidth() > coin.getY();
                    boolean collideY =
                        player.getY() < coin.getY() + coin.getHeight() && player.getY() + player.getHeight() > coin.getY();
                    
                    if (collideX && collideY) {
                        coin.setActive(false);
                        System.out.println("Coin collected!");
                    }
                }
            }
        }

        //remove if entity die
        this.entities.removeIf(entity -> !entity.isActive());
    }

    @Override
    public void render(final GraphicsContext gc) {
        for (final Entity entity : this.entities) {
            if (entity.isActive()) {
                entity.render(gc);
            }
        }
    }

}
