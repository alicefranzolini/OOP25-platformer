package it.unibo.platformer.model.entities.players;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.model.physics.api.BasicPhysics;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;

public class PlayerImpl extends DynamicEntity implements Player {

    // PlayerState and SpriteState are now defined in the Player interface

    private static final double SMALL_W = 16, SMALL_H = 24;
    private static final double BIG_H   = 48;

    // Jump strength, horizontal movement speed, and invincibility duration
    private static final double JUMP_VELOCITY       = -420.0; // speed of the jump (negative = upward)
    private static final double MOVE_SPEED          = 180.0;  // horizontal movement speed
    private static final double INVINCIBLE_DURATION = 10.0;   // invincibility duration in seconds

    // Death animation parameters: Mario jumps upward, then falls with custom gravity
    private static final double DEATH_JUMP_VELOCITY = -350.0;
    private static final double DEATH_GRAVITY       =  500.0;

    // Logical state (SMALL/BIG/INVINCIBLE) and visual animation state (IDLE/WALK/JUMP/DEAD)
    private PlayerState playerState;
    private SpriteState spriteState;

    // Direction the player is facing (true = right, false = left)
    private boolean facingRight;

    // Jump input handling: prevents repeated jumps from a single key press
    private boolean jumpRequested;
    private boolean jumpConsumed;

    // Invincibility timers and blinking effect control
    private double invincibleTimer;
    private double blinkTimer;
    private boolean visible;

    // Walking animation timing and frame index
    private double walkAnimTimer;
    private int    walkFrame;
    private static final double WALK_FRAME_DURATION = 0.1;
    private static final int    WALK_FRAMES         = 3;

    // Death animation flags and vertical velocity during the death sequence
    private boolean dying;
    private double  deathVelocityY;
    private boolean deathComplete;

    // Animation manager for sprite-based animations
    private final AnimationManager animManager;

    // Whether the player can collide with other objects (disabled during death)
    private boolean solid = true;

    /**
     * Initializes the player at the given position with SMALL size and default states.
     */
    /* public Player(double x, double y) {
        super(x, y, SMALL_W, SMALL_H);
        this.playerState   = PlayerState.SMALL;
        this.spriteState   = SpriteState.IDLE;
        this.facingRight   = true;
        this.visible       = true;
        this.animManager   = new AnimationManager();
    }*/
    public PlayerImpl(double x, double y, BasicPhysics physics) {
        super(x, y, SMALL_W, SMALL_H, physics);
        this.playerState   = PlayerState.SMALL;
        this.spriteState   = SpriteState.IDLE;
        this.facingRight   = true;
        this.visible       = true;
        this.animManager   = new AnimationManager();
        loadAnimations();
    }

    /**
     * Loads all sprite animations for small, big, and dead states.
     * Sprites are single PNG files (not spritesheets).
     *
     * Expected resource structure:
     *   /sprites/player/mario_small_idle.png
     *   /sprites/player/mario_small_walk_1.png
     *   /sprites/player/mario_small_walk_2.png
     *   /sprites/player/mario_small_walk_3.png
     *   /sprites/player/mario_small_jump.png
     *   /sprites/player/mario_big_idle.png
     *   /sprites/player/mario_big_walk_1.png
     *   /sprites/player/mario_big_walk_2.png
     *   /sprites/player/mario_big_walk_3.png
     *   /sprites/player/mario_big_jump.png
     *   /sprites/player/mario_dead.png
     */
    private void loadAnimations() {
        // --- SMALL animations ---
        Image smallIdle  = AnimationManager.loadImage("/sprites/player/mario_small_idle.png");
        Image smallWalk1 = AnimationManager.loadImage("/sprites/player/mario_small_walk_1.png");
        Image smallWalk2 = AnimationManager.loadImage("/sprites/player/mario_small_walk_2.png");
        Image smallWalk3 = AnimationManager.loadImage("/sprites/player/mario_small_walk_3.png");
        Image smallJump  = AnimationManager.loadImage("/sprites/player/mario_small_jump.png");

        if (smallIdle != null)
            animManager.register("small_idle", new Animation(new Image[]{smallIdle}, 1.0, false));
        if (smallWalk1 != null && smallWalk2 != null && smallWalk3 != null)
            animManager.register("small_walk", new Animation(new Image[]{smallWalk1, smallWalk2, smallWalk3}, WALK_FRAME_DURATION, true));
        if (smallJump != null)
            animManager.register("small_jump", new Animation(new Image[]{smallJump}, 1.0, false));

        // --- BIG animations ---
        Image bigIdle  = AnimationManager.loadImage("/sprites/player/mario_big_idle.png");
        Image bigWalk1 = AnimationManager.loadImage("/sprites/player/mario_big_walk_1.png");
        Image bigWalk2 = AnimationManager.loadImage("/sprites/player/mario_big_walk_2.png");
        Image bigWalk3 = AnimationManager.loadImage("/sprites/player/mario_big_walk_3.png");
        Image bigJump  = AnimationManager.loadImage("/sprites/player/mario_big_jump.png");

        if (bigIdle != null)
            animManager.register("big_idle", new Animation(new Image[]{bigIdle}, 1.0, false));
        if (bigWalk1 != null && bigWalk2 != null && bigWalk3 != null)
            animManager.register("big_walk", new Animation(new Image[]{bigWalk1, bigWalk2, bigWalk3}, WALK_FRAME_DURATION, true));
        if (bigJump != null)
            animManager.register("big_jump", new Animation(new Image[]{bigJump}, 1.0, false));

        // --- DEAD animation ---
        Image dead = AnimationManager.loadImage("/sprites/player/mario_dead.png");
        if (dead != null)
            animManager.register("dead", new Animation(new Image[]{dead}, 1.0, false));

        // Start with small idle
        animManager.play("small_idle");
    }

    /**
     * Returns the animation key to use based on current player and sprite state.
     * Falls back gracefully if an animation is not registered.
     */
    private String getCurrentAnimKey() {
        boolean isBig = (playerState == PlayerState.BIG || playerState == PlayerState.INVINCIBLE);
        String prefix = isBig ? "big" : "small";

        if (spriteState == SpriteState.DEAD) return "dead";

        return switch (spriteState) {
            case WALK -> prefix + "_walk";
            case JUMP -> prefix + "_jump";
            default   -> prefix + "_idle";
        };
    }

    /**
     * Main update loop for the player.
     * Handles:
     *  - death animation (if dying)
     *  - jump logic
     *  - invincibility countdown + blinking
     *  - sprite state transitions
     *  - walking animation
     *  - physics update via DynamicEntity
     */
    @Override
    public void update(double deltaTime) {

        if (dying) {
            updateDeath(deltaTime);
            return;
        }

        // Jump logic
        if (jumpRequested && onGround && !jumpConsumed) {
            setVelocityY(JUMP_VELOCITY);
            onGround     = false;
            jumpConsumed = true;
        }
        if (!jumpRequested) {
            jumpConsumed = false;
        }

        // Invincibility logic + blinking effect
        if (playerState == PlayerState.INVINCIBLE) {
            invincibleTimer -= deltaTime;
            blinkTimer      += deltaTime;

            if (blinkTimer >= 0.1) {
                blinkTimer = 0;
                visible = !visible;
            }

            if (invincibleTimer <= 0) {
                playerState = PlayerState.BIG;
                visible     = true;
            }
        }

        updateSpriteState();
        updateWalkAnimation(deltaTime);

        // Sync AnimationManager to current state
        animManager.play(getCurrentAnimKey());
        animManager.update(deltaTime);

        super.update(deltaTime);
    }

    /**
     * Handles the death animation manually.
     * Gravity is disabled so we can apply a custom upward jump followed by a fall.
     * When the player falls below the screen, the death animation is considered complete.
     
    private void updateDeath(double deltaTime) {
        affectedByGravity = false;

        deathVelocityY += DEATH_GRAVITY * deltaTime;
        y += deathVelocityY * deltaTime;

        if (y > 1000) {
            deathComplete = true;
        }
    }
*/
    private void updateDeath(double deltaTime) {
        affectedByGravity = false;
        deathVelocityY += DEATH_GRAVITY * deltaTime;
        setY(getY() + deathVelocityY * deltaTime);

        animManager.play("dead");
        animManager.update(deltaTime);

        if (getY() > 1000) {
            deathComplete = true;
        }
    }

    /**
     * Determines the current animation state based on movement and grounded status.
     *  - If airborne → JUMP
     *  - If moving horizontally → WALK
     *  - Otherwise → IDLE
     */
    private void updateSpriteState() {
        if (!onGround) {
            spriteState = SpriteState.JUMP;
        } else if (Math.abs(getVelocityX()) > 1.0) {
            spriteState = SpriteState.WALK;
            facingRight = getVelocityX() > 0;
        } else {
            spriteState = SpriteState.IDLE;
        }
    }

    /**
     * Advances the walking animation frames when the player is moving.
     * Resets the animation when the player stops walking.
     */
    private void updateWalkAnimation(double deltaTime) {
        if (spriteState == SpriteState.WALK) {
            walkAnimTimer += deltaTime;

            if (walkAnimTimer >= WALK_FRAME_DURATION) {
                walkAnimTimer = 0;
                walkFrame = (walkFrame + 1) % WALK_FRAMES;
            }

        } else {
            walkFrame = 0;
            walkAnimTimer = 0;
        }
    }

    /**
     * Renders the player on screen.
     * Uses AnimationManager sprites when available.
     * Falls back to colored rectangle if sprites are missing.
     *
     * The fallback color reflects the player's current state:
     *  - INVINCIBLE → yellow
     *  - BIG → orange-red
     *  - SMALL or DEAD → red
     
    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;

        Color bodyColor;

        if (dying) {
            bodyColor = Color.RED;
        } else if (playerState == PlayerState.INVINCIBLE) {
            bodyColor = Color.YELLOW;
        } else if (playerState == PlayerState.BIG) {
            bodyColor = Color.ORANGERED;
        } else {
            bodyColor = Color.RED;
        }

        gc.setFill(bodyColor);
        gc.fillRect(x, y, width, height);

        gc.setFill(Color.DARKRED);
        gc.fillRect(x + 2, y - 6, width - 4, 8);
    }
*/

    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;

        double px = getX();
        double py = getY();
        double pw = getWidth();
        double ph = getHeight();

        // Try to render with sprite
        String animKey = getCurrentAnimKey();
        animManager.play(animKey);

        // animManager.render is void, so use animation registration state
        // to decide whether to draw the sprite or fallback.
        boolean rendered = animManager.hasAnimation(animKey);
        if (rendered) {
            animManager.render(gc, px, py, pw, ph, !facingRight);
        }

        if (!rendered) {
            // Fallback: colored rectangle
            Color bodyColor;
            if (dying) {
                bodyColor = Color.RED;
            } else if (playerState == PlayerState.INVINCIBLE) {
                bodyColor = Color.YELLOW;
            } else if (playerState == PlayerState.BIG) {
                bodyColor = Color.ORANGERED;
            } else {
                bodyColor = Color.RED;
            }

            gc.setFill(bodyColor);
            gc.fillRect(px, py, pw, ph);

            gc.setFill(Color.DARKRED);
            gc.fillRect(px + 2, py - 6, pw - 4, 8);
        }
    }

    /**
     * STATE MANAGEMENT
     * Changes the player's logical state.
     * Handles size changes when transitioning between SMALL and BIG.
     * When becoming INVINCIBLE, starts the invincibility timer.
     */
    @Override
    public void setState(PlayerState newState) {
        switch (newState) {

/*          case BIG:
                if (playerState == PlayerState.SMALL) {
                    playerState = PlayerState.BIG;
                    height = BIG_H;
                    y -= (BIG_H - SMALL_H);
                }
                break;*/
            case BIG:
                if (playerState == PlayerState.SMALL) {
                    playerState = PlayerState.BIG;
                    height = BIG_H;
                    setY(getY() - (BIG_H - SMALL_H));
                }
                break;

            case INVINCIBLE:
                playerState     = PlayerState.INVINCIBLE;
                invincibleTimer = INVINCIBLE_DURATION;
                break;

            case SMALL:
                playerState = PlayerState.SMALL;
                height      = SMALL_H;
                break;
        }
    }

    /**
     * Applies damage to the player.
     *
     * Behavior:
     *  - INVINCIBLE → no effect
     *  - BIG → shrink to SMALL and grant brief invincibility
     *  - SMALL → player must die (returns true)
     *
     * @return true if the player should die
     */
    @Override
    public boolean takeDamage() {
        if (playerState == PlayerState.INVINCIBLE) {
            return false;
        }

        if (playerState == PlayerState.BIG) {
            setState(PlayerState.SMALL);
            return false;
        }

        return true; // SMALL → muore
    }

    /**
     * Starts the death animation.
     * The player jumps upward, stops colliding, and eventually falls off-screen.
     * GameManager checks isDeathComplete() to remove the player.
     */
    @Override
    public void die() {
        if (dying) return;

        dying          = true;
        spriteState    = SpriteState.DEAD;
        deathVelocityY = DEATH_JUMP_VELOCITY;

        setVelocityX(0);
        solid = false;
    }


    // INPUT HANDLING -> Horizontal movement commands (ignored while dying)
    @Override
    public void moveLeft()  {
        if (!dying) {
            setVelocityX(-MOVE_SPEED);
            facingRight = false;
        }
    }
    @Override
    public void moveRight() {
        if (!dying) {
            setVelocityX( MOVE_SPEED);
            facingRight = true;
        }
    }
    @Override
    public void stopX()     {
        if (!dying) {
            setVelocityX(0);
        }
    }

    // Jump input flag (actual jump happens in update())
    @Override
    public void setJumpRequested(boolean v) {
        this.jumpRequested = v;
    }


 /*
     * These methods set the horizontal
     * velocity to move left, move right, or stop.
     * They interact with the physics engine through setVelocityX.
     */

    @Override
    public PlayerState getPlayerState()  {
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
    public AnimationManager getAnimManager() {
        return animManager;
    }
    @Override
    public boolean isSolid() {
        return solid;
    }
}