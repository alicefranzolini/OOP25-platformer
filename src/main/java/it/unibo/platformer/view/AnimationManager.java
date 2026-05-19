package it.unibo.platformer.view;
 
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
 
import java.util.HashMap;
import java.util.Map;
 
/**
 * General-purpose animation controller for any entity that uses sprite frames.
 * Works with single PNG files — no spritesheet slicing needed.
 *
 * Typical usage:
 *   Image f1 = AnimationManager.loadImage("/sprites/enemies/goomba1.png");
 *   Image f2 = AnimationManager.loadImage("/sprites/enemies/goomba2.png");
 *   anim.register("walk", new Animation(new Image[]{f1, f2}, 0.2, true));
 *   anim.play("walk");
 */
public class AnimationManager {
 
    // =========================================================================
    // Inner class: Animation
    // =========================================================================
 
    public static class Animation {
 
        private final Image[]  frames;
        private final double   frameDuration;
        private final boolean  loop;
 
        private double  timer    = 0;
        private int     curFrame = 0;
        private boolean finished = false;
 
       
        public Animation(Image[] frames, double frameDuration, boolean loop) {
            this.frames        = frames;
            this.frameDuration = frameDuration;
            this.loop          = loop;
        }
 
        public void update(double deltaTime) {
            if (finished) return;
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
 
        public void reset() {
            timer    = 0;
            curFrame = 0;
            finished = false;
        }
 
        public Image   getCurrentFrame() { return frames[curFrame]; }
        public boolean isFinished()      { return finished; }
    }
 
    // =========================================================================
    // AnimationManager fields
    // =========================================================================
 
    private final Map<String, Animation> animations = new HashMap<>();
    private String currentKey;
 
    // =========================================================================
    // Registration & playback
    // =========================================================================
 
    /** Register an animation under a given key name. */
    public void register(String key, Animation animation) {
        animations.put(key, animation);
    }
 
    /**
     * Switch to an animation by key.
     * If the animation is already playing it is NOT reset (seamless loop).
     */
    public void play(String key) {
        if (key.equals(currentKey)) return;
        currentKey = key;
        Animation a = animations.get(key);
        if (a != null) a.reset();
    }
 
    /** Advance the current animation by deltaTime seconds. */
    public void update(double deltaTime) {
        Animation a = animations.get(currentKey);
        if (a != null) a.update(deltaTime);
    }
 
    // =========================================================================
    // Rendering
    // =========================================================================
 
    /**
     * Draw the current frame at (x, y) scaled to (width, height).
     *
     *
     */
    public void render(GraphicsContext gc, double x, double y, double width, double height, boolean flipX) {
 
        Animation a = animations.get(currentKey);
        if (a == null) return;
 
        Image frame = a.getCurrentFrame();
        if (frame == null) return;
 
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
 
    // =========================================================================
    // Single-image loader  (main utility for single-PNG workflows)
    // =========================================================================
 
    /**
     * Load a single PNG from the classpath resources folder.
     *
     * Path example: "/sprites/enemies/goomba1.png"
     *
     * Returns null (with a console warning) if the resource is not found,
     * so the caller can fall back to placeholder rendering without crashing.
     */
    public static Image loadImage(String resourcePath) {
        var stream = AnimationManager.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            System.err.println("[AnimationManager] Resource not found: " + resourcePath);
            return null;
        }
        return new Image(stream);
    }
 
 
    // =========================================================================
    // Accessors
    // =========================================================================
 
    public String  getCurrentKey()     { return currentKey; }
 
     /** Returns true if an animation with the given key has been registered. */
    public boolean hasAnimation(String key) {
        return animations.containsKey(key);
    }
    public boolean isCurrentFinished() {
        Animation a = animations.get(currentKey);
        return a != null && a.isFinished();
    }
   
}