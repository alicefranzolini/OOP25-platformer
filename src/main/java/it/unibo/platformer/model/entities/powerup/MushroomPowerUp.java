package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Mushroom power-up that makes the player big.
 */
public final class MushroomPowerUp extends AbstractPowerUp {

    private static final double SIZE = 32.0;
    private static final double FRAME_DURATION = 1.0;
    private static final String ANIMATION_NAME = "mushroom";
    private static final String SPRITE_PATH = "/sprites/powerup/red_mushroom.png";
    private static final String MISSING_SPRITE_MESSAGE =
            "[MushroomPowerUp] Sprite red_mushroom non trovato - uso fallback.";

    private final AnimationManager anim = new AnimationManager();

    /**
     * Creates a mushroom power-up.
     *
     * @param x the initial x coordinate
     * @param y the initial y coordinate
     * @param physics the physics engine used to update the power-up
     */
    public MushroomPowerUp(final double x, final double y, final BasicPhysics physics) {
        super(x, y, SIZE, SIZE, physics);
        loadSprite();
        anim.play(ANIMATION_NAME);
    }

    private void loadSprite() {
        final Image img = AnimationManager.loadImage(SPRITE_PATH);
        if (img != null) {
            final Animation animation = new Animation(new Image[] {img}, FRAME_DURATION, false);
            anim.register(ANIMATION_NAME, animation);
        } else {
            System.err.println(MISSING_SPRITE_MESSAGE);
        }
    }

    @Override
    public void applyEffect(final Player player) {
        if (player != null) {
            player.setState(Player.PlayerState.BIG);
        }
    }

    @Override
    public void render(final GraphicsContext gc) {
        if (!isActive()) {
            return;
        }
        anim.render(gc, getX(), getY(), getWidth(), getHeight(), false);
    }
}
