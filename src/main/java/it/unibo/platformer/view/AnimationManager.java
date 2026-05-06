package it.unibo.platformer.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;


 //general‑purpose animation controller for any entity that uses sprite frames.
 
public class AnimationManager {

    
    public static class Animation {
        private final Image[]  frames;
        private final double   frameDuration; 
        private final boolean  loop;

        private double timer    = 0;
        private int    curFrame = 0;
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
                    if (loop) curFrame = 0;
                    else { curFrame = frames.length - 1; finished = true; }
                }
            }
        }

        public void reset() { timer = 0; curFrame = 0; finished = false; }
        public Image getCurrentFrame() { return frames[curFrame]; }
        public boolean isFinished()    { return finished; }
    }

    

    private final Map<String, Animation> animations = new HashMap<>();
    private String currentKey;

    //register animations with a key name
    public void register(String key, Animation animation) {
        animations.put(key, animation);
    }

    //Switching animations:
    public void play(String key) {
        if (key.equals(currentKey)) return;
        currentKey = key;
        Animation a = animations.get(key);
        if (a != null) a.reset();
    }

    //update current frame
    public void update(double deltaTime) {
        Animation a = animations.get(currentKey);
        if (a != null) a.update(deltaTime);
    }

   // allows your player or enemies to face left/right without needing separate sprites.
    public void render(GraphicsContext gc, double x, double y,
                       double width, double height, boolean flipX) {
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

    //da sistemare
    public static Image[] sliceRow(Image sheet, int frameW, int frameH, int row, int numFrames) {
        
        return new Image[numFrames];
    }

    public String getCurrentKey() { return currentKey; }
    public boolean isCurrentFinished() {
        Animation a = animations.get(currentKey);
        return a != null && a.isFinished();
    }
}