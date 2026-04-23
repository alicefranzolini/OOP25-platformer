package it.unibo.platformer.model.entities.enemies;

import javafx.scene.canvas.GraphicsContext;
import it.unibo.platformer.model.entities.DynamicEntity;
import javafx.scene.paint.Color;


public class Koopa extends DynamicEntity {

    private static final double WALK_SPEED  = 50.0;
    private static final double SHELL_SPEED = 300.0;

    public enum KoopaState { WALK, SHELL, SHELL_MOVING }

    private KoopaState state;
    private double animTimer;
    private int animFrame;
    private static final double FRAME_DURATION = 0.2;

    public Koopa(double x, double y) {
        super(x, y, 32, 48); 
        this.state     = KoopaState.WALK;
        this.setVelocityX (-WALK_SPEED);
    }

    @Override
    public void update(double deltaTime) {
        animTimer += deltaTime;
        if (animTimer >= FRAME_DURATION) {
            animTimer = 0;
            animFrame = (animFrame + 1) % 2;
        }
        if (state == KoopaState.WALK || state == KoopaState.SHELL_MOVING) {
            super.update(deltaTime);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;

        if (state == KoopaState.SHELL || state == KoopaState.SHELL_MOVING) {
           
            gc.setFill(Color.GREEN);
            gc.fillOval(x, y + height / 2, width, height / 2);
            gc.setStroke(Color.DARKGREEN);
            gc.strokeOval(x, y + height / 2, width, height / 2);
            if (state == KoopaState.SHELL_MOVING) {
               
                gc.setStroke(Color.YELLOW);
                gc.strokeLine(x + 4, y + height * 0.6, x + 4, y + height * 0.9);
                gc.strokeLine(x + width - 4, y + height * 0.6, x + width - 4, y + height * 0.9);
            }
            return;
        }

        // body
        gc.setFill(Color.GREEN);
        gc.fillRect(x, y + 16, width, height - 16);

        // head
        gc.setFill(Color.LIGHTYELLOW);
        gc.fillOval(x + 4, y, width - 8, 20);

        //shell
        gc.setFill(Color.DARKGREEN);
        gc.fillOval(x, y + 14, width, height - 20);

        // eyes
        gc.setFill(Color.WHITE);
        gc.fillOval(x + 6,  y + 2, 8, 8);
        gc.fillOval(x + 18, y + 2, 8, 8);
        gc.setFill(Color.BLACK);
        gc.fillOval(x + 8,  y + 4, 4, 4);
        gc.fillOval(x + 20, y + 4, 4, 4);

        // feet
        gc.setFill(Color.LIGHTYELLOW);
        if (animFrame == 0) gc.fillRect(x,              y + height - 8, 12, 8);
        else                gc.fillRect(x + width - 12, y + height - 8, 12, 8);
    }

    // if mario stomp on koopa it became a shell
    public void stomp() {
        if (state != KoopaState.WALK) return;
        state      = KoopaState.SHELL;
        setVelocityX(0);
        height     = 32;
        y  += 16;
        affectedByGravity = true;
    }

    // when mario kick the shell it slides fast
    public void kick(boolean kickRight) {
        if (state != KoopaState.SHELL) return;
        state = KoopaState.SHELL_MOVING; // FIX: stato aggiornato a SHELL_MOVING
        double speed = kickRight ? SHELL_SPEED : -SHELL_SPEED;
        setVelocityX(speed);
    }

    // moving shell kills enemies
    public boolean canKillEnemies() {
        return state == KoopaState.SHELL_MOVING;
    }

    // when mario touches it on side
    public boolean hitsPlayer() {
        return state == KoopaState.WALK || state == KoopaState.SHELL_MOVING;
    }

    public KoopaState getKoopaState() { return state; }
}