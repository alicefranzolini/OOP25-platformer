package it.unibo.platformer.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Controls sprite animations made of one or more image frames.
 */
public final class AnimationManager {

    private final Map<String, Animation> animations;
    private String currentKey;

    /**
     * Creates an empty animation registry.
     */
    public AnimationManager() {
        this.animations = new HashMap<>();
    }

    /**
     * Registers an animation under a key.
     *
     * @param key the animation key
     * @param animation the animation to register
     */
    public void register(final String key, final Animation animation) {
        animations.put(key, animation);
    }

    /**
     * Selects the animation associated with the given key.
     * Selecting the current animation does not restart it.
     *
     * @param key the animation key to select
     */
    public void play(final String key) {
        if (Objects.equals(key, currentKey)) {
            return;
        }
        currentKey = key;
        final Animation animation = animations.get(key);
        if (animation != null) {
            animation.reset();
        }
    }

    /**
     * Advances the selected animation.
     *
     * @param deltaTime elapsed time in seconds
     */
    public void update(final double deltaTime) {
        final Animation animation = animations.get(currentKey);
        if (animation != null) {
            animation.update(deltaTime);
        }
    }

    /**
     * Draws the current animation frame.
     *
     * @param gc the graphics context
     * @param x the horizontal drawing position
     * @param y the vertical drawing position
     * @param width the drawing width
     * @param height the drawing height
     * @param flipX true to flip the frame horizontally
     */
    public void render(
        final GraphicsContext gc,
        final double x,
        final double y,
        final double width,
        final double height,
        final boolean flipX
    ) {
        final Animation animation = animations.get(currentKey);
        if (animation == null) {
            return;
        }

        final Image frame = animation.getCurrentFrame();
        if (frame == null) {
            return;
        }

        if (flipX) {
            gc.save();
            gc.translate(x + width, y);
            gc.scale(-1, 1);
            gc.drawImage(frame, 0, 0, width, height);
            gc.restore();
        } else {
            gc.drawImage(frame, x, y, width, height);
        }
    }

    /**
     * Loads an image from the classpath.
     *
     * @param resourcePath the absolute classpath path
     * @return the loaded image, or {@code null} if the resource cannot be loaded
     */
    public static Image loadImage(final String resourcePath) {
        try (InputStream stream = AnimationManager.class.getResourceAsStream(resourcePath)) {
            return stream == null ? null : new Image(stream);
        } catch (final IOException exception) {
            return null;
        }
    }

    /**
     * Returns the selected animation key.
     *
     * @return the current key, or {@code null} if no animation is selected
     */
    public String getCurrentKey() {
        return currentKey;
    }

    /**
     * Checks whether an animation is registered.
     *
     * @param key the animation key
     * @return true if the key is registered
     */
    public boolean hasAnimation(final String key) {
        return animations.containsKey(key);
    }

    /**
     * Checks whether the selected animation has finished.
     *
     * @return true if the current non-looping animation has finished
     */
    public boolean isCurrentFinished() {
        final Animation animation = animations.get(currentKey);
        return animation != null && animation.isFinished();
    }

    /**
     * Represents a sequence of image frames.
     */
    public static final class Animation {

        private final List<Image> frames;
        private final double frameDuration;
        private final boolean loop;

        private double timer;
        private int currentFrame;
        private boolean finished;

        /**
         * Creates an animation.
         *
         * @param frames the frames in display order
         * @param frameDuration the duration of each frame in seconds
         * @param loop true to restart after the final frame
         * @throws IllegalArgumentException if a frame is missing or the duration is not positive
         */
        public Animation(final Image[] frames, final double frameDuration, final boolean loop) {
            if (frames == null || frames.length == 0) {
                throw new IllegalArgumentException("An animation needs at least one frame");
            }
            for (final Image frame : frames) {
                if (frame == null) {
                    throw new IllegalArgumentException("Animation frames cannot be null");
                }
            }
            if (frameDuration <= 0) {
                throw new IllegalArgumentException("Frame duration must be positive");
            }
            this.frames = List.copyOf(Arrays.asList(frames));
            this.frameDuration = frameDuration;
            this.loop = loop;
        }

        /**
         * Advances the animation.
         *
         * @param deltaTime elapsed time in seconds
         */
        public void update(final double deltaTime) {
            if (finished || deltaTime <= 0) {
                return;
            }

            timer += deltaTime;
            while (timer >= frameDuration && !finished) {
                timer -= frameDuration;
                currentFrame++;
                if (currentFrame >= frames.size()) {
                    if (loop) {
                        currentFrame = 0;
                    } else {
                        currentFrame = frames.size() - 1;
                        finished = true;
                    }
                }
            }
        }

        /**
         * Restarts the animation from its first frame.
         */
        public void reset() {
            timer = 0;
            currentFrame = 0;
            finished = false;
        }

        /**
         * Returns the frame currently displayed.
         *
         * @return the current image
         */
        public Image getCurrentFrame() {
            return frames.get(currentFrame);
        }

        /**
         * Checks whether a non-looping animation has finished.
         *
         * @return true if the final frame has been reached
         */
        public boolean isFinished() {
            return finished;
        }
    }
}
