package it.unibo.platformer.model.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.powerup.MushroomPowerUp;
import it.unibo.platformer.model.entities.powerup.StarPowerUp;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.entities.world.Coin;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;

/**
 * Chooses and creates the rewards released by question blocks.
 */
final class QuestionBlockRewardManager {

    private static final int FIRST_REWARD_INDEX = 0;
    private static final double COIN_X_OFFSET = 8.0;
    private static final double COIN_Y_OFFSET = 24.0;

    private final List<QuestionReward> rewards;
    private final Random random;

    QuestionBlockRewardManager() {
        this.rewards = new ArrayList<>();
        this.random = new Random();
    }

    AbstractEntity createReward(final Block block) {
        final QuestionReward reward = takeNextReward();
        return switch (reward) {
            case COIN -> new Coin(
                    block.getX() + COIN_X_OFFSET,
                    block.getY() - COIN_Y_OFFSET,
                    new BasicPhysicsImpl()
                );
            case STAR -> new StarPowerUp(block.getX(), block.getY(), new BasicPhysicsImpl());
            case MUSHROOM -> new MushroomPowerUp(block.getX(), block.getY(), new BasicPhysicsImpl());
        };
    }

    private QuestionReward takeNextReward() {
        if (this.rewards.isEmpty()) {
            refillRewards();
        }
        return this.rewards.remove(FIRST_REWARD_INDEX);
    }

    private void refillRewards() {
        this.rewards.add(QuestionReward.COIN);
        this.rewards.add(QuestionReward.COIN);
        this.rewards.add(QuestionReward.MUSHROOM);
        this.rewards.add(QuestionReward.STAR);
        Collections.shuffle(this.rewards, this.random);
    }

    private enum QuestionReward {
        COIN,
        MUSHROOM,
        STAR
    }
}
