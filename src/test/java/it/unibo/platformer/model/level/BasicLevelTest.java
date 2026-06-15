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

    @Test
    void collectsCoinWhenPlayerBoundingBoxOverlapsIt() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 300, new BasicPhysicsImpl());
        final Coin coin = new Coin(105, 300, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(coin);

        level.update(0);

        assertFalse(level.getEntities().contains(coin));
        assertFalse(coin.isActive());
        assertEquals(1, level.getCollectedCoins());
    }

    @Test
    void doesNotCollectCoinWhenPlayerDoesNotOverlapIt() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 300, new BasicPhysicsImpl());
        final Coin coin = new Coin(200, 300, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(coin);

        level.update(0);

        assertTrue(level.getEntities().contains(coin));
        assertTrue(coin.isActive());
        assertEquals(0, level.getCollectedCoins());
    }

    @Test
    void collectedCoinsCanBeResetAfterScoreUpdate() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 300, new BasicPhysicsImpl());
        final Coin coin = new Coin(105, 300, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(coin);
        level.update(0);
        level.resetCollectedCoins();

        assertEquals(0, level.getCollectedCoins());
    }

    @Test
    void replacingPlayerRemovesPreviousPlayerFromLevel() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl firstPlayer = new PlayerImpl(100, 300, new BasicPhysicsImpl());
        final PlayerImpl secondPlayer = new PlayerImpl(150, 300, new BasicPhysicsImpl());

        level.setPlayer(firstPlayer);
        level.setPlayer(secondPlayer);

        assertEquals(secondPlayer, level.getPlayer());
        assertFalse(level.getEntities().contains(firstPlayer));
        assertTrue(level.getEntities().contains(secondPlayer));
    }

    @Test
    void groundedPlayerMovesHorizontally() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 300, new BasicPhysicsImpl());

        level.setPlayer(player);
        player.setOnGround(true);
        player.moveRight();

        level.update(1.0);

        assertTrue(player.getX() > 100);
    }

    @Test
    void questionBlocksCanSpawnCoinsMushroomsAndStars() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(108, 124, new BasicPhysicsImpl());
        final Block firstBlock = new Block(100, 100, BlockType.QUESTION);
        final Block secondBlock = new Block(180, 100, BlockType.QUESTION);
        final Block thirdBlock = new Block(260, 100, BlockType.QUESTION);
        final Block fourthBlock = new Block(340, 100, BlockType.QUESTION);

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
    void sideCollisionKeepsPlayerBeforeBlock() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(120, 306, new BasicPhysicsImpl());
        final Block block = new Block(140, 300, BlockType.BRICK);

        level.setPlayer(player);
        level.addEntity(block);
        player.setAffectedByGravity(false);
        player.moveRight();

        level.update(0.2);

        assertEquals(124, player.getX(), 0.001);
    }

    @Test
    void wallJumpPushesPlayerUpAndAwayFromWall() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 204, new BasicPhysicsImpl());
        final Block wall = new Block(130, 200, BlockType.BRICK);

        level.setPlayer(player);
        level.addEntity(wall);
        player.setAffectedByGravity(false);
        player.moveRight();

        level.update(0.1);
        level.handleJumpPressed(true);

        assertTrue(player.getVelocityX() < 0);
        assertTrue(player.getVelocityY() < 0);
    }

    @Test
    void movingKoopaShellDefeatsOtherEnemy() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 108, new BasicPhysicsImpl());
        final Koopa shell = new Koopa(100, 100, new BasicPhysicsImpl());
        final Goomba goomba = new Goomba(110, 116, new BasicPhysicsImpl());

        level.setPlayer(player);
        level.addEntity(shell);
        player.setVelocityY(100);
        level.update(0);
        player.setX(92);
        player.setY(112);
        level.update(0);
        level.addEntity(goomba);

        level.update(0);

        assertEquals(Goomba.GoombaState.SQUISHED, goomba.getState());
    }

    @Test
    void movingKoopaShellDoesNotDamagePlayer() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 108, new BasicPhysicsImpl());
        final Koopa shell = new Koopa(100, 100, new BasicPhysicsImpl());

        player.setState(PlayerImpl.PlayerState.BIG);
        level.setPlayer(player);
        level.addEntity(shell);
        player.setVelocityY(100);
        level.update(0);
        player.setX(92);
        player.setY(112);
        level.update(0);

        level.update(0);

        assertEquals(PlayerImpl.PlayerState.BIG, player.getPlayerState());
        assertFalse(player.isDying());
    }

    @Test
    void invinciblePlayerDefeatsEnemyOnContact() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 100, new BasicPhysicsImpl());
        final Goomba goomba = new Goomba(105, 100, new BasicPhysicsImpl());

        player.setState(PlayerImpl.PlayerState.INVINCIBLE);
        level.setPlayer(player);
        level.addEntity(goomba);

        level.update(0);

        assertEquals(Goomba.GoombaState.SQUISHED, goomba.getState());
        assertFalse(player.isDying());
    }

    @Test
    void defeatedEnemiesAreCountedAndCanBeReset() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 100, new BasicPhysicsImpl());
        final Goomba goomba = new Goomba(105, 100, new BasicPhysicsImpl());

        player.setState(PlayerImpl.PlayerState.INVINCIBLE);
        level.setPlayer(player);
        level.addEntity(goomba);

        level.update(0);

        assertEquals(1, level.getDefeatedEnemies());
        level.resetDefeatedEnemies();
        assertEquals(0, level.getDefeatedEnemies());
    }

    @Test
    void touchingGoalPoleCompletesLevelAndLowersFlag() {
        final BasicLevel level = new BasicLevel();
        final PlayerImpl player = new PlayerImpl(100, 250, new BasicPhysicsImpl());
        final Pole pole = new Pole(110, 100, 200);
        final Flag flag = new Flag(pole);

        level.setPlayer(player);
        level.addEntity(pole);
        level.addEntity(flag);

        level.update(0);

        assertTrue(level.isCompleted());
        assertTrue(flag.isLowering());
    }

    private void hitQuestionBlock(final BasicLevel level, final PlayerImpl player, final Block block) {
        player.setX(block.getX() + 8);
        player.setY(block.getY() + block.getHeight() - 8);
        player.setVelocityY(-100);
        level.update(0);
    }
}
