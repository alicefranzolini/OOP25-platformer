package it.unibo.platformer.model.entities.players;

import it.unibo.platformer.model.entities.DynamicEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Player extends DynamicEntity {

    public enum PlayerState { SMALL, BIG, INVINCIBLE } //logic states
    public enum SpriteState { IDLE, WALK, JUMP } //graphic states 

    //costance if configuration
    private static final double SMALL_W = 16, SMALL_H = 24;     //small player dimension
    private static final double BIG_W   = 16, BIG_H   = 48;     // big player dimension
    private static final double JUMP_VELOCITY       = -420.0;   // speed fo the jump, negative because 
    private static final double MOVE_SPEED          = 180.0;    //speed orizzontale
    private static final double INVINCIBLE_DURATION = 10.0;     //invincible duration 

    // chsnge of the player
    private PlayerState playerState;
    private SpriteState spriteState;
    private boolean facingRight;    //true if face of the player on right

    private boolean jumpRequested;  //if the gamer ask the jump with a button
    private boolean jumpConsumed;   // only one jump whit one pression

    private double invincibleTimer; //timer of invinciblity
    private double blinkTimer;  // timer of the graphic blink
    private boolean visible;    //false if player is bit drawn

    private double walkAnimTimer;   //counter of time for change frame
    private int walkFrame;  //index of the frame
    private static final double WALK_FRAME_DURATION = 0.1;
    private static final int WALK_FRAMES = 3;

    //contructor di Dynamic Entity ,  initializes the player at a given position, 
    // sets its size to the “small” version,
    public Player(double x, double y) {
        super(x, y, SMALL_W, SMALL_H);
        this.playerState   = PlayerState.SMALL;
        this.spriteState   = SpriteState.IDLE;
        this.facingRight   = true;
        this.jumpRequested = false;
        this.jumpConsumed  = false;
        this.visible       = true;
    }

    @Override
    public void update(double deltaTime) { 
        // called every frame and handles all time‑dependent behavior

        // Jump handling
        if (jumpRequested && onGround && !jumpConsumed) {
            setVelocityY(JUMP_VELOCITY);
            onGround = false;
            jumpConsumed = true;
        }
        if (!jumpRequested) jumpConsumed = false;

        //Invincibility and blinking
        if (playerState == PlayerState.INVINCIBLE) {
            invincibleTimer -= deltaTime;
            blinkTimer      += deltaTime;

            if (blinkTimer >= 0.1) {
                blinkTimer = 0;
                visible = !visible;
            }
            if (invincibleTimer <= 0) {
                playerState = PlayerState.BIG;
                visible = true;
            }
        }

        //Animation updates
        updateSpriteState();
        updateWalkAnimation(deltaTime);

        //Physics update
        super.update(deltaTime);
    }

    //This method determines whether the player should appear
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
    /*
     * This method is able to determinate if the player is 
     * walking, a timer accumulates time until it reaches 
     * the duration of a frame, then advances to the next 
     * animation frame. 
     * If the player stops walking, the animation resets.
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

    /*
     * The render method draws the player on screen.
    *  If the player is invisible (due to blinking), 
    *  the method returns immediately.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!visible) return;

    Color bodyColor;

    if (playerState == PlayerState.INVINCIBLE) {
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

    /*
     *  The setState method handles transitions 
     *  between SMALL, BIG, and INVINCIBLE.
     * */
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

    /*
     * The takeDamage method returns whether the player dies.
    *   If invincible, nothing happens.
    *    If big, the player shrinks to small.
    *   If already small, the player dies.
    */
    public boolean takeDamage() {
        if (playerState == PlayerState.INVINCIBLE) 
            return false;
        if (playerState == PlayerState.BIG) {
            setState(PlayerState.SMALL);
            return false;
        }
        return true;
    }

    /*
     * These methods set the horizontal 
     * velocity to move left, move right, or stop.
     * They interact with the physics engine through setVelocityX.
     */
    public void moveLeft() {
        setVelocityX(-MOVE_SPEED);
    }

    public void moveRight() {
        setVelocityX(MOVE_SPEED);
    }

    public void stopX() {
        setVelocityX(0);
    }

    public void setJumpRequested(boolean v) {
        this.jumpRequested = v;
    }

    public PlayerState getPlayerState() {
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
}

