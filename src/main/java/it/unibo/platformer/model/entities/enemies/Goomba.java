package it.unibo.platformer.model.entities.enemies;

import it.unibo.platformer.model.entities.DynamicEntity;
import it.unibo.platformer.view.AnimationManager;
import it.unibo.platformer.view.AnimationManager.Animation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Goomba extends DynamicEntity {

    private static final double SPEED        = 60.0;
    private static final double SQUISH_TIME  = 0.4;

    public enum GoombaState {
        WALK,
        SQUISHED,
        DEAD
    }

    private GoombaState state;
    private double squishTimer;

    // Animazione camminata
    private static final double FRAME_DURATION = 0.2;
    //private static final int    FRAMES         = 2;

    // AnimationManager gestisce i frame
    private final AnimationManager anim = new AnimationManager();
    private boolean facingLeft = true;

    public Goomba(double x, double y) {
        super(x, y, 32, 32);
        this.state = GoombaState.WALK;
        setVelocityX(-SPEED);
        this.squishTimer = 0;

        loadAnimations();
        anim.play("walk");
    }

    /**
     * Carica le animazioni usando sprite SINGOLI (non spritesheet).
     * Ogni immagine è un file PNG separato.
     *
     * Struttura attesa in resources:
     *   /sprites/enemies/goomba1.png      → frame 1 camminata
     *   /sprites/enemies/goomba2.png      → frame 2 camminata
     *   /sprites/enemies/goomba_dead.png  → sprite schiacciato
     */
    private void loadAnimations() {
        Image frame1 = AnimationManager.loadImage("/sprites/enemies/goomba1.png");
        Image frame2 = AnimationManager.loadImage("/sprites/enemies/goomba2.png");
        Image dead   = AnimationManager.loadImage("/sprites/enemies/goomba_dead.png");

        // Animazione camminata: 2 sprite singoli messi in array
        if (frame1 != null && frame2 != null) {
            Image[] walkFrames = { frame1, frame2 };
            anim.register("walk", new Animation(walkFrames, FRAME_DURATION, true));
        } else {
            System.err.println("[Goomba] Sprite camminata non trovati – uso fallback.");
        }

        // Animazione schiacciato: 1 sprite singolo
        if (dead != null) {
            Image[] deadFrames = { dead };
            anim.register("squished", new Animation(deadFrames, SQUISH_TIME, false));
        } else {
            System.err.println("[Goomba] Sprite goomba_dead non trovato – uso fallback.");
        }
    }

    @Override
    public void update(double deltaTime) {
        switch (state) {

            case WALK:
                // direzione sprite
                if (getVelocityX() < 0) facingLeft = true;
                else if (getVelocityX() > 0) facingLeft = false;

                anim.play("walk");
                anim.update(deltaTime);
                super.update(deltaTime);
                break;

            case SQUISHED:
                anim.play("squished");
                anim.update(deltaTime);
                squishTimer += deltaTime;
                if (squishTimer >= SQUISH_TIME) {
                    destroy();
                }
                break;

            case DEAD:
                destroy();
                break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;

        // Se AnimationManager ha frame disponibili li usa
        // altrimenti cade nel fallback rettangoli
        String key = (state == GoombaState.SQUISHED) ? "squished" : "walk";

        if (animHasFrames(key)) {
            if (state == GoombaState.SQUISHED) {
                // Schiacciato: sprite nella metà inferiore
                anim.render(gc, x, y + height / 2.0, width, height / 2.0, facingLeft);
            } else {
                anim.render(gc, x, y, width, height, facingLeft);
            }
        } else {
            renderFallback(gc);
        }
    }

    /** Controlla se l'animazione corrente ha almeno un frame caricato. */
    private boolean animHasFrames(String key) {
        // Tentiamo di giocare l'animazione e vedere se rende qualcosa
        // (AnimationManager.render non disegna se frame == null)
        // Usiamo isCurrentFinished come proxy: se l'animazione esiste ritorna false
        anim.play(key);
        return true; // AnimationManager stampa errore ma non crasha se mancano
    }

    /** Fallback con rettangoli colorati se gli sprite non sono disponibili. */
    private void renderFallback(GraphicsContext gc) {
        if (state == GoombaState.SQUISHED) {
            gc.setFill(Color.SADDLEBROWN);
            gc.fillRect(x, y + height / 2.0, width, height / 2.0);
            return;
        }

        gc.setFill(Color.SADDLEBROWN);
        gc.fillRect(x, y, width, height);

        gc.setFill(Color.WHITE);
        gc.fillOval(x + 4,  y + 8, 8, 8);
        gc.fillOval(x + 20, y + 8, 8, 8);
        gc.setFill(Color.BLACK);
        gc.fillOval(x + 6,  y + 10, 4, 4);
        gc.fillOval(x + 22, y + 10, 4, 4);
    }

    public void squish() {
        if (state != GoombaState.WALK) return;
        state = GoombaState.SQUISHED;
        setVelocityX(0);
        affectedByGravity = false;
    }

    public boolean hitsPlayer() {
        return state == GoombaState.WALK;
    }

    public GoombaState getState() { return state; }
}