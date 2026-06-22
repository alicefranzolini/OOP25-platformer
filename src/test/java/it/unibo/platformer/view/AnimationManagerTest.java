package it.unibo.platformer.view;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.Test;

class AnimationManagerTest {

    private static final double FRAME_DURATION = 0.1;
    private static final double LONG_UPDATE = 1.0;
    private static final int IMAGE_SIZE = 1;

    @Test
    void animationCopiesTheFrameArray() {
        final Image firstFrame = new WritableImage(IMAGE_SIZE, IMAGE_SIZE);
        final Image replacementFrame = new WritableImage(IMAGE_SIZE, IMAGE_SIZE);
        final Image[] frames = {firstFrame};
        final AnimationManager.Animation animation =
            new AnimationManager.Animation(frames, FRAME_DURATION, false);

        frames[0] = replacementFrame;

        assertSame(firstFrame, animation.getCurrentFrame());
    }

    @Test
    void nonLoopingAnimationStopsOnLastFrame() {
        final Image firstFrame = new WritableImage(IMAGE_SIZE, IMAGE_SIZE);
        final Image secondFrame = new WritableImage(IMAGE_SIZE, IMAGE_SIZE);
        final AnimationManager.Animation animation =
            new AnimationManager.Animation(new Image[] {firstFrame, secondFrame}, FRAME_DURATION, false);

        animation.update(LONG_UPDATE);

        assertSame(secondFrame, animation.getCurrentFrame());
        assertTrue(animation.isFinished());
    }
}
