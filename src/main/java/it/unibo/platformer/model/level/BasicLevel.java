package it.unibo.platformer.model.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import it.unibo.platformer.model.physics.api.CollisionDetector;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import it.unibo.platformer.model.physics.impl.CollisionDetectorImpl;
import it.unibo.platformer.model.physics.impl.CollisionResult;
import it.unibo.platformer.model.physics.impl.CollisionSide;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// CHECKSTYLE: MagicNumber OFF
/**
 * Basic implementation of a playable platform level.
 */
public final class BasicLevel implements Level {
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

    private enum QuestionReward {
        COIN,
        MUSHROOM,
        STAR
    }

    private final List<AbstractEntity> entities;
    private final List<AbstractEntity> pendingEntities;
    private final List<QuestionReward> questionRewards;
    private final Random random;
    private final int levelNumber;
    private final double width;
    private final double height;
    private final double spawnX;
    private final double spawnY;
    private int collectedCoins;

    private Player player;
    private int wallContactDirection;
    private double wallContactTimer;

    private final CollisionDetector detector;

    /**
     * Creates the default test level.
     */
    public BasicLevel() {
        this(1, 2000, 720, 100, 300);
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
        this.random = new Random();
        this.levelNumber = levelNumber;
        this.width = levelWidth;
        this.height = levelHeight;
        this.spawnX = playerSpawnX;
        this.spawnY = playerSpawnY;
        this.collectedCoins = 0;
        this.wallContactDirection = 0;
        this.wallContactTimer = 0;
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
        this.collectedCoins = 0;
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
                final double previousX = previousPosition == null ? movingEntity.getX() : previousPosition[0];
                final double previousY = previousPosition == null ? movingEntity.getY() : previousPosition[1];

                if (result != null) {
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
        handlePlayerEnemyCollisions();
        handlePlayerPowerUpCollisions();
        handlePlayerFallOut();

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
        if (!jumpPressed || this.player == null || this.wallContactTimer <= 0 || this.wallContactDirection == 0) {
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
        this.wallContactTimer = 0;
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
                return new Coin(block.getX() + 8, block.getY() - 24, new BasicPhysicsImpl());
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
        return this.questionRewards.remove(0);
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

    private void resolveSideCollision(
        final AbstractDynamicEntity dynamicEntity,
        final AbstractEntity staticEntity,
        final double previousX,
        final double speedXBeforeCollision
    ) {
        final double previousRight = previousX + dynamicEntity.getWidth();
        final double staticLeft = staticEntity.getX();
        final double staticRight = staticEntity.getX() + staticEntity.getWidth();

        if (previousRight <= staticLeft || speedXBeforeCollision > 0) {
            dynamicEntity.setX(staticEntity.getX() - dynamicEntity.getWidth());
        } else if (previousX >= staticRight || speedXBeforeCollision < 0) {
            dynamicEntity.setX(staticEntity.getX() + staticEntity.getWidth());
        } else if (dynamicEntity.getX() < staticEntity.getX()) {
            dynamicEntity.setX(staticEntity.getX() - dynamicEntity.getWidth());
        } else {
            dynamicEntity.setX(staticEntity.getX() + staticEntity.getWidth());
        }
        dynamicEntity.setVelocityX(0);
    }

    private void rememberWallContact(final AbstractEntity dynamicEntity, final AbstractEntity staticEntity) {
        if (!isPlayer(dynamicEntity) || !(dynamicEntity instanceof AbstractDynamicEntity)) {
            return;
        }

        final AbstractDynamicEntity dynamicPlayer = (AbstractDynamicEntity) dynamicEntity;
        if (dynamicPlayer.isOnGround()) {
            return;
        }

        final double playerCenterX = dynamicPlayer.getX() + dynamicPlayer.getWidth() / 2.0;
        final double staticCenterX = staticEntity.getX() + staticEntity.getWidth() / 2.0;
        this.wallContactDirection = playerCenterX < staticCenterX ? 1 : -1;
        this.wallContactTimer = WALL_JUMP_TIME;
    }

    private void updateWallJumpTimer(final double deltaTime) {
        if (this.wallContactTimer > 0) {
            this.wallContactTimer = Math.max(0, this.wallContactTimer - deltaTime);
            if (this.wallContactTimer == 0) {
                this.wallContactDirection = 0;
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

        return horizontalOverlap(dynamicEntity, staticEntity) > 0
            && (cameFromAbove || closeToTop && dynamicEntity.getVelocityY() >= 0);
    }

    private void placeOnTopOfBlock(final AbstractDynamicEntity dynamicEntity, final AbstractEntity staticEntity) {
        dynamicEntity.setY(staticEntity.getY() - dynamicEntity.getHeight());
        dynamicEntity.setVelocityY(0);
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

        return dynamicEntity.getVelocityY() <= 0 && cameFromBelow && enoughOverlap;
    }

    private void resolveBottomCollision(final AbstractDynamicEntity dynamicEntity, final AbstractEntity staticEntity) {
        dynamicEntity.setY(staticEntity.getY() + staticEntity.getHeight());
        dynamicEntity.setVelocityY(0);
    }

    private void reverseAfterWallHit(final AbstractEntity entity, final double speedXBeforeCollision) {
        if (entity instanceof AbstractDynamicEntity && (entity instanceof Enemy || entity instanceof PowerUp)) {
            ((AbstractDynamicEntity) entity).setVelocityX(-speedXBeforeCollision);
        }
    }

    private boolean isStandingOn(final AbstractDynamicEntity dynamicEntity, final AbstractEntity staticEntity) {
        final double dynamicBottom = dynamicEntity.getY() + dynamicEntity.getHeight();
        final double staticTop = staticEntity.getY();
        return horizontalOverlap(dynamicEntity, staticEntity) > 0
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
                if (enemy.getX() <= 0) {
                    enemy.setX(0);
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

            final AbstractDynamicEntity enemy = (AbstractDynamicEntity) entity;
            if (!enemy.isOnGround() || enemy.getVelocityX() == 0) {
                continue;
            }

            final double direction = Math.signum(enemy.getVelocityX());
            final double probeX = direction > 0
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
                return true;
            }
        }
        return false;
    }

    private boolean isPlayerStompingEnemy(final AbstractEntity playerEntity, final AbstractEntity enemyEntity) {
        if (!(playerEntity instanceof AbstractDynamicEntity)) {
            return false;
        }
        final AbstractDynamicEntity dynamicPlayer = (AbstractDynamicEntity) playerEntity;
        final double playerBottom = playerEntity.getY() + playerEntity.getHeight();
        final double enemyMiddle = enemyEntity.getY() + enemyEntity.getHeight() / 2.0;
        return dynamicPlayer.getVelocityY() >= 0 && playerBottom <= enemyMiddle + STOMP_TOLERANCE;
    }

    private void stompEnemy(final AbstractEntity enemyEntity, final AbstractDynamicEntity playerEntity) {
        if (enemyEntity instanceof Goomba) {
            ((Goomba) enemyEntity).squish();
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

    private void defeatEnemy(final AbstractEntity enemyEntity) {
        if (enemyEntity instanceof Goomba) {
            ((Goomba) enemyEntity).squish();
        } else {
            enemyEntity.destroy();
        }
    }

    private void handlePlayerPowerUpCollisions() {
        if (this.player == null || !this.player.isActive()) {
            return;
        }

        for (final AbstractEntity entity : this.entities) {
            if (entity instanceof PowerUp && entity.isActive() && overlaps(this.player, entity)) {
                final PowerUp powerUp = (PowerUp) entity;
                if (!powerUp.isEmerging()) {
                    powerUp.collect(this.player);
                }
            }
        }
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
        gc.fillRect(0, 0, backgroundWidth, backgroundHeight);
        drawSun(gc);
        drawClouds(gc, backgroundWidth);
        drawHills(gc, backgroundWidth, backgroundHeight);
        drawGroundShadow(gc, backgroundWidth, backgroundHeight);
    }

    private Color getSkyColor() {
        switch (this.levelNumber) {
            case 2:
                return Color.rgb(99, 170, 224);
            case 3:
                return Color.rgb(78, 126, 193);
            default:
                return Color.rgb(113, 190, 244);
        }
    }

    private void drawSun(final GraphicsContext gc) {
        final double sunX = 180 + this.levelNumber * 45;
        final double sunY = 80 + this.levelNumber * 10;
        gc.setFill(Color.rgb(255, 222, 89, 0.9));
        gc.fillOval(sunX, sunY, 70, 70);
        gc.setFill(Color.rgb(255, 244, 170, 0.65));
        gc.fillOval(sunX + 12, sunY + 12, 46, 46);
    }

    private void drawClouds(final GraphicsContext gc, final double backgroundWidth) {
        for (double x = 260; x < backgroundWidth; x += 520) {
            final double y = 85 + (x % 3) * 18;
            gc.setFill(Color.rgb(255, 255, 255, 0.78));
            gc.fillOval(x, y, 70, 34);
            gc.fillOval(x + 35, y - 18, 78, 48);
            gc.fillOval(x + 82, y, 76, 34);
        }
    }

    private void drawHills(final GraphicsContext gc, final double backgroundWidth, final double backgroundHeight) {
        for (double x = -80; x < backgroundWidth; x += 360) {
            gc.setFill(Color.rgb(84, 174, 91));
            gc.fillOval(x, backgroundHeight - 245, 280, 220);
            gc.setFill(Color.rgb(63, 145, 76));
            gc.fillOval(x + 120, backgroundHeight - 195, 260, 170);
        }
    }

    private void drawGroundShadow(
        final GraphicsContext gc,
        final double backgroundWidth,
        final double backgroundHeight
    ) {
        gc.setFill(Color.rgb(67, 128, 73, 0.45));
        gc.fillRect(0, backgroundHeight - 175, backgroundWidth, 175);
        gc.setFill(Color.rgb(42, 93, 59, 0.35));
        gc.fillRect(0, 500, backgroundWidth, 18);
    }

}
// CHECKSTYLE: MagicNumber ON
