package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.players.Player;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Star power-up that makes the player temporarily invincible.
 */
public final class StarPowerUp extends AbstractPowerUp {

    private static final double SIZE = 32.0;
    private static final double BOUNCE_VELOCITY = -400.0;
    private static final double FRAME_DURATION = 1.0;
    private static final double INNER_RADIUS_RATIO = 0.45;
    private static final double HALF = 2.0;
    private static final int STAR_POINTS = 10;
    private static final int OUTER_POINT_STEP = 2;
    private static final String ANIMATION_NAME = "star";
    private static final String SPRITE_PATH = "/sprites/powerup/star.png";

    private final AnimationManager anim = new AnimationManager();
    private boolean spriteLoaded;

    /**
     * Creates a star power-up.
     *
     * @param x the initial x coordinate
     * @param y the initial y coordinate
     * @param physics the physics engine used to update the power-up
     */
    public StarPowerUp(final double x, final double y, final BasicPhysics physics) {
        super(x, y, SIZE, SIZE, physics);
        loadSprite();
        if (spriteLoaded) {
            anim.play(ANIMATION_NAME);
        }
    }

    private void loadSprite() {
        final Image img = AnimationManager.loadImage(SPRITE_PATH);
        if (img != null) {
            final Animation animation = new Animation(new Image[] {img}, FRAME_DURATION, false);
            anim.register(ANIMATION_NAME, animation);
            spriteLoaded = true;
        }
    }

    @Override
    public void update(final double deltaTime) {
        super.update(deltaTime);
        if (isOnGround()) {
            setVelocityY(BOUNCE_VELOCITY);
            setOnGround(false);
        }
        anim.update(deltaTime);
    }

    @Override
    public void applyEffect(final Player player) {
        if (player != null) {
            player.setState(Player.PlayerState.INVINCIBLE);
        }
    }

    @Override
    public void render(final GraphicsContext gc) {
        if (!isActive()) {
            return;
        }
        if (spriteLoaded) {
            anim.render(gc, getX(), getY(), getWidth(), getHeight(), false);
            return;
        }
        renderFallback(gc);
    }

    private void renderFallback(final GraphicsContext gc) {
        final double centerX = getX() + getWidth() / HALF;
        final double centerY = getY() + getHeight() / HALF;
        final double outerRadius = getWidth() / HALF;
        final double[] xs = new double[STAR_POINTS];
        final double[] ys = new double[STAR_POINTS];
        for (int i = 0; i < STAR_POINTS; i++) {
            final double angle = Math.PI / HALF + i * Math.PI / (STAR_POINTS / HALF);
            final double radius = i % OUTER_POINT_STEP == 0
                    ? outerRadius
                    : outerRadius * INNER_RADIUS_RATIO;
            xs[i] = centerX + radius * Math.cos(angle);
            ys[i] = centerY - radius * Math.sin(angle);
        }
        gc.setFill(Color.YELLOW);
        gc.fillPolygon(xs, ys, STAR_POINTS);
        gc.setStroke(Color.ORANGE);
        gc.strokePolygon(xs, ys, STAR_POINTS);
    }
}
