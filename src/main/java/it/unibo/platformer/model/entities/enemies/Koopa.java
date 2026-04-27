package it.unibo.platformer.model.entities.enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;

public class Koopa extends DynamicEntity {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final double WALK_SPEED  = 50.0;
    private static final double SHELL_SPEED = 300.0;

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    public enum KoopaState { WALK, SHELL, SHELL_MOVING }

    private KoopaState state;

    // -------------------------------------------------------------------------
    // Animation
    // -------------------------------------------------------------------------

    private final AnimationManager anim = new AnimationManager();
    private boolean facingLeft = true;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public Koopa(double x, double y) {
        super(x, y, 32, 48);
        this.state = KoopaState.WALK;
        this.setVelocityX(-WALK_SPEED);

        loadAnimations();
        anim.play("walk");
    }

    // -------------------------------------------------------------------------
    // Animation setup — sprite SINGOLI (non spritesheet)
    // -------------------------------------------------------------------------

    /**
     * Carica le animazioni usando file PNG singoli (uno per frame).
     *
     * Struttura attesa in resources:
     *   /sprites/enemies/koopa1.png       → frame 1 camminata
     *   /sprites/enemies/koopa2.png       → frame 2 camminata
     *   /sprites/enemies/koopa_shell.png  → guscio fermo e guscio in moto
     */
    private void loadAnimations() {
        Image walkFrame1  = AnimationManager.loadImage("src\\main\\resources\\sprites\\koopa1.png");
        Image walkFrame2  = AnimationManager.loadImage("src\\main\\resources\\sprites\\koopa2.png");
        Image shellSprite = AnimationManager.loadImage("src\\main\\resources\\sprites\\koopa_shell.png");

        // --- Walk: 2 sprite singoli, loop a 0.15 s ---
        if (walkFrame1 != null && walkFrame2 != null) {
            Image[] walkFrames = { walkFrame1, walkFrame2 };
            anim.register("walk", new Animation(walkFrames, 0.15, true));
        } else {
            System.err.println("[Koopa] Sprite camminata non trovati – uso fallback.");
        }

        // --- Shell (fermo): 1 sprite, non loopato ---
        if (shellSprite != null) {
            Image[] shellFrames = { shellSprite };
            anim.register("shell", new Animation(shellFrames, 1.0, false));

            // --- Shell moving: stesso sprite del guscio ma animato velocemente ---
            // Usiamo lo stesso PNG; l'effetto di moto viene dalla velocità
            Image[] movingFrames = { shellSprite };
            anim.register("shell_moving", new Animation(movingFrames, 0.08, true));
        } else {
            System.err.println("[Koopa] Sprite koopa_shell non trovato – uso fallback.");
        }
    }

    // -------------------------------------------------------------------------
    // Game logic
    // -------------------------------------------------------------------------

    @Override
    public void update(double deltaTime) {
        // direzione sprite
        double vx = getVelocityX();
        if (vx < 0) facingLeft = true;
        else if (vx > 0) facingLeft = false;

        // sync animazione allo stato
        switch (state) {
            case WALK         -> anim.play("walk");
            case SHELL        -> anim.play("shell");
            case SHELL_MOVING -> anim.play("shell_moving");
        }

        anim.update(deltaTime);

        // fisica solo quando si muove
        if (state == KoopaState.WALK || state == KoopaState.SHELL_MOVING) {
            super.update(deltaTime);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;

        if (state == KoopaState.SHELL || state == KoopaState.SHELL_MOVING) {
            // Il guscio è più basso: 32x32 invece di 32x48
            anim.render(gc, x, y, width, 32, facingLeft);
        } else {
            anim.render(gc, x, y, width, height, facingLeft);
        }
    }

    // -------------------------------------------------------------------------
    // State transitions
    // -------------------------------------------------------------------------

    /** Mario ha saltato su Koopa → diventa guscio fermo. */
    public void stomp() {
        if (state != KoopaState.WALK) return;
        state = KoopaState.SHELL;
        setVelocityX(0);
        height = 32;
        y += 16;
        affectedByGravity = true;
    }

    /** Mario ha calciato il guscio → scivola velocemente. */
    public void kick(boolean kickRight) {
        if (state != KoopaState.SHELL) return;
        state = KoopaState.SHELL_MOVING;
        double speed = kickRight ? SHELL_SPEED : -SHELL_SPEED;
        setVelocityX(speed);
    }

    // -------------------------------------------------------------------------
    // Queries
    // -------------------------------------------------------------------------

    public boolean canKillEnemies() {
        return state == KoopaState.SHELL_MOVING;
    }

    public boolean hitsPlayer() {
        return state == KoopaState.WALK || state == KoopaState.SHELL_MOVING;
    }

    public KoopaState getKoopaState() { return state; }
}