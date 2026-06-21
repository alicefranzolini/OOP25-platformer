package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import java.util.List;
import org.junit.jupiter.api.Test;

class EnemyInteractionManagerTest {

    private static final double PLAYER_X = 100.0;
    private static final double PLAYER_Y = 100.0;
    private static final double LEVEL_WIDTH = 1000.0;
    private static final double GROUND_Y = 500.0;
    private static final double GOOMBA_Y = 468.0;
    private static final int ONE_ENEMY = 1;

    @Test
    void invinciblePlayerDefeatsEnemyOnContact() {
        final EnemyInteractionManager manager = new EnemyInteractionManager();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());
        final Goomba goomba = new Goomba(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());
        player.setState(PlayerImpl.PlayerState.INVINCIBLE);

        final int defeated = manager.update(List.of(player, goomba), player, LEVEL_WIDTH);

        assertEquals(ONE_ENEMY, defeated);
        assertEquals(Goomba.GoombaState.SQUISHED, goomba.getState());
    }

    @Test
    void walkingEnemyTurnsAroundAtPlatformEdge() {
        final EnemyInteractionManager manager = new EnemyInteractionManager();
        final Block ground = new Block(PLAYER_X, GROUND_Y);
        final Goomba goomba = new Goomba(PLAYER_X, GOOMBA_Y, new BasicPhysicsImpl());
        goomba.setOnGround(true);

        manager.update(List.of((AbstractEntity) ground, goomba), null, LEVEL_WIDTH);

        assertTrue(goomba.getVelocityX() > 0);
    }
}
