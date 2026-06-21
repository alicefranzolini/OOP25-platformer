package it.unibo.platformer.model.level;

import static org.junit.jupiter.api.Assertions.assertTrue;

import it.unibo.platformer.model.entities.AbstractEntity;
import it.unibo.platformer.model.entities.powerup.MushroomPowerUp;
import it.unibo.platformer.model.entities.powerup.StarPowerUp;
import it.unibo.platformer.model.entities.world.Block;
import it.unibo.platformer.model.entities.world.Block.BlockType;
import it.unibo.platformer.model.entities.world.Coin;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuestionBlockRewardManagerTest {

    private static final int REWARDS_IN_ROTATION = 4;
    private static final double BLOCK_X = 100.0;
    private static final double BLOCK_Y = 200.0;

    @Test
    void eachRotationContainsCoinsMushroomAndStar() {
        final QuestionBlockRewardManager manager = new QuestionBlockRewardManager();
        final Block block = new Block(BLOCK_X, BLOCK_Y, BlockType.QUESTION);
        final List<AbstractEntity> rewards = new ArrayList<>();

        for (int i = 0; i < REWARDS_IN_ROTATION; i++) {
            rewards.add(manager.createReward(block));
        }

        assertTrue(rewards.stream().anyMatch(Coin.class::isInstance));
        assertTrue(rewards.stream().anyMatch(MushroomPowerUp.class::isInstance));
        assertTrue(rewards.stream().anyMatch(StarPowerUp.class::isInstance));
    }
}
