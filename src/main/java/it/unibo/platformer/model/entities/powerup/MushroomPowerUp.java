package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 *  Class purpose: a mushroom power-up that makes the player BIG. 
 *  that bounces and grants temporary invincibility.
 */
public class MushroomPowerUp extends PowerUpImpl {

    // Class purpose: a mushroom that makes the player BIG.

    private final AnimationManager anim = new AnimationManager();

    public MushroomPowerUp(double x, double y, BasicPhysics physics) {
        super(x, y, 32, 32, physics);
        loadSprite();
        anim.play("mushroom");
    }

    /**
     * Loads the mushroom sprite.
     * Expected resource:
     *   /sprites/powerup/red_mushroom.png
     */
    private void loadSprite() {
        Image img = AnimationManager.loadImage("/sprites/powerup/red_mushroom.png");
        if (img != null) {
            anim.register("mushroom", new Animation(new Image[]{img}, 1.0, false));
        } else {
            System.err.println("[MushroomPowerUp] Sprite red_mushroom non trovato – uso fallback.");
        }
    }

/*
    @Override
    public void applyEffect(Object playerObj) {
    // If the collector is a Player, set its state to BIG
    // Player must update hitbox, sprite, and collision behavior when BIG
        if (!(playerObj instanceof Player)) return;
        Player player = (Player) playerObj;
        player.setState(Player.PlayerState.BIG);
    }
*/
    @Override
    public void applyEffect(Player player) {
        if (player == null) return;
        player.setState(Player.PlayerState.BIG);
    }

/*
    @Override
    public void render(GraphicsContext gc) {
    // Draw a simple red mushroom: cap (oval), stem (rect), white spots (small ovals)
    // Use sprites if available for consistent visuals
        if (!active) return;
        gc.setFill(Color.RED);  //cap
        gc.fillOval(x, y, width, height * 0.6);
        gc.setFill(Color.WHITE);    //stem
        gc.fillRect(x + 8, y + (int)(height * 0.5), width - 16, (int)(height * 0.5));
        gc.setFill(Color.WHITE);    //white spots
        gc.fillOval(x + 6,  y + 4, 6, 6);
        gc.fillOval(x + 20, y + 4, 6, 6);
    }
}
*/
    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;
        double px = getX();
        double py = getY();
        double pw = getWidth();
        double ph = getHeight();

        // Try sprite first
        anim.render(gc, px, py, pw, ph, false);

        // If the animation manager did not render anything, no fallback is available here.
        // The animation is expected to handle missing sprite cases internally.
    }
}

// When player becomes BIG, adjust player's collision box and position
// to avoid getting stuck in tiles; consider a small upward shift if needed.