package it.unibo.platformer.model.level;

import java.util.ArrayList;
import java.util.List;

import it.unibo.platformer.model.entities.Entity;
import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.entities.StaticEntity;
import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.entities.worldEntity.Coin;
import it.unibo.platformer.model.physics.CollisionDetector;
import it.unibo.platformer.model.physics.CollisionResolver;
import it.unibo.platformer.model.physics.CollisionResult;
import it.unibo.platformer.model.physics.GameObject;
import javafx.scene.canvas.GraphicsContext;

public class BasicLevel implements Level {
    private final List<Entity> entities;
    private final int levelNumber;
    private final double width;
    private final double height;
    private final double spawnX;
    private final double spawnY;
    private int collectedCoins;

    private Player player;

    private final CollisionDetector detector;
    private final CollisionResolver resolver;

    public BasicLevel() {
        this(1, 2000, 720, 100, 300);
    }

    public BasicLevel(
        final int levelNumber,
        final double width,
        final double height,
        final double spawnX,
        final double spawnY
    ) {
        this.entities = new ArrayList<>();
        this.levelNumber = levelNumber;
        this.width = width;
        this.height = height;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.collectedCoins = 0;
        this.detector = new CollisionDetector();
        this.resolver = new CollisionResolver();
    }

    @Override
    public int getLevelNumber() {
        return this.levelNumber;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getSpawnX() {
        return this.spawnX;
    }

    @Override
    public double getSpawnY() {
        return this.spawnY;
    }

    @Override
    public void setPlayer(final Player player) {
        if (this.player != null) {
            removeEntity(this.player);
        }
        this.player = player;
        if (player != null) {
            addEntity(player);
        }
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
    public int getCollectedCoins() {
        return this.collectedCoins;
    }

    @Override
    public void resetCollectedCoins() {
        this.collectedCoins = 0;
    }

    @Override
    public void update(final double deltaTime) {
        for (final Entity entity : this.entities) {
            if (entity.isActive()) {
                entity.update(deltaTime);
            }
        }

        for (final Entity dynamicEntity : this.entities) {
            if (!(dynamicEntity instanceof DynamicEntity)) {
                continue;
            }

            for (final Entity staticEntity : this.entities) {
                if (!(staticEntity instanceof StaticEntity)) {
                    continue;
                }

                GameObject staticObj = new GameObject(
                    (float) staticEntity.getX(),
                    (float) staticEntity.getY(),
                    (float) staticEntity.getWidth(),
                    (float) staticEntity.getHeight()
                );

                final CollisionResult result = detector.getCollisionResult(
                    ((DynamicEntity) dynamicEntity).getGameObject(),
                    staticObj
                );

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
            for (final Entity entity : this.entities) {
                if (entity instanceof Coin) {
                    final Coin coin = (Coin) entity;
                    if (overlaps(this.player, coin)) {
                        coin.setActive(false);
                        this.collectedCoins++;
                    }
                }
            }
        }

        //remove if entity die
        this.entities.removeIf(entity -> !entity.isActive());
    }

    private boolean overlaps(final Entity first, final Entity second) {
        return first.getX() < second.getX() + second.getWidth()
            && first.getX() + first.getWidth() > second.getX()
            && first.getY() < second.getY() + second.getHeight()
            && first.getY() + first.getHeight() > second.getY();
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
