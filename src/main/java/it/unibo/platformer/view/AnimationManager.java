package it.unibo.platformer.view;
 
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
 
import java.util.HashMap;
import java.util.Map;
 
/**
 * General-purpose animation controller for any entity that uses sprite frames.
 * Works with single PNG files — no spritesheet slicing needed.
 *
 * <p>Typical usage:
 *   Image f1 = AnimationManager.loadImage("/sprites/enemies/goomba1.png");
 *   Image f2 = AnimationManager.loadImage("/sprites/enemies/goomba2.png");
 *   anim.register("walk", new Animation(new Image[]{f1, f2}, 0.2, true));
 *   anim.play("walk");
 */
public class AnimationManager {
 
    private final Map<String, Animation> animations = new HashMap<>();
    private String currentKey;
 
    /** 
     * Register an animation under a given key name. 
     * 
     * @param key       the name to register the animation under
     * @param animation the animation to register
     */
    public void register(final String key, final Animation animation) {
        animations.put(key, animation);
    }
 
    /**
     * Switch to an animation by key.
     * If the animation is already playing it is NOT reset (seamless loop).
     * 
     * @param key the animation key to switch to
     */
    public void play(final String key) {
        if (key.equals(currentKey)) {
            return;
        }
        currentKey = key;
        final Animation a = animations.get(key);
        if (a != null) {
            a.reset();
        }
    }
 
    /** 
     * Advance the current animation by deltaTime seconds.
     *
     * @param deltaTime elapsed time in seconds since the last update
     */
    public void update(final double deltaTime) {
        final Animation a = animations.get(currentKey);
        if (a != null) {
            a.update(deltaTime);
        }
    }
 
    /**
     * Draw the current frame at (x, y) scaled to (width, height).
     *
     * @param gc the graphics context to draw on
     * @param x the x coordinate of the top-left corner
     * @param y the y coordinate of the top-left corner
     * @param width the width of the destination rectangle
     * @param height the height of the destination rectangle
     * @param flipX whether to flip the image horizontally
     */
    public void render(final GraphicsContext gc, final double x, final double y, 
                       final double width, final double height, final boolean flipX) {
 
        final Animation a = animations.get(currentKey);
        if (a == null) {
            return;
        }

        final Image frame = a.getCurrentFrame();
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
     * Load a single PNG from the classpath resources folder.
     *
     *  <p>Path example: "/sprites/enemies/goomba1.png"
     *
     *  <p>Returns null (with a console warning) if the resource is not found,
     * so the caller can fall back to placeholder rendering without crashing.
     * 
     *  @param resourcePath the absolute classpath path to the image resource
     * @return the loaded {@link Image}, or {@code null} if the resource was not found
     */
    public static Image loadImage(final String resourcePath) {
        final var stream = AnimationManager.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            System.err.println("[AnimationManager] Resource not found: " + resourcePath);
            return null;
        }
        return new Image(stream);
    }
 
    /** 
     * return the animation key.
     *
     * @return the current animation key, or {@code null} if none is set
     */
    public String getCurrentKey() { 
        return currentKey; 
    }
 
    /**
     * Returns whether the given animation key is registered.
     *
     *  @param key the animation key to look up
     * @return {@code true} if the key is present in the registry 
     */
    public boolean hasAnimation(final String key) {
        return animations.containsKey(key);
    }

    /**
     * Returns true if the current animation has finished playing.
     *
     * @return {@code true} if the current animation is non-null and has finished
     */
    public boolean isCurrentFinished() {
        final Animation a = animations.get(currentKey);
        return a != null && a.isFinished();
    }

    /** 
     * Represents a sequence of image frames played at a fixed frame rate. 
     */
    public static class Animation {
 
        private final Image[] frames;
        private final double frameDuration;
        private final boolean loop;
 
        private double timer;
        private int curFrame;
        private boolean finished;
 
        /**
         * Constructs a new Animation.
         *
         * @param frames        array of {@link Image} frames in display order
         * @param frameDuration time in seconds each frame is displayed
         * @param loop    if {@code true} the clip restarts after the last frame
         */
        public Animation(final Image[] frames, final double frameDuration, final boolean loop) {
            this.frames = frames;
            this.frameDuration = frameDuration;
            this.loop = loop;
        }
 
        /**
         * Advance the animation by the given time delta.
         *
         * @param deltaTime elapsed time in seconds since the last update
         */
        public void update(final double deltaTime) {
            if (finished) {
                return;
            }
            timer += deltaTime;
            if (timer >= frameDuration) {
                timer = 0;
                curFrame++;
                if (curFrame >= frames.length) {
                    if (loop) {
                        curFrame = 0;
                    } else {
                        curFrame = frames.length - 1;
                        finished = true;
                    }
                }
            }
        }
 
        /**
         * Reset the animation to its initial state so it can be replayed.
         */
        public void reset() {
            timer = 0;
            curFrame = 0;
            finished = false;
        }

        /**
         * Returns the frame image that should be displayed at the current moment.
         *
         * @return the current {@link Image} frame
         */
        public Image getCurrentFrame() { 
            return frames[curFrame]; 
        }

        /**
         * Returns whether the animation has reached its last frame in non-loop mode.
         *
         * @return {@code true} if the animation has finished
         */
        public boolean isFinished() { 
            return finished; 
        }
    }
}
