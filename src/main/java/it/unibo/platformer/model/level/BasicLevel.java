package it.unibo.platformer.model.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.AbstractDynamicEntity;
import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.entities.powerup.MushroomPowerUp;
import it.unibo.platformer.model.entities.powerup.PowerUp;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.entities.world.Coin;
import it.unibo.platformer.model.entities.world.Flag;
import it.unibo.platformer.view.LevelBackgroundRenderer;
import javafx.scene.canvas.GraphicsContext;

/**
 * Basic implementation of a playable platform level.
 */
public final class BasicLevel implements Level {
    private static final int DEFAULT_LEVEL_NUMBER = 1;
    private static final double DEFAULT_LEVEL_WIDTH = 2000.0;
    private static final double DEFAULT_LEVEL_HEIGHT = 720.0;
    private static final double DEFAULT_SPAWN_X = 100.0;
    private static final double DEFAULT_SPAWN_Y = 300.0;
    private static final int INITIAL_COUNTER = 0;
    private static final double FALL_LIMIT_OFFSET = 160.0;
    private static final double GOAL_DISTANCE_FROM_END = 96.0;
    private static final double BIG_PLAYER_HEIGHT = 48.0;

    private final List<AbstractEntity> entities;
    private final List<AbstractEntity> pendingEntities;
    private final QuestionBlockRewardManager rewardManager;
    private final LevelBackgroundRenderer backgroundRenderer;
    private final LevelCollisionManager collisionManager;
    private final EnemyInteractionManager enemyInteractionManager;
    private final int levelNumber;
    private final double width;
    private final double height;
    private final double spawnX;
    private final double spawnY;
    private int collectedCoins;
    private int defeatedEnemies;
    private boolean completionStarted;
    private boolean completed;

    private Player player;

    /**
     * Creates the default test level.
     */
    public BasicLevel() {
        this(DEFAULT_LEVEL_NUMBER, DEFAULT_LEVEL_WIDTH, DEFAULT_LEVEL_HEIGHT, DEFAULT_SPAWN_X, DEFAULT_SPAWN_Y);
    }

    /**
     * Creates a level with custom size and spawn point.
     *
     * @param levelNumber the level number
     * @param levelWidth the level width
     * @param levelHeight the level height
     * @param playerSpawnX the player spawn x coordinate
     * @param playerSpawnY the player spawn y coordinate
     */
    public BasicLevel(
        final int levelNumber,
        final double levelWidth,
        final double levelHeight,
        final double playerSpawnX,
        final double playerSpawnY
    ) {
        this.entities = new ArrayList<>();
        this.pendingEntities = new ArrayList<>();
        this.rewardManager = new QuestionBlockRewardManager();
        this.backgroundRenderer = new LevelBackgroundRenderer();
        this.collisionManager = new LevelCollisionManager();
        this.enemyInteractionManager = new EnemyInteractionManager();
        this.levelNumber = levelNumber;
        this.width = levelWidth;
        this.height = levelHeight;
        this.spawnX = playerSpawnX;
        this.spawnY = playerSpawnY;
        this.collectedCoins = INITIAL_COUNTER;
        this.defeatedEnemies = INITIAL_COUNTER;
        this.completionStarted = false;
        this.completed = false;
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
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The level owns the player instance used by the game loop."
    )
    public void setPlayer(final Player player) {
        if (this.player != null) {
            removeEntity(playerEntity(this.player));
        }
        this.player = player;
        if (player != null) {
            addEntity(playerEntity(player));
        }
    }

    @Override
    @SuppressFBWarnings(
        value = "EI_EXPOSE_REP",
        justification = "The current player is part of the public Level contract."
    )
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public void addEntity(final AbstractEntity entity) {
        this.entities.add(entity);
    }

    @Override
    public void removeEntity(final AbstractEntity entity) {
        this.entities.remove(entity);
    }

    @Override
    public List<AbstractEntity> getEntities() {
        return Collections.unmodifiableList(this.entities);
    }

    @Override
    public int getCollectedCoins() {
        return this.collectedCoins;
    }

    @Override
    public void resetCollectedCoins() {
        this.collectedCoins = INITIAL_COUNTER;
    }

    @Override
    public int getDefeatedEnemies() {
        return this.defeatedEnemies;
    }

    @Override
    public void resetDefeatedEnemies() {
        this.defeatedEnemies = INITIAL_COUNTER;
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public void update(final double deltaTime) {
        this.collisionManager.updateWallJumpTimer(deltaTime);

        final Map<AbstractEntity, double[]> previousPositions = new HashMap<>();

        for (final AbstractEntity entity : this.entities) {
            if (entity.isActive()) {
                if (entity instanceof AbstractDynamicEntity) {
                    previousPositions.put(entity, new double[] {entity.getX(), entity.getY()});
                }
                entity.update(deltaTime);
                if (entity instanceof AbstractDynamicEntity && ((AbstractDynamicEntity) entity).isOnGround()) {
                    moveGroundedEntity((AbstractDynamicEntity) entity, deltaTime);
                }
            }
        }
        ensureBigPlayerSize();

        this.collisionManager.resolveWorldCollisions(
            this.entities,
            this.player,
            previousPositions
        );
        for (final Block block : this.collisionManager.getHitBlocks()) {
            hitBlockFromBelow(block);
        }
        if (this.collisionManager.isGoalReached()) {
            completeLevel();
        }

        this.defeatedEnemies += this.enemyInteractionManager.update(
            this.entities,
            this.player,
            this.width
        );
        handlePlayerPowerUpCollisions();
        handlePlayerFallOut();
        handleLevelEndByPosition();
        updateCompletionState();

        if (this.player != null) {
            for (final AbstractEntity entity : this.entities) {
                if (entity instanceof Coin) {
                    final Coin coin = (Coin) entity;
                    if (overlaps(this.player, coin)) {
                        coin.setActive(false);
                        this.collectedCoins++;
                    }
                }
            }
        }

        this.entities.addAll(this.pendingEntities);
        this.pendingEntities.clear();

        // Remove defeated or collected entities at the end of the frame.
        this.entities.removeIf(entity -> !entity.isActive());
    }

    @Override
    public void handleJumpPressed(final boolean jumpPressed) {
        this.collisionManager.handleJumpPressed(jumpPressed, this.player);
    }

    private AbstractEntity playerEntity(final Player currentPlayer) {
        return (AbstractEntity) currentPlayer;
    }

    private void moveGroundedEntity(final AbstractDynamicEntity entity, final double deltaTime) {
        entity.setX(entity.getX() + entity.getVelocityX() * deltaTime);
    }

    private void hitBlockFromBelow(final Block block) {
        if (block.onHit()) {
            this.pendingEntities.add(this.rewardManager.createReward(block));
        }
    }

    private void handleLevelEndByPosition() {
        if (this.player == null || this.completed) {
            return;
        }

        final double playerEndX = this.player.getX() + this.player.getWidth();
        if (playerEndX >= this.width - GOAL_DISTANCE_FROM_END) {
            completeLevel();
        }
    }

    private void completeLevel() {
        if (this.completionStarted) {
            return;
        }

        this.completionStarted = true;
        for (final AbstractEntity entity : this.entities) {
            if (entity instanceof Flag) {
                ((Flag) entity).lower();
            }
        }
    }

    private void updateCompletionState() {
        if (!this.completionStarted || this.completed) {
            return;
        }

        boolean hasFlag = false;
        for (final AbstractEntity entity : this.entities) {
            if (entity instanceof Flag) {
                hasFlag = true;
                if (!((Flag) entity).isDown()) {
                    return;
                }
            }
        }
        this.completed = hasFlag;
    }

    private void handlePlayerPowerUpCollisions() {
        if (this.player == null || !this.player.isActive()) {
            return;
        }

        for (final AbstractEntity entity : this.entities) {
            if (entity instanceof PowerUp && entity.isActive() && overlaps(this.player, entity)) {
                final PowerUp powerUp = (PowerUp) entity;
                if (canCollectPowerUp(powerUp, entity)) {
                    growInvinciblePlayer(powerUp);
                    powerUp.collect(this.player);
                }
            }
        }
    }

    private void growInvinciblePlayer(final PowerUp powerUp) {
        if (!(powerUp instanceof MushroomPowerUp)
            || !this.player.isInvincible()
            || this.player.getHeight() >= BIG_PLAYER_HEIGHT) {
            return;
        }

        growPlayerToBigSize();
    }

    private void ensureBigPlayerSize() {
        if (this.player != null
            && this.player.getPlayerState() == Player.PlayerState.BIG
            && this.player.getHeight() < BIG_PLAYER_HEIGHT) {
            growPlayerToBigSize();
        }
    }

    private void growPlayerToBigSize() {
        final AbstractEntity currentPlayer = playerEntity(this.player);
        final double heightDifference = BIG_PLAYER_HEIGHT - currentPlayer.getHeight();
        currentPlayer.setHeight(BIG_PLAYER_HEIGHT);
        currentPlayer.setY(currentPlayer.getY() - heightDifference);
    }

    private boolean canCollectPowerUp(final PowerUp powerUp, final AbstractEntity powerUpEntity) {
        return !powerUp.isEmerging() || this.player.getY() <= powerUpEntity.getY();
    }

    private void handlePlayerFallOut() {
        if (this.player != null && !this.player.isDying() && this.player.getY() > this.height + FALL_LIMIT_OFFSET) {
            this.player.die();
        }
    }

    private boolean overlaps(final Player first, final AbstractEntity second) {
        return first.getX() < second.getX() + second.getWidth()
            && first.getX() + first.getWidth() > second.getX()
            && first.getY() < second.getY() + second.getHeight()
            && first.getY() + first.getHeight() > second.getY();
    }

    @Override
    public void render(final GraphicsContext gc) {
        this.backgroundRenderer.render(gc, this.levelNumber, this.width, this.height);
        for (final AbstractEntity entity : this.entities) {
            if (entity.isActive()) {
                entity.render(gc);
            }
        }
    }

}
