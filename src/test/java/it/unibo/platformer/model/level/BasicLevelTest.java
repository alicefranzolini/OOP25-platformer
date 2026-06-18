package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.enemies.Goomba;
import it.unibo.platformer.model.entities.enemies.Koopa;
import it.unibo.platformer.model.entities.players.PlayerImpl;
import it.unibo.platformer.model.entities.powerup.MushroomPowerUp;
import it.unibo.platformer.model.entities.powerup.StarPowerUp;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.entities.world.Block.BlockType;
import it.unibo.platformer.model.entities.world.Coin;
import it.unibo.platformer.model.entities.world.Flag;
import it.unibo.platformer.model.entities.world.Pole;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import org.junit.jupiter.api.Test;

class BasicLevelTest {

    private static final int ZERO_COUNT = 0;
    private static final int ONE_COUNT = 1;
    private static final int SECOND_BLOCK_INDEX = 1;
    private static final int THIRD_BLOCK_INDEX = 2;
    private static final int FOURTH_BLOCK_INDEX = 3;
    private static final double ZERO_DELTA_TIME = 0.0;
    private static final double SHORT_DELTA_TIME = 0.1;
    private static final double SIDE_COLLISION_TIME = 0.2;
    private static final double FULL_SECOND = 1.0;
    private static final double EPSILON = 0.001;
    private static final double PLAYER_X = 100.0;
    private static final double PLAYER_Y = 300.0;
    private static final double COIN_X = 105.0;
    private static final double FAR_COIN_X = 200.0;
    private static final double SECOND_PLAYER_X = 150.0;
    private static final double QUESTION_PLAYER_X = 108.0;
    private static final double QUESTION_PLAYER_Y = 124.0;
    private static final double QUESTION_BLOCK_X = 100.0;
    private static final double QUESTION_BLOCK_Y = 100.0;
    private static final double QUESTION_BLOCK_SPACING = 80.0;
    private static final double MUSHROOM_PLAYER_Y = 80.0;
    private static final double VISIBLE_MUSHROOM_PLAYER_Y = 90.0;
    private static final double MUSHROOM_Y = 100.0;
    private static final double BIG_PLAYER_HEIGHT = 48.0;
    private static final double SIDE_PLAYER_X = 120.0;
    private static final double SIDE_PLAYER_Y = 306.0;
    private static final double SIDE_BLOCK_X = 140.0;
    private static final double SIDE_BLOCK_Y = 300.0;
    private static final double SIDE_EXPECTED_PLAYER_X = 124.0;
    private static final double WALL_PLAYER_Y = 204.0;
    private static final double WALL_X = 130.0;
    private static final double WALL_Y = 200.0;
    private static final double PLAYER_FALL_SPEED = 100.0;
    private static final double SHELL_PLAYER_Y = 108.0;
    private static final double SHELL_SIDE_PLAYER_Y = 112.0;
    private static final double ENEMY_X = 110.0;
    private static final double GOOMBA_SHELL_TEST_Y = 116.0;
    private static final double PLAYER_AFTER_STOMP_X = 92.0;
    private static final double PLAYER_AFTER_STOMP_Y = 112.0;
    private static final double SEPARATED_PLAYER_X = 40.0;
    private static final double SHELL_STOMP_PLAYER_Y = 88.0;
    private static final double GROUND_Y = 500.0;
    private static final double GOOMBA_GROUND_Y = 468.0;
    private static final double KOOPA_GROUND_Y = 452.0;
    private static final double BOUNDARY_SHELL_X = 2.0;
    private static final double GOAL_PLAYER_Y = 250.0;
    private static final double POLE_X = 110.0;
    private static final double POLE_Y = 100.0;
    private static final double POLE_HEIGHT = 200.0;
    private static final double BLOCK_HIT_X_OFFSET = 8.0;
    private static final double BLOCK_HIT_Y_OFFSET = 8.0;
    private static final double BLOCK_HIT_SPEED = -100.0;

    @Test
    void collectsCoinWhenPlayerBoundingBoxOverlapsIt() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());
        final Coin coin = new Coin(COIN_X, PLAYER_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(coin);

        level.update(ZERO_DELTA_TIME);

        assertFalse(level.getEntities().contains(coin));
        assertFalse(coin.isActive());
        assertEquals(ONE_COUNT, level.getCollectedCoins());
    }

    @Test
    void doesNotCollectCoinWhenPlayerDoesNotOverlapIt() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());
        final Coin coin = new Coin(FAR_COIN_X, PLAYER_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(coin);

        level.update(ZERO_DELTA_TIME);

        assertTrue(level.getEntities().contains(coin));
        assertTrue(coin.isActive());
        assertEquals(ZERO_COUNT, level.getCollectedCoins());
    }

    @Test
    void collectedCoinsCanBeResetAfterScoreUpdate() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());
        final Coin coin = new Coin(COIN_X, PLAYER_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(coin);
        level.update(ZERO_DELTA_TIME);
        level.resetCollectedCoins();

        assertEquals(ZERO_COUNT, level.getCollectedCoins());
    }

    @Test
    void replacingPlayerRemovesPreviousPlayerFromLevel() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl firstPlayer = new PlayerImpl(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());
        final PlayerImpl secondPlayer = new PlayerImpl(SECOND_PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());

        level.setPlayer(firstPlayer);
        level.setPlayer(secondPlayer);

        assertEquals(secondPlayer, level.getPlayer());
        assertFalse(level.getEntities().contains(firstPlayer));
        assertTrue(level.getEntities().contains(secondPlayer));
    }

    @Test
    void groundedPlayerMovesHorizontally() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, PLAYER_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        player.setOnGround(true);
        player.moveRight();

        level.update(FULL_SECOND);

        assertTrue(player.getX() > PLAYER_X);
    }

    @Test
    void questionBlocksCanSpawnCoinsMushroomsAndStars() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(QUESTION_PLAYER_X, QUESTION_PLAYER_Y, new BasicPhysicsImpl());
        final Block firstBlock = new Block(QUESTION_BLOCK_X, QUESTION_BLOCK_Y, BlockType.QUESTION);
        final Block secondBlock = new Block(
            QUESTION_BLOCK_X + QUESTION_BLOCK_SPACING * SECOND_BLOCK_INDEX,
            QUESTION_BLOCK_Y,
            BlockType.QUESTION
        );
        final Block thirdBlock = new Block(
            QUESTION_BLOCK_X + QUESTION_BLOCK_SPACING * THIRD_BLOCK_INDEX,
            QUESTION_BLOCK_Y,
            BlockType.QUESTION
        );
        final Block fourthBlock = new Block(
            QUESTION_BLOCK_X + QUESTION_BLOCK_SPACING * FOURTH_BLOCK_INDEX,
            QUESTION_BLOCK_Y,
            BlockType.QUESTION
        );

        level.setPlayer(player);
        level.addEntity(firstBlock);
        level.addEntity(secondBlock);
        level.addEntity(thirdBlock);
        level.addEntity(fourthBlock);
        hitQuestionBlock(level, player, firstBlock);
        hitQuestionBlock(level, player, secondBlock);
        hitQuestionBlock(level, player, thirdBlock);
        hitQuestionBlock(level, player, fourthBlock);

        assertTrue(level.getEntities().stream().anyMatch(entity -> entity instanceof Coin));
        assertTrue(level.getEntities().stream().anyMatch(entity -> entity instanceof MushroomPowerUp));
        assertTrue(level.getEntities().stream().anyMatch(entity -> entity instanceof StarPowerUp));
    }

    @Test
    void collectingMushroomInLevelMakesPlayerBig() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, MUSHROOM_PLAYER_Y, new BasicPhysicsImpl());
        final MushroomPowerUp mushroom = new MushroomPowerUp(PLAYER_X, MUSHROOM_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(mushroom);
        finishPowerUpEmergence(mushroom);

        level.update(ZERO_DELTA_TIME);

        assertEquals(PlayerImpl.PlayerState.BIG, player.getPlayerState());
        assertEquals(BIG_PLAYER_HEIGHT, player.getHeight());
        assertFalse(mushroom.isActive());
    }

    @Test
    void visibleEmergingMushroomCanBeCollected() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, VISIBLE_MUSHROOM_PLAYER_Y, new BasicPhysicsImpl());
        final MushroomPowerUp mushroom = new MushroomPowerUp(PLAYER_X, MUSHROOM_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(mushroom);

        level.update(ZERO_DELTA_TIME);

        assertEquals(PlayerImpl.PlayerState.BIG, player.getPlayerState());
        assertFalse(mushroom.isActive());
    }

    @Test
    void emergingMushroomIsNotCollectedFromBelow() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, QUESTION_PLAYER_Y, new BasicPhysicsImpl());
        final MushroomPowerUp mushroom = new MushroomPowerUp(PLAYER_X, MUSHROOM_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(mushroom);

        level.update(ZERO_DELTA_TIME);

        assertEquals(PlayerImpl.PlayerState.SMALL, player.getPlayerState());
        assertTrue(mushroom.isActive());
    }

    @Test
    void sideCollisionKeepsPlayerBeforeBlock() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(SIDE_PLAYER_X, SIDE_PLAYER_Y, new BasicPhysicsImpl());
        final Block block = new Block(SIDE_BLOCK_X, SIDE_BLOCK_Y, BlockType.BRICK);

        level.setPlayer(player);
        level.addEntity(block);
        player.setAffectedByGravity(false);
        player.moveRight();

        level.update(SIDE_COLLISION_TIME);

        assertEquals(SIDE_EXPECTED_PLAYER_X, player.getX(), EPSILON);
    }

    @Test
    void wallJumpPushesPlayerUpAndAwayFromWall() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, WALL_PLAYER_Y, new BasicPhysicsImpl());
        final Block wall = new Block(WALL_X, WALL_Y, BlockType.BRICK);

        level.setPlayer(player);
        level.addEntity(wall);
        player.setAffectedByGravity(false);
        player.moveRight();

        level.update(SHORT_DELTA_TIME);
        level.handleJumpPressed(true);

        assertTrue(player.getVelocityX() < ZERO_COUNT);
        assertTrue(player.getVelocityY() < ZERO_COUNT);
    }

    @Test
    void movingKoopaShellDefeatsOtherEnemy() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, SHELL_PLAYER_Y, new BasicPhysicsImpl());
        final Koopa shell = new Koopa(PLAYER_X, MUSHROOM_Y, new BasicPhysicsImpl());
        final Goomba goomba = new Goomba(ENEMY_X, GOOMBA_SHELL_TEST_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(shell);
        player.setVelocityY(PLAYER_FALL_SPEED);
        level.update(ZERO_DELTA_TIME);
        player.setX(PLAYER_AFTER_STOMP_X);
        player.setY(PLAYER_AFTER_STOMP_Y);
        level.update(ZERO_DELTA_TIME);
        level.addEntity(goomba);

        level.update(ZERO_DELTA_TIME);

        assertEquals(Goomba.GoombaState.SQUISHED, goomba.getState());
    }

    @Test
    void movingKoopaShellDamagesPlayerOnSideContact() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, SHELL_SIDE_PLAYER_Y, new BasicPhysicsImpl());
        final Koopa shell = new Koopa(ENEMY_X, MUSHROOM_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(shell);
        makeMovingShell(shell, false);

        level.update(ZERO_DELTA_TIME);

        assertTrue(player.isDying());
    }

    @Test
    void kickedKoopaShellDoesNotDamagePlayerBeforeTheySeparate() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, SHELL_SIDE_PLAYER_Y, new BasicPhysicsImpl());
        final Koopa shell = new Koopa(ENEMY_X, MUSHROOM_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(shell);
        shell.stomp();

        level.update(ZERO_DELTA_TIME);
        level.update(ZERO_DELTA_TIME);

        assertFalse(player.isDying());
        assertTrue(shell.canKillEnemies());

        player.setX(SEPARATED_PLAYER_X);
        level.update(ZERO_DELTA_TIME);
        player.setX(PLAYER_X);
        level.update(ZERO_DELTA_TIME);

        assertTrue(player.isDying());
    }

    @Test
    void playerCanStompMovingKoopaShellFromAbove() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(QUESTION_PLAYER_X, SHELL_STOMP_PLAYER_Y, new BasicPhysicsImpl());
        final Koopa shell = new Koopa(PLAYER_X, MUSHROOM_Y, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(shell);
        makeMovingShell(shell, true);
        player.setVelocityY(PLAYER_FALL_SPEED);

        level.update(ZERO_DELTA_TIME);

        assertFalse(player.isDying());
        assertFalse(shell.isActive());
        assertEquals(ONE_COUNT, level.getDefeatedEnemies());
        assertTrue(player.getVelocityY() < ZERO_COUNT);
    }

    @Test
    void walkingEnemyTurnsAroundBeforePit() {
        final BasicLevel level = new BasicLevel();
        final Block ground = new Block(PLAYER_X, GROUND_Y, BlockType.NORMAL);
        final Goomba goomba = new Goomba(PLAYER_X, GOOMBA_GROUND_Y, new BasicPhysicsImpl());

        level.addEntity(ground);
        level.addEntity(goomba);
        goomba.setOnGround(true);

        level.update(SHORT_DELTA_TIME);

        assertTrue(goomba.getVelocityX() > ZERO_COUNT);
    }

    @Test
    void movingKoopaShellDoesNotTurnAroundBeforePit() {
        final BasicLevel level = new BasicLevel();
        final Block ground = new Block(PLAYER_X, GROUND_Y, BlockType.NORMAL);
        final Koopa shell = new Koopa(PLAYER_X, KOOPA_GROUND_Y, new BasicPhysicsImpl());

        level.addEntity(ground);
        level.addEntity(shell);
        makeMovingShell(shell, false);
        shell.setOnGround(true);

        level.update(SHORT_DELTA_TIME);

        assertTrue(shell.getVelocityX() < ZERO_COUNT);
    }

    @Test
    void movingKoopaShellBouncesAgainstLevelBounds() {
        final BasicLevel level = new BasicLevel();
        final Koopa shell = new Koopa(BOUNDARY_SHELL_X, KOOPA_GROUND_Y, new BasicPhysicsImpl());

        level.addEntity(shell);
        makeMovingShell(shell, false);
        shell.setOnGround(true);

        level.update(SHORT_DELTA_TIME);

        assertEquals(ZERO_COUNT, shell.getX(), EPSILON);
        assertTrue(shell.getVelocityX() > ZERO_COUNT);
    }

    @Test
    void invinciblePlayerDefeatsEnemyOnContact() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, MUSHROOM_Y, new BasicPhysicsImpl());
        final Goomba goomba = new Goomba(COIN_X, MUSHROOM_Y, new BasicPhysicsImpl());

        player.setState(PlayerImpl.PlayerState.INVINCIBLE);
        level.setPlayer(player);
        level.addEntity(goomba);

        level.update(ZERO_DELTA_TIME);

        assertEquals(Goomba.GoombaState.SQUISHED, goomba.getState());
        assertFalse(player.isDying());
    }

    @Test
    void defeatedEnemiesAreCountedAndCanBeReset() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, MUSHROOM_Y, new BasicPhysicsImpl());
        final Goomba goomba = new Goomba(COIN_X, MUSHROOM_Y, new BasicPhysicsImpl());

        player.setState(PlayerImpl.PlayerState.INVINCIBLE);
        level.setPlayer(player);
        level.addEntity(goomba);

        level.update(ZERO_DELTA_TIME);

        assertEquals(ONE_COUNT, level.getDefeatedEnemies());
        level.resetDefeatedEnemies();
        assertEquals(ZERO_COUNT, level.getDefeatedEnemies());
    }

    @Test
    void touchingGoalPoleCompletesLevelAndLowersFlag() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(PLAYER_X, GOAL_PLAYER_Y, new BasicPhysicsImpl());
        final Pole pole = new Pole(POLE_X, POLE_Y, POLE_HEIGHT);
        final Flag flag = new Flag(pole);

        level.setPlayer(player);
        level.addEntity(pole);
        level.addEntity(flag);

        level.update(ZERO_DELTA_TIME);

        assertTrue(level.isCompleted());
        assertTrue(flag.isLowering());
    }

    private void hitQuestionBlock(final BasicLevel level, final PlayerImpl player, final Block block) {
        player.setX(block.getX() + BLOCK_HIT_X_OFFSET);
        player.setY(block.getY() + block.getHeight() - BLOCK_HIT_Y_OFFSET);
        player.setVelocityY(BLOCK_HIT_SPEED);
        level.update(ZERO_DELTA_TIME);
    }

    private void makeMovingShell(final Koopa shell, final boolean toRight) {
        shell.stomp();
        shell.kick(toRight);
    }

    private void finishPowerUpEmergence(final MushroomPowerUp mushroom) {
        while (mushroom.isEmerging()) {
            mushroom.update(SHORT_DELTA_TIME);
        }
    }
}
