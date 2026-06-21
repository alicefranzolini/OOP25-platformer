package it.unibo.platformer.model.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unibo.platformer.model.entities.AbstractDynamicEntity;
import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.AbstractStaticEntity;
import it.unibo.platformer.model.entities.enemies.Enemy;
import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.entities.powerup.PowerUp;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.entities.world.Pole;
import it.unibo.platformer.model.physics.api.CollisionDetector;
import it.unibo.platformer.model.physics.impl.CollisionDetectorImpl;
import it.unibo.platformer.model.physics.impl.CollisionResult;
import it.unibo.platformer.model.physics.impl.CollisionSide;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;

/**
 * Resolves collisions between dynamic entities and the static level world.
 */
final class LevelCollisionManager {

    private static final int INITIAL_VALUE = 0;
    private static final int PREVIOUS_X_INDEX = 0;
    private static final int PREVIOUS_Y_INDEX = 1;
    private static final double HALF_DIVISOR = 2.0;
    private static final double GROUND_EPSILON = 2.0;
    private static final double TOP_COLLISION_TOLERANCE = 10.0;
    private static final double BOTTOM_COLLISION_TOLERANCE = 18.0;
    private static final double MIN_BLOCK_HIT_OVERLAP = 6.0;
    private static final double WALL_JUMP_SPEED_X = 260.0;
    private static final double WALL_JUMP_SPEED_Y = -430.0;
    private static final double WALL_JUMP_TIME = 0.18;

    private final CollisionDetector detector;
    private final List<Block> hitBlocks;
    private int wallContactDirection;
    private double wallContactTimer;
    private boolean goalReached;

    LevelCollisionManager() {
        this.detector = new CollisionDetectorImpl();
        this.hitBlocks = new ArrayList<>();
    }

    void updateWallJumpTimer(final double deltaTime) {
        if (this.wallContactTimer > INITIAL_VALUE) {
            this.wallContactTimer = Math.max(INITIAL_VALUE, this.wallContactTimer - deltaTime);
            if (this.wallContactTimer == INITIAL_VALUE) {
                this.wallContactDirection = INITIAL_VALUE;
            }
        }
    }

    void resolveWorldCollisions(
        final List<AbstractEntity> entities,
        final Player player,
        final Map<AbstractEntity, double[]> previousPositions
    ) {
        this.hitBlocks.clear();
        this.goalReached = false;
        for (final AbstractEntity dynamicEntity : entities) {
            if (!(dynamicEntity instanceof AbstractDynamicEntity)) {
                continue;
            }

            final AbstractDynamicEntity movingEntity = (AbstractDynamicEntity) dynamicEntity;
            movingEntity.setOnGround(false);

            if (isEmergingPowerUp(dynamicEntity)) {
                continue;
            }

            for (final AbstractEntity staticEntity : entities) {
                if (!(staticEntity instanceof AbstractStaticEntity)
                    || !((AbstractStaticEntity) staticEntity).isSolid()) {
                    continue;
                }
                resolveCollision(
                    movingEntity,
                    staticEntity,
                    player,
                    previousPositions.get(dynamicEntity)
                );
            }
        }
    }

    List<Block> getHitBlocks() {
        return this.hitBlocks;
    }

    boolean isGoalReached() {
        return this.goalReached;
    }

    void handleJumpPressed(final boolean jumpPressed, final Player player) {
        if (!jumpPressed
            || player == null
            || this.wallContactTimer <= INITIAL_VALUE
            || this.wallContactDirection == INITIAL_VALUE) {
            return;
        }

        final AbstractEntity playerEntity = (AbstractEntity) player;
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
        this.wallContactTimer = INITIAL_VALUE;
    }

    private void resolveCollision(
        final AbstractDynamicEntity movingEntity,
        final AbstractEntity staticEntity,
        final Player player,
        final double[] previousPosition
    ) {
        final CollisionResult result = this.detector.getCollisionResult(
            createGameObject(movingEntity),
            createGameObject(staticEntity)
        );
        final double previousX = previousPosition == null
            ? movingEntity.getX()
            : previousPosition[PREVIOUS_X_INDEX];
        final double previousY = previousPosition == null
            ? movingEntity.getY()
            : previousPosition[PREVIOUS_Y_INDEX];

        if (result == null) {
            if (isStandingOn(movingEntity, staticEntity)) {
                movingEntity.setOnGround(true);
            }
            return;
        }

        if (isPlayer(movingEntity, player) && staticEntity instanceof Pole) {
            this.goalReached = true;
        }

        final double speedXBeforeCollision = movingEntity.getVelocityX();
        if (isBottomCollisionFromPlayer(movingEntity, staticEntity, result, previousY, player)) {
            resolveBottomCollision(movingEntity, staticEntity);
            if (staticEntity instanceof Block) {
                this.hitBlocks.add((Block) staticEntity);
            }
        } else if (isTopCollision(movingEntity, staticEntity, previousY)) {
            placeOnTopOfBlock(movingEntity, staticEntity);
        } else if (isSideCollision(result) || isPlayer(movingEntity, player)) {
            resolveSideCollision(movingEntity, staticEntity, previousX, speedXBeforeCollision);
            rememberWallContact(movingEntity, staticEntity, player);
            reverseAfterWallHit(movingEntity, speedXBeforeCollision);
        } else if (result.getSide() == CollisionSide.TOP) {
            placeOnTopOfBlock(movingEntity, staticEntity);
        } else if (result.getSide() == CollisionSide.BOTTOM) {
            resolveBottomCollision(movingEntity, staticEntity);
        }
    }

    private GameObjectImpl createGameObject(final AbstractEntity entity) {
        return new GameObjectImpl(
            (float) entity.getX(),
            (float) entity.getY(),
            (float) entity.getWidth(),
            (float) entity.getHeight()
        );
    }

    private boolean isEmergingPowerUp(final AbstractEntity entity) {
        return entity instanceof PowerUp && ((PowerUp) entity).isEmerging();
    }

    private boolean isPlayer(final AbstractEntity entity, final Player player) {
        return player != null && entity == (AbstractEntity) player;
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

        if (previousRight <= staticLeft || speedXBeforeCollision > INITIAL_VALUE) {
            dynamicEntity.setX(staticEntity.getX() - dynamicEntity.getWidth());
        } else if (previousX >= staticRight || speedXBeforeCollision < INITIAL_VALUE) {
            dynamicEntity.setX(staticEntity.getX() + staticEntity.getWidth());
        } else if (dynamicEntity.getX() < staticEntity.getX()) {
            dynamicEntity.setX(staticEntity.getX() - dynamicEntity.getWidth());
        } else {
            dynamicEntity.setX(staticEntity.getX() + staticEntity.getWidth());
        }
        dynamicEntity.setVelocityX(INITIAL_VALUE);
    }

    private void rememberWallContact(
        final AbstractEntity dynamicEntity,
        final AbstractEntity staticEntity,
        final Player player
    ) {
        if (!isPlayer(dynamicEntity, player) || !(dynamicEntity instanceof AbstractDynamicEntity)) {
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

        return horizontalOverlap(dynamicEntity, staticEntity) > INITIAL_VALUE
            && (cameFromAbove || closeToTop && dynamicEntity.getVelocityY() >= INITIAL_VALUE);
    }

    private void placeOnTopOfBlock(
        final AbstractDynamicEntity dynamicEntity,
        final AbstractEntity staticEntity
    ) {
        dynamicEntity.setY(staticEntity.getY() - dynamicEntity.getHeight());
        dynamicEntity.setVelocityY(INITIAL_VALUE);
        dynamicEntity.setOnGround(true);
    }

    private boolean isBottomCollisionFromPlayer(
        final AbstractDynamicEntity dynamicEntity,
        final AbstractEntity staticEntity,
        final CollisionResult result,
        final double previousY,
        final Player player
    ) {
        if (!isPlayer(dynamicEntity, player)) {
            return false;
        }
        if (result.getSide() == CollisionSide.BOTTOM) {
            return true;
        }

        final double staticBottom = staticEntity.getY() + staticEntity.getHeight();
        final boolean cameFromBelow = previousY >= staticBottom - BOTTOM_COLLISION_TOLERANCE
            && dynamicEntity.getY() <= staticBottom;
        final boolean enoughOverlap = horizontalOverlap(dynamicEntity, staticEntity) >= MIN_BLOCK_HIT_OVERLAP;

        return dynamicEntity.getVelocityY() <= INITIAL_VALUE && cameFromBelow && enoughOverlap;
    }

    private void resolveBottomCollision(
        final AbstractDynamicEntity dynamicEntity,
        final AbstractEntity staticEntity
    ) {
        dynamicEntity.setY(staticEntity.getY() + staticEntity.getHeight());
        dynamicEntity.setVelocityY(INITIAL_VALUE);
    }

    private void reverseAfterWallHit(final AbstractEntity entity, final double speedXBeforeCollision) {
        if (entity instanceof AbstractDynamicEntity && (entity instanceof Enemy || entity instanceof PowerUp)) {
            ((AbstractDynamicEntity) entity).setVelocityX(-speedXBeforeCollision);
        }
    }

    private boolean isStandingOn(
        final AbstractDynamicEntity dynamicEntity,
        final AbstractEntity staticEntity
    ) {
        final double dynamicBottom = dynamicEntity.getY() + dynamicEntity.getHeight();
        final double staticTop = staticEntity.getY();
        return horizontalOverlap(dynamicEntity, staticEntity) > INITIAL_VALUE
            && dynamicBottom >= staticTop
            && dynamicBottom <= staticTop + GROUND_EPSILON;
    }

    private double horizontalOverlap(final AbstractEntity first, final AbstractEntity second) {
        return Math.min(first.getX() + first.getWidth(), second.getX() + second.getWidth())
            - Math.max(first.getX(), second.getX());
    }
}
