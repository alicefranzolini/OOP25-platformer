package it.unibo.platformer.model.entities.players;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Default implementation of the controllable player entity.
 */
public final class PlayerImpl extends DynamicEntity implements Player {

    private static final double SMALL_W = 16.0;
    private static final double SMALL_H = 24.0;
    private static final double BIG_H = 48.0;
    private static final double JUMP_VELOCITY = -420.0;
    private static final double MOVE_SPEED = 180.0;
    private static final double INVINCIBLE_DURATION = 10.0;
    private static final double BLINK_INTERVAL = 0.1;
    private static final double MOVING_THRESHOLD = 1.0;
    private static final double DEATH_JUMP_VELOCITY = -350.0;
    private static final double DEATH_GRAVITY = 500.0;
    private static final double DEATH_COMPLETE_Y = 1000.0;
    private static final double HAT_OFFSET_X = 2.0;
    private static final double HAT_OFFSET_Y = 6.0;
    private static final double HAT_WIDTH_DELTA = 4.0;
    private static final double HAT_HEIGHT = 8.0;
    private static final double WALK_FRAME_DURATION = 0.1;
    private static final int WALK_FRAMES = 3;

    private static final String SPRITE_ROOT = "/sprites/player/";
    private static final String SMALL_IDLE_KEY = "small_idle";
    private static final String SMALL_WALK_KEY = "small_walk";
    private static final String SMALL_JUMP_KEY = "small_jump";
    private static final String BIG_IDLE_KEY = "big_idle";
    private static final String BIG_WALK_KEY = "big_walk";
    private static final String BIG_JUMP_KEY = "big_jump";
    private static final String DEAD_KEY = "dead";

    private PlayerState playerState;
    private SpriteState spriteState;
    private boolean facingRight;
    private boolean jumpRequested;
    private boolean jumpConsumed;
    private double invincibleTimer;
    private double blinkTimer;
    private boolean visible;
    private double walkAnimTimer;
    private int walkFrame;
    private boolean dying;
    private double deathVelocityY;
    private boolean deathComplete;
    private final AnimationManager animManager;
    private boolean solid = true;

    /**
     * Initializes the player at the given position with small size and default states.
     *
     * @param x the starting x coordinate
     * @param y the starting y coordinate
     * @param physics the physics engine used by the player
     */
    public PlayerImpl(final double x, final double y, final BasicPhysics physics) {
        super(x, y, SMALL_W, SMALL_H, physics);
        this.playerState = PlayerState.SMALL;
        this.spriteState = SpriteState.IDLE;
        this.facingRight = true;
        this.visible = true;
        this.animManager = new AnimationManager();
        loadAnimations();
    }

    private void loadAnimations() {
        final Image smallIdle = loadSprite("mario_small_idle.png");
        final Image smallWalk1 = loadSprite("mario_small_walk_1.png");
        final Image smallWalk2 = loadSprite("mario_small_walk_2.png");
        final Image smallWalk3 = loadSprite("mario_small_walk_3.png");
        final Image smallJump = loadSprite("mario_small_jump.png");

        registerSingleFrameAnimation(SMALL_IDLE_KEY, smallIdle);
        registerWalkAnimation(SMALL_WALK_KEY, smallWalk1, smallWalk2, smallWalk3);
        registerSingleFrameAnimation(SMALL_JUMP_KEY, smallJump);

        final Image bigIdle = loadSprite("mario_big_idle.png");
        final Image bigWalk1 = loadSprite("mario_big_walk_1.png");
        final Image bigWalk2 = loadSprite("mario_big_walk_2.png");
        final Image bigWalk3 = loadSprite("mario_big_walk_3.png");
        final Image bigJump = loadSprite("mario_big_jump.png");

        registerSingleFrameAnimation(BIG_IDLE_KEY, bigIdle);
        registerWalkAnimation(BIG_WALK_KEY, bigWalk1, bigWalk2, bigWalk3);
        registerSingleFrameAnimation(BIG_JUMP_KEY, bigJump);
        registerSingleFrameAnimation(DEAD_KEY, loadSprite("mario_dead.png"));

        animManager.play(SMALL_IDLE_KEY);
    }

    private Image loadSprite(final String fileName) {
        return AnimationManager.loadImage(SPRITE_ROOT + fileName);
    }

    private void registerSingleFrameAnimation(final String key, final Image image) {
        if (image != null) {
            animManager.register(key, new Animation(new Image[] {image}, 1.0, false));
        }
    }

    private void registerWalkAnimation(
            final String key,
            final Image firstFrame,
            final Image secondFrame,
            final Image thirdFrame
    ) {
        if (firstFrame != null && secondFrame != null && thirdFrame != null) {
            animManager.register(
                    key,
                    new Animation(new Image[] {firstFrame, secondFrame, thirdFrame}, WALK_FRAME_DURATION, true)
            );
        }
    }

    private String getCurrentAnimKey() {
        if (spriteState == SpriteState.DEAD) {
            return DEAD_KEY;
        }
        final boolean bigSprite = playerState == PlayerState.BIG || playerState == PlayerState.INVINCIBLE;
        return switch (spriteState) {
            case WALK -> bigSprite ? BIG_WALK_KEY : SMALL_WALK_KEY;
            case JUMP -> bigSprite ? BIG_JUMP_KEY : SMALL_JUMP_KEY;
            default -> bigSprite ? BIG_IDLE_KEY : SMALL_IDLE_KEY;
        };
    }

    @Override
    public void update(final double deltaTime) {
        if (dying) {
            updateDeath(deltaTime);
            return;
        }

        updateJumpState();
        updateInvincibility(deltaTime);
        updateSpriteState();
        updateWalkAnimation(deltaTime);

        animManager.play(getCurrentAnimKey());
        animManager.update(deltaTime);

        super.update(deltaTime);
    }

    private void updateJumpState() {
        if (jumpRequested && isOnGround() && !jumpConsumed) {
            setVelocityY(JUMP_VELOCITY);
            setOnGround(false);
            jumpConsumed = true;
        }
        if (!jumpRequested) {
            jumpConsumed = false;
        }
    }

    private void updateInvincibility(final double deltaTime) {
        if (playerState == PlayerState.INVINCIBLE) {
            invincibleTimer -= deltaTime;
            blinkTimer += deltaTime;

            if (blinkTimer >= BLINK_INTERVAL) {
                blinkTimer = 0.0;
                visible = !visible;
            }

            if (invincibleTimer <= 0.0) {
                playerState = PlayerState.BIG;
                visible = true;
            }
        }
    }

    private void updateDeath(final double deltaTime) {
        setAffectedByGravity(false);
        deathVelocityY += DEATH_GRAVITY * deltaTime;
        setY(getY() + deathVelocityY * deltaTime);

        animManager.play(DEAD_KEY);
        animManager.update(deltaTime);

        if (getY() > DEATH_COMPLETE_Y) {
            deathComplete = true;
        }
    }

    private void updateSpriteState() {
        if (!isOnGround()) {
            spriteState = SpriteState.JUMP;
        } else if (Math.abs(getVelocityX()) > MOVING_THRESHOLD) {
            spriteState = SpriteState.WALK;
            facingRight = getVelocityX() > 0.0;
        } else {
            spriteState = SpriteState.IDLE;
        }
    }

    private void updateWalkAnimation(final double deltaTime) {
        if (spriteState == SpriteState.WALK) {
            walkAnimTimer += deltaTime;

            if (walkAnimTimer >= WALK_FRAME_DURATION) {
                walkAnimTimer = 0.0;
                walkFrame = (walkFrame + 1) % WALK_FRAMES;
            }
        } else {
            walkFrame = 0;
            walkAnimTimer = 0.0;
        }
    }

    @Override
    public void render(final GraphicsContext gc) {
        if (!visible) {
            return;
        }

        final double px = getX();
        final double py = getY();
        final double pw = getWidth();
        final double ph = getHeight();
        final String animKey = getCurrentAnimKey();
        final boolean rendered = animManager.hasAnimation(animKey);

        animManager.play(animKey);
        if (rendered) {
            animManager.render(gc, px, py, pw, ph, !facingRight);
        } else {
            renderFallback(gc, px, py, pw, ph);
        }
    }

    private void renderFallback(
            final GraphicsContext gc,
            final double px,
            final double py,
            final double pw,
            final double ph
    ) {
        gc.setFill(getFallbackColor());
        gc.fillRect(px, py, pw, ph);

        gc.setFill(Color.DARKRED);
        gc.fillRect(px + HAT_OFFSET_X, py - HAT_OFFSET_Y, pw - HAT_WIDTH_DELTA, HAT_HEIGHT);
    }

    private Color getFallbackColor() {
        if (dying) {
            return Color.RED;
        } else if (playerState == PlayerState.INVINCIBLE) {
            return Color.YELLOW;
        } else if (playerState == PlayerState.BIG) {
            return Color.ORANGERED;
        }
        return Color.RED;
    }

    @Override
    public void setState(final PlayerState newState) {
        switch (newState) {
            case BIG:
                if (playerState == PlayerState.SMALL) {
                    playerState = PlayerState.BIG;
                    visible = true;
                    setHeight(BIG_H);
                    setY(getY() - (BIG_H - SMALL_H));
                }
                break;
            case INVINCIBLE:
                playerState = PlayerState.INVINCIBLE;
                invincibleTimer = INVINCIBLE_DURATION;
                blinkTimer = 0.0;
                visible = true;
                break;
            case SMALL:
                playerState = PlayerState.SMALL;
                visible = true;
                setHeight(SMALL_H);
                break;
        }
    }

    @Override
    public boolean takeDamage() {
        if (playerState == PlayerState.INVINCIBLE) {
            return false;
        }

        if (playerState == PlayerState.BIG) {
            setState(PlayerState.SMALL);
            return false;
        }

        return true;
    }

    @Override
    public void die() {
        if (dying) {
            return;
        }

        dying = true;
        spriteState = SpriteState.DEAD;
        deathVelocityY = DEATH_JUMP_VELOCITY;

        setVelocityX(0.0);
        solid = false;
    }

    @Override
    public void moveLeft() {
        if (!dying) {
            setVelocityX(-MOVE_SPEED);
            facingRight = false;
        }
    }

    @Override
    public void moveRight() {
        if (!dying) {
            setVelocityX(MOVE_SPEED);
            facingRight = true;
        }
    }

    @Override
    public void stopX() {
        if (!dying) {
            setVelocityX(0.0);
        }
    }

    @Override
    public void setJumpRequested(final boolean requested) {
        this.jumpRequested = requested;
    }

    @Override
    public PlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public SpriteState getSpriteState() {
        return spriteState;
    }

    @Override
    public boolean isFacingRight() {
        return facingRight;
    }

    @Override
    public boolean isInvincible() {
        return playerState == PlayerState.INVINCIBLE;
    }

    @Override
    public boolean isDying() {
        return dying;
    }

    @Override
    public boolean isDeathComplete() {
        return deathComplete;
    }

    /**
     * Returns the animation manager used by this player.
     *
     * @return the animation manager
     */
    public AnimationManager getAnimManager() {
        return animManager;
    }

    @Override
    public boolean isSolid() {
        return solid;
    }
}
