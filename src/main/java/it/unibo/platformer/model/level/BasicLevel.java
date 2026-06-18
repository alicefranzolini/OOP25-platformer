package it.unibo.platformer.model.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.AbstractDynamicEntity;
import it.unibo.platformer.model.entities.AbstractStaticEntity;
import it.unibo.platformer.model.entities.enemies.Enemy;
import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.entities.enemies.Koopa;
import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.entities.powerup.MushroomPowerUp;
import it.unibo.platformer.model.entities.powerup.PowerUp;
import it.unibo.platformer.model.entities.powerup.StarPowerUp;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.entities.world.Coin;
import it.unibo.platformer.model.entities.world.Flag;
import it.unibo.platformer.model.entities.world.Pole;
import it.unibo.platformer.model.physics.api.CollisionDetector;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import it.unibo.platformer.model.physics.impl.CollisionDetectorImpl;
import it.unibo.platformer.model.physics.impl.CollisionResult;
import it.unibo.platformer.model.physics.impl.CollisionSide;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
    private static final int PREVIOUS_X_INDEX = 0;
    private static final int PREVIOUS_Y_INDEX = 1;
    private static final double ORIGIN = 0.0;
    private static final double HALF_DIVISOR = 2.0;
    private static final double POWER_UP_COIN_X_OFFSET = 8.0;
    private static final double POWER_UP_COIN_Y_OFFSET = 24.0;
    private static final double GROUND_EPSILON = 2.0;
    private static final double TOP_COLLISION_TOLERANCE = 10.0;
    private static final double BOTTOM_COLLISION_TOLERANCE = 18.0;
    private static final double MIN_BLOCK_HIT_OVERLAP = 6.0;
    private static final double STOMP_TOLERANCE = 8.0;
    private static final double PLAYER_BOUNCE_AFTER_STOMP = -250.0;
    private static final double WALL_JUMP_SPEED_X = 260.0;
    private static final double WALL_JUMP_SPEED_Y = -430.0;
    private static final double WALL_JUMP_TIME = 0.18;
    private static final double EDGE_CHECK_DISTANCE = 8.0;
    private static final double FALL_LIMIT_OFFSET = 160.0;
    private static final double GOAL_DISTANCE_FROM_END = 96.0;
    private static final int LEVEL_TWO = 2;
    private static final int LEVEL_THREE = 3;
    private static final int LEVEL_ONE_SKY_RED = 113;
    private static final int LEVEL_ONE_SKY_GREEN = 190;
    private static final int LEVEL_ONE_SKY_BLUE = 244;
    private static final int LEVEL_TWO_SKY_RED = 99;
    private static final int LEVEL_TWO_SKY_GREEN = 170;
    private static final int LEVEL_TWO_SKY_BLUE = 224;
    private static final int LEVEL_THREE_SKY_RED = 78;
    private static final int LEVEL_THREE_SKY_GREEN = 126;
    private static final int LEVEL_THREE_SKY_BLUE = 193;
    private static final double SUN_BASE_X = 180.0;
    private static final double SUN_BASE_Y = 80.0;
    private static final double SUN_LEVEL_X_STEP = 45.0;
    private static final double SUN_LEVEL_Y_STEP = 10.0;
    private static final double SUN_SIZE = 70.0;
    private static final double SUN_INNER_OFFSET = 12.0;
    private static final double SUN_INNER_SIZE = 46.0;
    private static final int SUN_RED = 255;
    private static final int SUN_GREEN = 222;
    private static final int SUN_BLUE = 89;
    private static final double SUN_ALPHA = 0.9;
    private static final int SUN_INNER_RED = 255;
    private static final int SUN_INNER_GREEN = 244;
    private static final int SUN_INNER_BLUE = 170;
    private static final double SUN_INNER_ALPHA = 0.65;
    private static final double CLOUD_START_X = 260.0;
    private static final double CLOUD_SPACING = 520.0;
    private static final double CLOUD_BASE_Y = 85.0;
    private static final double CLOUD_VARIATION_STEP = 18.0;
    private static final int CLOUD_VARIATIONS = 3;
    private static final int CLOUD_RED = 255;
    private static final int CLOUD_GREEN = 255;
    private static final int CLOUD_BLUE = 255;
    private static final double CLOUD_ALPHA = 0.78;
    private static final double CLOUD_LEFT_WIDTH = 70.0;
    private static final double CLOUD_LEFT_HEIGHT = 34.0;
    private static final double CLOUD_MIDDLE_X_OFFSET = 35.0;
    private static final double CLOUD_MIDDLE_Y_OFFSET = 18.0;
    private static final double CLOUD_MIDDLE_WIDTH = 78.0;
    private static final double CLOUD_MIDDLE_HEIGHT = 48.0;
    private static final double CLOUD_RIGHT_X_OFFSET = 82.0;
    private static final double CLOUD_RIGHT_WIDTH = 76.0;
    private static final double CLOUD_RIGHT_HEIGHT = 34.0;
    private static final double HILL_START_X = -80.0;
    private static final double HILL_SPACING = 360.0;
    private static final int HILL_FRONT_RED = 84;
    private static final int HILL_FRONT_GREEN = 174;
    private static final int HILL_FRONT_BLUE = 91;
    private static final int HILL_BACK_RED = 63;
    private static final int HILL_BACK_GREEN = 145;
    private static final int HILL_BACK_BLUE = 76;
    private static final double HILL_FRONT_Y_OFFSET = 245.0;
    private static final double HILL_FRONT_WIDTH = 280.0;
    private static final double HILL_FRONT_HEIGHT = 220.0;
    private static final double HILL_BACK_X_OFFSET = 120.0;
    private static final double HILL_BACK_Y_OFFSET = 195.0;
    private static final double HILL_BACK_WIDTH = 260.0;
    private static final double HILL_BACK_HEIGHT = 170.0;
    private static final int GROUND_SHADOW_RED = 67;
    private static final int GROUND_SHADOW_GREEN = 128;
    private static final int GROUND_SHADOW_BLUE = 73;
    private static final double GROUND_SHADOW_ALPHA = 0.45;
    private static final int GROUND_DARK_RED = 42;
    private static final int GROUND_DARK_GREEN = 93;
    private static final int GROUND_DARK_BLUE = 59;
    private static final double GROUND_DARK_ALPHA = 0.35;
    private static final double GROUND_SHADOW_HEIGHT = 175.0;
    private static final double GROUND_DARK_Y = 500.0;
    private static final double GROUND_DARK_HEIGHT = 18.0;

    private enum QuestionReward {
        COIN,
        MUSHROOM,
        STAR
    }

    private final List<AbstractEntity> entities;
    private final List<AbstractEntity> pendingEntities;
    private final List<QuestionReward> questionRewards;
    private final Set<AbstractEntity> playerSafeShells;
    private final Random random;
    private final int levelNumber;
    private final double width;
    private final double height;
    private final double spawnX;
    private final double spawnY;
    private int collectedCoins;
    private int defeatedEnemies;
    private boolean completed;

    private Player player;
    private int wallContactDirection;
    private double wallContactTimer;

    private final CollisionDetector detector;

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
        this.questionRewards = new ArrayList<>();
        this.playerSafeShells = new HashSet<>();
        this.random = new Random();
        this.levelNumber = levelNumber;
        this.width = levelWidth;
        this.height = levelHeight;
        this.spawnX = playerSpawnX;
        this.spawnY = playerSpawnY;
        this.collectedCoins = INITIAL_COUNTER;
        this.defeatedEnemies = INITIAL_COUNTER;
        this.completed = false;
        this.wallContactDirection = INITIAL_COUNTER;
        this.wallContactTimer = INITIAL_COUNTER;
        this.detector = new CollisionDetectorImpl();
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
            removeEntity(playerEntity(this.player));
        }
        this.player = player;
        if (player != null) {
            addEntity(playerEntity(player));
        }
    }

    @Override
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
        return this.entities;
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
        updateWallJumpTimer(deltaTime);

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

        for (final AbstractEntity dynamicEntity : this.entities) {
            if (!(dynamicEntity instanceof AbstractDynamicEntity)) {
                continue;
            }

            final AbstractDynamicEntity movingEntity = (AbstractDynamicEntity) dynamicEntity;
            movingEntity.setOnGround(false);

            if (isEmergingPowerUp(dynamicEntity)) {
                continue;
            }

            for (final AbstractEntity staticEntity : this.entities) {
                if (!(staticEntity instanceof AbstractStaticEntity)) {
                    continue;
                }
                if (!((AbstractStaticEntity) staticEntity).isSolid()) {
                    continue;
                }

                final GameObjectImpl dynamicObj = new GameObjectImpl(
                    (float) movingEntity.getX(),
                    (float) movingEntity.getY(),
                    (float) movingEntity.getWidth(),
                    (float) movingEntity.getHeight()
                );
                final GameObjectImpl staticObj = new GameObjectImpl(
                    (float) staticEntity.getX(),
                    (float) staticEntity.getY(),
                    (float) staticEntity.getWidth(),
                    (float) staticEntity.getHeight()
                );

                final CollisionResult result = detector.getCollisionResult(
                    dynamicObj,
                    staticObj
                );

                final double[] previousPosition = previousPositions.get(dynamicEntity);
                final double previousX = previousPosition == null
                    ? movingEntity.getX()
                    : previousPosition[PREVIOUS_X_INDEX];
                final double previousY = previousPosition == null
                    ? movingEntity.getY()
                    : previousPosition[PREVIOUS_Y_INDEX];

                if (result != null) {
                    handleGoalCollision(dynamicEntity, staticEntity);

                    final double speedXBeforeCollision = movingEntity.getVelocityX();
                    if (isBottomCollisionFromPlayer(movingEntity, staticEntity, result, previousY)) {
                        resolveBottomCollision(movingEntity, staticEntity);
                        hitBlockFromBelow(staticEntity);
                    } else if (isTopCollision(movingEntity, staticEntity, previousY)) {
                        placeOnTopOfBlock(movingEntity, staticEntity);
                    } else if (isSideCollision(result) || isPlayer(dynamicEntity)) {
                        resolveSideCollision(movingEntity, staticEntity, previousX, speedXBeforeCollision);
                        rememberWallContact(dynamicEntity, staticEntity);
                        reverseAfterWallHit(dynamicEntity, speedXBeforeCollision);
                    } else if (result.getSide() == CollisionSide.TOP) {
                        placeOnTopOfBlock(movingEntity, staticEntity);
                    } else if (result.getSide() == CollisionSide.BOTTOM) {
                        resolveBottomCollision(movingEntity, staticEntity);
                    }
                } else if (isStandingOn(movingEntity, staticEntity)) {
                    movingEntity.setOnGround(true);
                }
            }
        }

        handleEnemyLedges();
        handleEnemyBounds();
        handleShellEnemyCollisions();
        updateShellContactProtection();
        handlePlayerEnemyCollisions();
        handlePlayerPowerUpCollisions();
        handlePlayerFallOut();
        handleLevelEndByPosition();

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
        if (!jumpPressed
            || this.player == null
            || this.wallContactTimer <= INITIAL_COUNTER
            || this.wallContactDirection == INITIAL_COUNTER) {
            return;
        }

        final AbstractEntity playerEntity = playerEntity(this.player);
        if (!(playerEntity instanceof AbstractDynamicEntity)) {
            return;
        }

        final AbstractDynamicEntity dynamicPlayer = (AbstractDynamicEntity) playerEntity;
        if (dynamicPlayer.isOnGround()) {
            return;
        }

        dynamicPlayer.setVelocityX(-this.wallContactDirection * WALL_JUMP_SPEED_X);
        dynamicPlayer.setVelocityY(WALL_JUMP_SPEED_Y);
        dynamicPlayer.setOnGround(false);
        this.wallContactTimer = INITIAL_COUNTER;
    }

    private AbstractEntity playerEntity(final Player currentPlayer) {
        return (AbstractEntity) currentPlayer;
    }

    private void moveGroundedEntity(final AbstractDynamicEntity entity, final double deltaTime) {
        entity.setX(entity.getX() + entity.getVelocityX() * deltaTime);
    }

    private boolean isPlayer(final AbstractEntity entity) {
        return this.player != null && entity == playerEntity(this.player);
    }

    private void hitBlockFromBelow(final AbstractEntity entity) {
        if (entity instanceof Block) {
            final Block block = (Block) entity;
            if (block.onHit()) {
                this.pendingEntities.add(createPowerUpFromBlock(block));
            }
        }
    }

    private AbstractEntity createPowerUpFromBlock(final Block block) {
        final QuestionReward reward = takeNextQuestionReward();
        switch (reward) {
            case COIN:
                return new Coin(
                    block.getX() + POWER_UP_COIN_X_OFFSET,
                    block.getY() - POWER_UP_COIN_Y_OFFSET,
                    new BasicPhysicsImpl()
                );
            case STAR:
                return new StarPowerUp(block.getX(), block.getY(), new BasicPhysicsImpl());
            case MUSHROOM:
            default:
                return new MushroomPowerUp(block.getX(), block.getY(), new BasicPhysicsImpl());
        }
    }

    private QuestionReward takeNextQuestionReward() {
        if (this.questionRewards.isEmpty()) {
            refillQuestionRewards();
        }
        return this.questionRewards.remove(INITIAL_COUNTER);
    }

    private void refillQuestionRewards() {
        this.questionRewards.add(QuestionReward.COIN);
        this.questionRewards.add(QuestionReward.COIN);
        this.questionRewards.add(QuestionReward.MUSHROOM);
        this.questionRewards.add(QuestionReward.STAR);
        Collections.shuffle(this.questionRewards, this.random);
    }

    private boolean isEmergingPowerUp(final AbstractEntity entity) {
        return entity instanceof PowerUp && ((PowerUp) entity).isEmerging();
    }

    private boolean isSideCollision(final CollisionResult result) {
        return result.getSide() == CollisionSide.LEFT || result.getSide() == CollisionSide.RIGHT;
    }

    private void handleGoalCollision(final AbstractEntity dynamicEntity, final AbstractEntity staticEntity) {
        if (isPlayer(dynamicEntity) && staticEntity instanceof Pole) {
            completeLevel();
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
        if (this.completed) {
            return;
        }

        this.completed = true;
        for (final AbstractEntity entity : this.entities) {
            if (entity instanceof Flag) {
                ((Flag) entity).lower();
            }
        }
    }

    private void resolveSideCollision(
        final AbstractDynamicEntity dynamicEntity,
        final AbstractEntity staticEntity,
        final double previousX,
        final double speedXBeforeCollision
    ) {
        final double previousRight = previousX + dynamicEntity.getWidth();
        final double staticLeft = staticEntity.getX();
        final double staticRight = staticEntity.getX() + staticEntity.getWidth();

        if (previousRight <= staticLeft || speedXBeforeCollision > INITIAL_COUNTER) {
            dynamicEntity.setX(staticEntity.getX() - dynamicEntity.getWidth());
        } else if (previousX >= staticRight || speedXBeforeCollision < INITIAL_COUNTER) {
            dynamicEntity.setX(staticEntity.getX() + staticEntity.getWidth());
        } else if (dynamicEntity.getX() < staticEntity.getX()) {
            dynamicEntity.setX(staticEntity.getX() - dynamicEntity.getWidth());
        } else {
            dynamicEntity.setX(staticEntity.getX() + staticEntity.getWidth());
        }
        dynamicEntity.setVelocityX(INITIAL_COUNTER);
    }

    private void rememberWallContact(final AbstractEntity dynamicEntity, final AbstractEntity staticEntity) {
        if (!isPlayer(dynamicEntity) || !(dynamicEntity instanceof AbstractDynamicEntity)) {
            return;
        }

        final AbstractDynamicEntity dynamicPlayer = (AbstractDynamicEntity) dynamicEntity;
        if (dynamicPlayer.isOnGround()) {
            return;
        }

        final double playerCenterX = dynamicPlayer.getX() + dynamicPlayer.getWidth() / HALF_DIVISOR;
        final double staticCenterX = staticEntity.getX() + staticEntity.getWidth() / HALF_DIVISOR;
        this.wallContactDirection = playerCenterX < staticCenterX ? 1 : -1;
        this.wallContactTimer = WALL_JUMP_TIME;
    }

    private void updateWallJumpTimer(final double deltaTime) {
        if (this.wallContactTimer > INITIAL_COUNTER) {
            this.wallContactTimer = Math.max(INITIAL_COUNTER, this.wallContactTimer - deltaTime);
            if (this.wallContactTimer == INITIAL_COUNTER) {
                this.wallContactDirection = INITIAL_COUNTER;
            }
        }
    }

    private boolean isTopCollision(
        final AbstractDynamicEntity dynamicEntity,
        final AbstractEntity staticEntity,
        final double previousY
    ) {
        final double previousBottom = previousY + dynamicEntity.getHeight();
        final double dynamicBottom = dynamicEntity.getY() + dynamicEntity.getHeight();
        final double staticTop = staticEntity.getY();
        final boolean cameFromAbove = previousBottom <= staticTop + GROUND_EPSILON
            && dynamicBottom >= staticTop;
        final boolean closeToTop = dynamicBottom >= staticTop
            && dynamicBottom <= staticTop + TOP_COLLISION_TOLERANCE;

        return horizontalOverlap(dynamicEntity, staticEntity) > INITIAL_COUNTER
            && (cameFromAbove || closeToTop && dynamicEntity.getVelocityY() >= INITIAL_COUNTER);
    }

    private void placeOnTopOfBlock(final AbstractDynamicEntity dynamicEntity, final AbstractEntity staticEntity) {
        dynamicEntity.setY(staticEntity.getY() - dynamicEntity.getHeight());
        dynamicEntity.setVelocityY(INITIAL_COUNTER);
        dynamicEntity.setOnGround(true);
    }

    private boolean isBottomCollisionFromPlayer(
        final AbstractDynamicEntity dynamicEntity,
        final AbstractEntity staticEntity,
        final CollisionResult result,
        final double previousY
    ) {
        if (!isPlayer(dynamicEntity)) {
            return false;
        }

        if (result.getSide() == CollisionSide.BOTTOM) {
            return true;
        }

        final double staticBottom = staticEntity.getY() + staticEntity.getHeight();
        final boolean cameFromBelow = previousY >= staticBottom - BOTTOM_COLLISION_TOLERANCE
            && dynamicEntity.getY() <= staticBottom;
        final boolean enoughOverlap = horizontalOverlap(dynamicEntity, staticEntity) >= MIN_BLOCK_HIT_OVERLAP;

        return dynamicEntity.getVelocityY() <= INITIAL_COUNTER && cameFromBelow && enoughOverlap;
    }

    private void resolveBottomCollision(final AbstractDynamicEntity dynamicEntity, final AbstractEntity staticEntity) {
        dynamicEntity.setY(staticEntity.getY() + staticEntity.getHeight());
        dynamicEntity.setVelocityY(INITIAL_COUNTER);
    }

    private void reverseAfterWallHit(final AbstractEntity entity, final double speedXBeforeCollision) {
        if (entity instanceof AbstractDynamicEntity && (entity instanceof Enemy || entity instanceof PowerUp)) {
            ((AbstractDynamicEntity) entity).setVelocityX(-speedXBeforeCollision);
        }
    }

    private boolean isStandingOn(final AbstractDynamicEntity dynamicEntity, final AbstractEntity staticEntity) {
        final double dynamicBottom = dynamicEntity.getY() + dynamicEntity.getHeight();
        final double staticTop = staticEntity.getY();
        return horizontalOverlap(dynamicEntity, staticEntity) > INITIAL_COUNTER
            && dynamicBottom >= staticTop
            && dynamicBottom <= staticTop + GROUND_EPSILON;
    }

    private double horizontalOverlap(final AbstractEntity first, final AbstractEntity second) {
        return Math.min(first.getX() + first.getWidth(), second.getX() + second.getWidth())
            - Math.max(first.getX(), second.getX());
    }

    private void handleEnemyBounds() {
        for (final AbstractEntity entity : this.entities) {
            if (entity instanceof Enemy && entity instanceof AbstractDynamicEntity && entity.isActive()) {
                final AbstractDynamicEntity enemy = (AbstractDynamicEntity) entity;
                if (enemy.getX() <= INITIAL_COUNTER) {
                    enemy.setX(INITIAL_COUNTER);
                    enemy.setVelocityX(Math.abs(enemy.getVelocityX()));
                } else if (enemy.getX() + enemy.getWidth() >= this.width) {
                    enemy.setX(this.width - enemy.getWidth());
                    enemy.setVelocityX(-Math.abs(enemy.getVelocityX()));
                }
            }
        }
    }

    private void handleEnemyLedges() {
        for (final AbstractEntity entity : this.entities) {
            if (!(entity instanceof Enemy) || !(entity instanceof AbstractDynamicEntity) || !entity.isActive()) {
                continue;
            }
            if (isMovingKoopaShell(entity)) {
                continue;
            }

            final AbstractDynamicEntity enemy = (AbstractDynamicEntity) entity;
            if (!enemy.isOnGround() || enemy.getVelocityX() == INITIAL_COUNTER) {
                continue;
            }

            final double direction = Math.signum(enemy.getVelocityX());
            final double probeX = direction > INITIAL_COUNTER
                ? enemy.getX() + enemy.getWidth() + EDGE_CHECK_DISTANCE
                : enemy.getX() - EDGE_CHECK_DISTANCE;
            final double probeY = enemy.getY() + enemy.getHeight() + EDGE_CHECK_DISTANCE;

            if (!hasStaticBlockAt(probeX, probeY)) {
                enemy.setVelocityX(-enemy.getVelocityX());
            }
        }
    }

    private boolean hasStaticBlockAt(final double x, final double y) {
        for (final AbstractEntity entity : this.entities) {
            if (entity instanceof AbstractStaticEntity
                && ((AbstractStaticEntity) entity).isSolid()
                && x >= entity.getX()
                && x <= entity.getX() + entity.getWidth()
                && y >= entity.getY()
                && y <= entity.getY() + entity.getHeight()) {
                return true;
            }
        }
        return false;
    }

    private void handleShellEnemyCollisions() {
        for (final AbstractEntity shellEntity : this.entities) {
            if (!(shellEntity instanceof Koopa) || !((Koopa) shellEntity).canKillEnemies()) {
                continue;
            }

            for (final AbstractEntity enemyEntity : this.entities) {
                if (enemyEntity == shellEntity || !(enemyEntity instanceof Enemy) || !enemyEntity.isActive()) {
                    continue;
                }

                if (overlaps(shellEntity, enemyEntity)) {
                    defeatEnemy(enemyEntity);
                }
            }
        }
    }

    private void handlePlayerEnemyCollisions() {
        if (this.player == null || !this.player.isActive()) {
            return;
        }

        final AbstractEntity playerEntity = playerEntity(this.player);
        for (final AbstractEntity entity : this.entities) {
            if (entity == playerEntity || !(entity instanceof Enemy) || !entity.isActive()) {
                continue;
            }

            if (overlaps(this.player, entity)) {
                handlePlayerEnemyCollision(playerEntity, entity);
            }
        }
    }

    private void handlePlayerEnemyCollision(final AbstractEntity playerEntity, final AbstractEntity enemyEntity) {
        if (this.player.isInvincible()) {
            defeatEnemy(enemyEntity);
            return;
        }

        if (isMovingKoopaShell(enemyEntity)) {
            if (isPlayerStompingEnemy(playerEntity, enemyEntity)) {
                stompMovingShell(enemyEntity, (AbstractDynamicEntity) playerEntity);
            } else if (this.playerSafeShells.contains(enemyEntity)) {
                return;
            } else {
                this.player.die();
            }
            return;
        }

        if (tryKickKoopaShell(enemyEntity)) {
            return;
        }

        if (isPlayerStompingEnemy(playerEntity, enemyEntity)) {
            stompEnemy(enemyEntity, (AbstractDynamicEntity) playerEntity);
        } else if (((Enemy) enemyEntity).hitsPlayer() && this.player.takeDamage()) {
            this.player.die();
        }
    }

    private boolean isMovingKoopaShell(final AbstractEntity enemyEntity) {
        return enemyEntity instanceof Koopa && ((Koopa) enemyEntity).canKillEnemies();
    }

    private boolean tryKickKoopaShell(final AbstractEntity enemyEntity) {
        if (enemyEntity instanceof Koopa) {
            final Koopa koopa = (Koopa) enemyEntity;
            if (koopa.getState() == Koopa.KoopaState.SHELL) {
                koopa.kick(this.player.getX() < koopa.getX());
                this.playerSafeShells.add(enemyEntity);
                return true;
            }
        }
        return false;
    }

    private void updateShellContactProtection() {
        if (this.player == null) {
            this.playerSafeShells.clear();
            return;
        }
        this.playerSafeShells.removeIf(shell -> !shell.isActive() || !overlaps(this.player, shell));
    }

    private boolean isPlayerStompingEnemy(final AbstractEntity playerEntity, final AbstractEntity enemyEntity) {
        if (!(playerEntity instanceof AbstractDynamicEntity)) {
            return false;
        }
        final AbstractDynamicEntity dynamicPlayer = (AbstractDynamicEntity) playerEntity;
        final double playerBottom = playerEntity.getY() + playerEntity.getHeight();
        final double enemyMiddle = enemyEntity.getY() + enemyEntity.getHeight() / HALF_DIVISOR;
        return dynamicPlayer.getVelocityY() >= INITIAL_COUNTER && playerBottom <= enemyMiddle + STOMP_TOLERANCE;
    }

    private void stompEnemy(final AbstractEntity enemyEntity, final AbstractDynamicEntity playerEntity) {
        if (enemyEntity instanceof Goomba) {
            defeatEnemy(enemyEntity);
        } else if (enemyEntity instanceof Koopa) {
            final Koopa koopa = (Koopa) enemyEntity;
            if (koopa.getState() == Koopa.KoopaState.SHELL) {
                koopa.kick(this.player.getX() < koopa.getX());
            } else {
                koopa.stomp();
            }
        }
        playerEntity.setVelocityY(PLAYER_BOUNCE_AFTER_STOMP);
    }

    private void stompMovingShell(final AbstractEntity enemyEntity, final AbstractDynamicEntity playerEntity) {
        defeatEnemy(enemyEntity);
        playerEntity.setVelocityY(PLAYER_BOUNCE_AFTER_STOMP);
    }

    private void defeatEnemy(final AbstractEntity enemyEntity) {
        if (enemyEntity instanceof Goomba) {
            final Goomba goomba = (Goomba) enemyEntity;
            if (goomba.getState() != Goomba.GoombaState.SQUISHED) {
                goomba.squish();
                this.defeatedEnemies++;
            }
        } else {
            enemyEntity.destroy();
            this.defeatedEnemies++;
        }
    }

    private void handlePlayerPowerUpCollisions() {
        if (this.player == null || !this.player.isActive()) {
            return;
        }

        for (final AbstractEntity entity : this.entities) {
            if (entity instanceof PowerUp && entity.isActive() && overlaps(this.player, entity)) {
                final PowerUp powerUp = (PowerUp) entity;
                if (canCollectPowerUp(powerUp, entity)) {
                    powerUp.collect(this.player);
                }
            }
        }
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

    private boolean overlaps(final AbstractEntity first, final AbstractEntity second) {
        return first.getX() < second.getX() + second.getWidth()
            && first.getX() + first.getWidth() > second.getX()
            && first.getY() < second.getY() + second.getHeight()
            && first.getY() + first.getHeight() > second.getY();
    }

    @Override
    public void render(final GraphicsContext gc) {
        renderBackground(gc);
        for (final AbstractEntity entity : this.entities) {
            if (entity.isActive()) {
                entity.render(gc);
            }
        }
    }

    private void renderBackground(final GraphicsContext gc) {
        final double backgroundWidth = Math.max(this.width, gc.getCanvas().getWidth());
        final double backgroundHeight = Math.max(this.height, gc.getCanvas().getHeight());

        gc.setFill(getSkyColor());
        gc.fillRect(ORIGIN, ORIGIN, backgroundWidth, backgroundHeight);
        drawSun(gc);
        drawClouds(gc, backgroundWidth);
        drawHills(gc, backgroundWidth, backgroundHeight);
        drawGroundShadow(gc, backgroundWidth, backgroundHeight);
    }

    private Color getSkyColor() {
        switch (this.levelNumber) {
            case LEVEL_TWO:
                return Color.rgb(LEVEL_TWO_SKY_RED, LEVEL_TWO_SKY_GREEN, LEVEL_TWO_SKY_BLUE);
            case LEVEL_THREE:
                return Color.rgb(LEVEL_THREE_SKY_RED, LEVEL_THREE_SKY_GREEN, LEVEL_THREE_SKY_BLUE);
            default:
                return Color.rgb(LEVEL_ONE_SKY_RED, LEVEL_ONE_SKY_GREEN, LEVEL_ONE_SKY_BLUE);
        }
    }

    private void drawSun(final GraphicsContext gc) {
        final double sunX = SUN_BASE_X + this.levelNumber * SUN_LEVEL_X_STEP;
        final double sunY = SUN_BASE_Y + this.levelNumber * SUN_LEVEL_Y_STEP;
        gc.setFill(Color.rgb(SUN_RED, SUN_GREEN, SUN_BLUE, SUN_ALPHA));
        gc.fillOval(sunX, sunY, SUN_SIZE, SUN_SIZE);
        gc.setFill(Color.rgb(SUN_INNER_RED, SUN_INNER_GREEN, SUN_INNER_BLUE, SUN_INNER_ALPHA));
        gc.fillOval(
            sunX + SUN_INNER_OFFSET,
            sunY + SUN_INNER_OFFSET,
            SUN_INNER_SIZE,
            SUN_INNER_SIZE
        );
    }

    private void drawClouds(final GraphicsContext gc, final double backgroundWidth) {
        int cloudIndex = INITIAL_COUNTER;
        for (double x = CLOUD_START_X; x < backgroundWidth; x += CLOUD_SPACING) {
            final double y = CLOUD_BASE_Y + cloudIndex % CLOUD_VARIATIONS * CLOUD_VARIATION_STEP;
            gc.setFill(Color.rgb(CLOUD_RED, CLOUD_GREEN, CLOUD_BLUE, CLOUD_ALPHA));
            gc.fillOval(x, y, CLOUD_LEFT_WIDTH, CLOUD_LEFT_HEIGHT);
            gc.fillOval(
                x + CLOUD_MIDDLE_X_OFFSET,
                y - CLOUD_MIDDLE_Y_OFFSET,
                CLOUD_MIDDLE_WIDTH,
                CLOUD_MIDDLE_HEIGHT
            );
            gc.fillOval(x + CLOUD_RIGHT_X_OFFSET, y, CLOUD_RIGHT_WIDTH, CLOUD_RIGHT_HEIGHT);
            cloudIndex++;
        }
    }

    private void drawHills(final GraphicsContext gc, final double backgroundWidth, final double backgroundHeight) {
        for (double x = HILL_START_X; x < backgroundWidth; x += HILL_SPACING) {
            gc.setFill(Color.rgb(HILL_FRONT_RED, HILL_FRONT_GREEN, HILL_FRONT_BLUE));
            gc.fillOval(x, backgroundHeight - HILL_FRONT_Y_OFFSET, HILL_FRONT_WIDTH, HILL_FRONT_HEIGHT);
            gc.setFill(Color.rgb(HILL_BACK_RED, HILL_BACK_GREEN, HILL_BACK_BLUE));
            gc.fillOval(
                x + HILL_BACK_X_OFFSET,
                backgroundHeight - HILL_BACK_Y_OFFSET,
                HILL_BACK_WIDTH,
                HILL_BACK_HEIGHT
            );
        }
    }

    private void drawGroundShadow(
        final GraphicsContext gc,
        final double backgroundWidth,
        final double backgroundHeight
    ) {
        gc.setFill(Color.rgb(GROUND_SHADOW_RED, GROUND_SHADOW_GREEN, GROUND_SHADOW_BLUE, GROUND_SHADOW_ALPHA));
        gc.fillRect(ORIGIN, backgroundHeight - GROUND_SHADOW_HEIGHT, backgroundWidth, GROUND_SHADOW_HEIGHT);
        gc.setFill(Color.rgb(GROUND_DARK_RED, GROUND_DARK_GREEN, GROUND_DARK_BLUE, GROUND_DARK_ALPHA));
        gc.fillRect(ORIGIN, GROUND_DARK_Y, backgroundWidth, GROUND_DARK_HEIGHT);
    }

}
