package it.unibo.platformer.view;

import it.unibo.platformer.controller.GameManager.GameState;
import it.unibo.platformer.model.score.ScoreSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HudView {

    private static final double TEXT_X = 20;
    private static final double FIRST_TEXT_Y = 30;
    private static final double TEXT_DISTANCE = 20;
    private static final double MESSAGE_Y = 40;
    private static final double MESSAGE_CENTER_OFFSET = 45;
    private static final double FONT_SIZE = 18;

    public void render(
        final GraphicsContext gc,
        final ScoreSystem scoreSystem,
        final int levelNumber,
        final GameState gameState,
        final double viewWidth
    ) {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(FONT_SIZE));

        gc.fillText("Score: " + scoreSystem.getScore(), TEXT_X, FIRST_TEXT_Y);
        gc.fillText("Coins: " + scoreSystem.getCoins(), TEXT_X, FIRST_TEXT_Y + TEXT_DISTANCE);
        gc.fillText("Lives: " + scoreSystem.getLives(), TEXT_X, FIRST_TEXT_Y + TEXT_DISTANCE * 2);
        gc.fillText("Level: " + levelNumber, TEXT_X, FIRST_TEXT_Y + TEXT_DISTANCE * 3);

        renderStateMessage(gc, gameState, viewWidth);
        renderMenuHelp(gc, gameState);
    }

    private void renderStateMessage(
        final GraphicsContext gc,
        final GameState gameState,
        final double viewWidth
    ) {
        if (gameState == GameState.PAUSED) {
            gc.fillText("PAUSED", viewWidth / 2 - MESSAGE_CENTER_OFFSET, MESSAGE_Y);
        } else if (gameState == GameState.GAME_OVER) {
            gc.fillText("GAME OVER", viewWidth / 2 - MESSAGE_CENTER_OFFSET, MESSAGE_Y);
        }
    }

    private void renderMenuHelp(final GraphicsContext gc, final GameState gameState) {
        if (gameState == GameState.MENU) {
            gc.fillText("Press 1, 2 or 3 to select a level", TEXT_X, FIRST_TEXT_Y + TEXT_DISTANCE * 5);
        }
    }
}
