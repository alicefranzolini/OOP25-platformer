package it.unibo.platformer.model.entities.powerup;

import it.unibo.platformer.model.entities.players.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class StarPowerUp extends PowerUp {

    // the star jump insted of walking
    private static final double BOUNCE_VELOCITY = -400.0;

    // Create a star power-up with a 32x32 size
    public StarPowerUp(double x, double y) {
        super(x, y, 32, 32);
    }

    @Override
    public void update(double deltaTime) {
        // Run base update (emergence / normal physics)
        super.update(deltaTime);
        
         // If the star is on the ground, give it an upward velocity to bounce
         // and clear the onGround flag so physics continues 

        if (isOnGround()) {
            setVelocityY(BOUNCE_VELOCITY);
            setOnGround(false);
        }
    }

    /*
    @Override
    public void applyEffect(Object playerObj) {
        if (!(playerObj instanceof Player)) return;
        Player player = (Player) playerObj;
        player.setState(Player.PlayerState.INVINCIBLE);
    }
*/
    @Override
    public void applyEffect(Player player) {
        if (player == null) return;
        player.setState(Player.PlayerState.INVINCIBLE);
    }
    
    /*
    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;
        // Draw a simple yellow star polygon centered on x,y
        // Use stroke to add an orange outline
        // Replace with sprite drawing if you have assets
        gc.setFill(Color.YELLOW);
        double cx = x + width / 2;
        double cy = y + height / 2;
        double r  = width / 2;
        double[] px = new double[10];
        double[] py = new double[10];
        for (int i = 0; i < 10; i++) {
            double angle  = Math.PI / 2 + i * Math.PI / 5;
            double radius = (i % 2 == 0) ? r : r * 0.45;
            px[i] = cx + radius * Math.cos(angle);
            py[i] = cy - radius * Math.sin(angle);
        }
        gc.fillPolygon(px, py, 10);
        gc.setStroke(Color.ORANGE);
        gc.strokePolygon(px, py, 10);
    }
    */

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;
        double cx = getX() + getWidth() / 2;
        double cy = getY() + getHeight() / 2;
        double r  = getWidth() / 2;
        double[] px = new double[10];
        double[] py = new double[10];
        for (int i = 0; i < 10; i++) {
            double angle  = Math.PI / 2 + i * Math.PI / 5;
            double radius = (i % 2 == 0) ? r : r * 0.45;
            px[i] = cx + radius * Math.cos(angle);
            py[i] = cy - radius * Math.sin(angle);
        }
        gc.setFill(Color.YELLOW);
        gc.fillPolygon(px, py, 10);
        gc.setStroke(Color.ORANGE);
        gc.strokePolygon(px, py, 10);
    }

}

// When setting Player.PlayerState.INVINCIBLE, ensure Player starts a timer
// to revert the state after the invincibility duration expires.
