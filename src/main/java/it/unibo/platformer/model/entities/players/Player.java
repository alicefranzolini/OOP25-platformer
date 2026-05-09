package it.unibo.platformer.model.entities.players;

import it.unibo.platformer.model.entities.DynamicEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import it.unibo.platformer.view.AnimationManager;

/**
 * Player — Week 3.
 *
 * This class represents the main controllable character.
 * It includes:
 *  - full invincibility state logic
 *  - death animation (jump upward, then fall off-screen)
 *  - hooks for advanced sprite animations via AnimationManager
 *
 */
public class Player extends DynamicEntity {


    public enum PlayerState { SMALL, BIG, INVINCIBLE }   // logic states
    public enum SpriteState { IDLE, WALK, JUMP }          // graphic states

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

    // Animation manager for sprite-based animations (used in Week 4)
    private final AnimationManager animManager;

    // Whether the player can collide with other objects (disabled during death)
    private boolean solid = true;

    /**
     * Initializes the player at the given position with SMALL size and default states.
     */
    public Player(double x, double y) {
        super(x, y, SMALL_W, SMALL_H);
        this.playerState   = PlayerState.SMALL;
        this.spriteState   = SpriteState.IDLE;
        this.facingRight   = true;
        this.visible       = true;
        this.animManager   = new AnimationManager();
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
        animManager.update(deltaTime);

        super.update(deltaTime);
    }

    /**
     * Handles the death animation manually.
     * Gravity is disabled so we can apply a custom upward jump followed by a fall.
     * When the player falls below the screen, the death animation is considered complete.
     */
    private void updateDeath(double deltaTime) {
        affectedByGravity = false;

        deathVelocityY += DEATH_GRAVITY * deltaTime;
        y += deathVelocityY * deltaTime;

        if (y > 1000) {
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
     * If advanced sprite animations are available, AnimationManager will handle them.
     * Otherwise, a simple colored rectangle is drawn as a placeholder.
     *
     * The color reflects the player's current state:
     *  - INVINCIBLE → yellow
     *  - BIG → orange-red
     *  - SMALL or DEAD → red
     */
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


    /**
     * STATE MANAGEMENT
     * Changes the player's logical state.
     * Handles size changes when transitioning between SMALL and BIG.
     * When becoming INVINCIBLE, starts the invincibility timer.
     */
    public void setState(PlayerState newState) {
        switch (newState) {

            case BIG:
                if (playerState == PlayerState.SMALL) {
                    playerState = PlayerState.BIG;
                    height = BIG_H;
                    y -= (BIG_H - SMALL_H);
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
    public void die() {
        if (dying) return;

        dying          = true;
       // spriteState    = SpriteState.DEAD;
       spriteState    = SpriteState.JUMP; // or IDLE
        deathVelocityY = DEATH_JUMP_VELOCITY;

        setVelocityX(0);
        solid = false;
    }

   
    // INPUT HANDLING -> Horizontal movement commands (ignored while dying)
    public void moveLeft()  { 
        if (!dying) {
            setVelocityX(-MOVE_SPEED); 
        }
    }
    public void moveRight() { 
        if (!dying) {
            setVelocityX( MOVE_SPEED); 
        }
    }
    public void stopX()     { 
        if (!dying) {
            setVelocityX(0); 
        }
    }

    // Jump input flag (actual jump happens in update())
    public void setJumpRequested(boolean v) { 
        this.jumpRequested = v; 
    }

   
 /*
     * These methods set the horizontal 
     * velocity to move left, move right, or stop.
     * They interact with the physics engine through setVelocityX.
     */
    
    public PlayerState getPlayerState()  { 
        return playerState; 
    }
    public SpriteState getSpriteState() { 
        return spriteState; 
    }
    public boolean isFacingRight() { 
        return facingRight; 
    }
    public boolean isInvincible() { 
        return playerState == PlayerState.INVINCIBLE; 
    }
    public boolean isDying() { 
        return dying; 
    }
    public boolean isDeathComplete()     { 
        return deathComplete; 
    }
    public AnimationManager getAnimManager() { 
        return animManager; 
    }
    public boolean isSolid() { 
        return solid; 
    }
}
