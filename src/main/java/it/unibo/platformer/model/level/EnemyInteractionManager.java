package it.unibo.platformer.model.level;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.unibo.platformer.model.entities.AbstractDynamicEntity;
import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.AbstractStaticEntity;
import it.unibo.platformer.model.entities.enemies.Enemy;
import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.entities.enemies.Koopa;
import it.unibo.platformer.model.entities.players.Player;

/**
 * Handles enemy movement rules and contacts with the player or shells.
 */
final class EnemyInteractionManager {

    private static final int INITIAL_COUNT = 0;
    private static final double HALF_DIVISOR = 2.0;
    private static final double STOMP_TOLERANCE = 8.0;
    private static final double PLAYER_BOUNCE_AFTER_STOMP = -250.0;
    private static final double EDGE_CHECK_DISTANCE = 8.0;

    private final Set<AbstractEntity> playerSafeShells;
    private int defeatedEnemies;

    EnemyInteractionManager() {
        this.playerSafeShells = new HashSet<>();
    }

    int update(
        final List<AbstractEntity> entities,
        final Player player,
        final double levelWidth
    ) {
        this.defeatedEnemies = INITIAL_COUNT;
        handleEnemyLedges(entities);
        handleEnemyBounds(entities, levelWidth);
        handleShellEnemyCollisions(entities);
        updateShellContactProtection(player);
        handlePlayerEnemyCollisions(entities, player);
        return this.defeatedEnemies;
    }

    private void handleEnemyBounds(final List<AbstractEntity> entities, final double levelWidth) {
        for (final AbstractEntity entity : entities) {
            if (entity instanceof Enemy && entity instanceof AbstractDynamicEntity && entity.isActive()) {
                final AbstractDynamicEntity enemy = (AbstractDynamicEntity) entity;
                if (enemy.getX() <= INITIAL_COUNT) {
                    enemy.setX(INITIAL_COUNT);
                    enemy.setVelocityX(Math.abs(enemy.getVelocityX()));
                } else if (enemy.getX() + enemy.getWidth() >= levelWidth) {
                    enemy.setX(levelWidth - enemy.getWidth());
                    enemy.setVelocityX(-Math.abs(enemy.getVelocityX()));
                }
            }
        }
    }

    private void handleEnemyLedges(final List<AbstractEntity> entities) {
        for (final AbstractEntity entity : entities) {
            if (!(entity instanceof Enemy) || !(entity instanceof AbstractDynamicEntity) || !entity.isActive()) {
                continue;
            }
            if (isMovingKoopaShell(entity)) {
                continue;
            }

            final AbstractDynamicEntity enemy = (AbstractDynamicEntity) entity;
            if (!enemy.isOnGround() || enemy.getVelocityX() == INITIAL_COUNT) {
                continue;
            }

            final double direction = Math.signum(enemy.getVelocityX());
            final double probeX = direction > INITIAL_COUNT
                ? enemy.getX() + enemy.getWidth() + EDGE_CHECK_DISTANCE
                : enemy.getX() - EDGE_CHECK_DISTANCE;
            final double probeY = enemy.getY() + enemy.getHeight() + EDGE_CHECK_DISTANCE;

            if (!hasStaticBlockAt(entities, probeX, probeY)) {
                enemy.setVelocityX(-enemy.getVelocityX());
            }
        }
    }

    private boolean hasStaticBlockAt(
        final List<AbstractEntity> entities,
        final double x,
        final double y
    ) {
        for (final AbstractEntity entity : entities) {
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

    private void handleShellEnemyCollisions(final List<AbstractEntity> entities) {
        for (final AbstractEntity shellEntity : entities) {
            if (!(shellEntity instanceof Koopa) || !((Koopa) shellEntity).canKillEnemies()) {
                continue;
            }

            for (final AbstractEntity enemyEntity : entities) {
                if (enemyEntity == shellEntity || !(enemyEntity instanceof Enemy) || !enemyEntity.isActive()) {
                    continue;
                }
                if (overlaps(shellEntity, enemyEntity)) {
                    defeatEnemy(enemyEntity);
                }
            }
        }
    }

    private void handlePlayerEnemyCollisions(
        final List<AbstractEntity> entities,
        final Player player
    ) {
        if (player == null || !player.isActive()) {
            return;
        }

        final AbstractEntity playerEntity = (AbstractEntity) player;
        for (final AbstractEntity entity : entities) {
            if (entity == playerEntity || !(entity instanceof Enemy) || !entity.isActive()) {
                continue;
            }
            if (overlaps(player, entity)) {
                handlePlayerEnemyCollision(playerEntity, entity, player);
            }
        }
    }

    private void handlePlayerEnemyCollision(
        final AbstractEntity playerEntity,
        final AbstractEntity enemyEntity,
        final Player player
    ) {
        if (player.isInvincible()) {
            defeatEnemy(enemyEntity);
            return;
        }

        if (isMovingKoopaShell(enemyEntity)) {
            if (isPlayerStompingEnemy(playerEntity, enemyEntity)) {
                stompMovingShell(enemyEntity, (AbstractDynamicEntity) playerEntity);
            } else if (!this.playerSafeShells.contains(enemyEntity)) {
                player.die();
            }
            return;
        }

        if (tryKickKoopaShell(enemyEntity, player)) {
            return;
        }

        if (isPlayerStompingEnemy(playerEntity, enemyEntity)) {
            stompEnemy(enemyEntity, (AbstractDynamicEntity) playerEntity, player);
        } else if (((Enemy) enemyEntity).hitsPlayer() && player.takeDamage()) {
            player.die();
        }
    }

    private boolean isMovingKoopaShell(final AbstractEntity enemyEntity) {
        return enemyEntity instanceof Koopa && ((Koopa) enemyEntity).canKillEnemies();
    }

    private boolean tryKickKoopaShell(final AbstractEntity enemyEntity, final Player player) {
        if (enemyEntity instanceof Koopa) {
            final Koopa koopa = (Koopa) enemyEntity;
            if (koopa.getState() == Koopa.KoopaState.SHELL) {
                koopa.kick(player.getX() < koopa.getX());
                this.playerSafeShells.add(enemyEntity);
                return true;
            }
        }
        return false;
    }

    private void updateShellContactProtection(final Player player) {
        if (player == null) {
            this.playerSafeShells.clear();
            return;
        }
        this.playerSafeShells.removeIf(shell -> !shell.isActive() || !overlaps(player, shell));
    }

    private boolean isPlayerStompingEnemy(
        final AbstractEntity playerEntity,
        final AbstractEntity enemyEntity
    ) {
        if (!(playerEntity instanceof AbstractDynamicEntity)) {
            return false;
        }
        final AbstractDynamicEntity dynamicPlayer = (AbstractDynamicEntity) playerEntity;
        final double playerBottom = playerEntity.getY() + playerEntity.getHeight();
        final double enemyMiddle = enemyEntity.getY() + enemyEntity.getHeight() / HALF_DIVISOR;
        return dynamicPlayer.getVelocityY() >= INITIAL_COUNT
            && playerBottom <= enemyMiddle + STOMP_TOLERANCE;
    }

    private void stompEnemy(
        final AbstractEntity enemyEntity,
        final AbstractDynamicEntity playerEntity,
        final Player player
    ) {
        if (enemyEntity instanceof Goomba) {
            defeatEnemy(enemyEntity);
        } else if (enemyEntity instanceof Koopa) {
            final Koopa koopa = (Koopa) enemyEntity;
            if (koopa.getState() == Koopa.KoopaState.SHELL) {
                koopa.kick(player.getX() < koopa.getX());
            } else {
                koopa.stomp();
            }
        }
        playerEntity.setVelocityY(PLAYER_BOUNCE_AFTER_STOMP);
    }

    private void stompMovingShell(
        final AbstractEntity enemyEntity,
        final AbstractDynamicEntity playerEntity
    ) {
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
}
