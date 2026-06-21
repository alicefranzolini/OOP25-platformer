package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.entities.world.Pole;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class LevelCollisionManagerTest {

    private static final double PLAYER_X = 100.0;
    private static final double PLAYER_Y = 480.0;
    private static final double PREVIOUS_PLAYER_Y = 470.0;
    private static final double GROUND_Y = 500.0;
    private static final double EXPECTED_PLAYER_Y = 476.0;
    private static final double POLE_HEIGHT = 200.0;
    private static final double EPSILON = 0.001;

    @Test
    void landingPlacesPlayerOnTopOfBlock() {
        final LevelCollisionManager manager = new LevelCollisionManager();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());
        final Block ground = new Block(PLAYER_X, GROUND_Y);
        final Map<AbstractEntity, double[]> previousPositions = new HashMap<>();
        previousPositions.put(player, new double[] {PLAYER_X, PREVIOUS_PLAYER_Y});

        manager.resolveWorldCollisions(List.of(player, ground), player, previousPositions);

        assertEquals(EXPECTED_PLAYER_Y, player.getY(), EPSILON);
        assertTrue(player.isOnGround());
    }

    @Test
    void touchingPoleReportsGoalReached() {
        final LevelCollisionManager manager = new LevelCollisionManager();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());
        final Pole pole = new Pole(PLAYER_X, PLAYER_Y, POLE_HEIGHT);

        manager.resolveWorldCollisions(List.of(player, pole), player, new HashMap<>());

        assertTrue(manager.isGoalReached());
    }
}
