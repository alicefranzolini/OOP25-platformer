package it.unibo.platformer.view;

import it.unibo.platformer.controller.GameManager.GameState;
import it.unibo.platformer.model.score.ScoreSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

// CHECKSTYLE: MagicNumber OFF
/**
 * Draws the menu, HUD, and simple game state messages.
 */
public final class HudView {

    private static final double TEXT_X = 20;
    private static final double FIRST_TEXT_Y = 32;
    private static final double HUD_BOX_WIDTH = 150;
    private static final double HUD_BOX_HEIGHT = 48;
    private static final double HUD_BOX_DISTANCE = 15;
    private static final double FONT_SIZE = 18;
    private static final double SMALL_FONT_SIZE = 15;
    private static final double TITLE_FONT_SIZE = 48;
    private static final double MENU_FONT_SIZE = 22;

    /**
     * Renders the correct HUD screen for the current game state.
     *
     * @param gc the canvas graphics context
     * @param scoreSystem the current score system
     * @param levelNumber the current level number
     * @param gameState the current game state
     * @param viewWidth the visible window width
     * @param viewHeight the visible window height
     */
    public void render(
        final GraphicsContext gc,
        final ScoreSystem scoreSystem,
        final int levelNumber,
        final GameState gameState,
        final double viewWidth,
        final double viewHeight
    ) {
        if (gameState == GameState.MENU) {
            renderMenu(gc, viewWidth, viewHeight);
            return;
        }

        renderStats(gc, scoreSystem, levelNumber);
        renderStateMessage(gc, gameState, viewWidth, viewHeight);
    }

    private void renderStats(final GraphicsContext gc, final ScoreSystem scoreSystem, final int levelNumber) {
        gc.setFill(Color.rgb(12, 18, 34, 0.78));
        gc.fillRoundRect(12, 12, 690, 70, 18, 18);
        gc.setStroke(Color.rgb(255, 255, 255, 0.18));
        gc.strokeRoundRect(12, 12, 690, 70, 18, 18);

        drawHudBox(gc, TEXT_X, "SCORE", String.valueOf(scoreSystem.getScore()));
        drawHudBox(gc, TEXT_X + (HUD_BOX_WIDTH + HUD_BOX_DISTANCE), "COINS", String.valueOf(scoreSystem.getCoins()));
        drawHudBox(
            gc,
            TEXT_X + (HUD_BOX_WIDTH + HUD_BOX_DISTANCE) * 2,
            "LIVES",
            String.valueOf(scoreSystem.getLives())
        );
        drawHudBox(gc, TEXT_X + (HUD_BOX_WIDTH + HUD_BOX_DISTANCE) * 3, "LEVEL", String.valueOf(levelNumber));
    }

    private void drawHudBox(
        final GraphicsContext gc,
        final double x,
        final String label,
        final String value
    ) {
        gc.setFill(Color.rgb(255, 255, 255, 0.12));
        gc.fillRoundRect(x, 23, HUD_BOX_WIDTH, HUD_BOX_HEIGHT, 12, 12);
        gc.setFill(Color.rgb(255, 211, 77));
        gc.setFont(Font.font(SMALL_FONT_SIZE));
        gc.fillText(label, x + 12, FIRST_TEXT_Y + 2);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(FONT_SIZE));
        gc.fillText(value, x + 12, FIRST_TEXT_Y + 25);
    }

    private void renderStateMessage(
        final GraphicsContext gc,
        final GameState gameState,
        final double viewWidth,
        final double viewHeight
    ) {
        if (gameState == GameState.PAUSED) {
            gc.setFill(Color.rgb(0, 0, 0, 0.65));
            gc.fillRect(0, 0, viewWidth, viewHeight);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(TITLE_FONT_SIZE));
            gc.fillText("PAUSED", viewWidth / 2 - 80, viewHeight / 2 - 30);
            gc.setFont(Font.font(MENU_FONT_SIZE));
            gc.fillText("ESC: menu", viewWidth / 2 - 80, viewHeight / 2 + 15);
            gc.fillText("R: restart", viewWidth / 2 - 80, viewHeight / 2 + 45);
        } else if (gameState == GameState.GAME_OVER) {
            renderCenteredMessage(gc, viewWidth, viewHeight, "GAME OVER", "Press R to restart");
        } else if (gameState == GameState.VICTORY) {
            renderCenteredMessage(gc, viewWidth, viewHeight, "LEVEL COMPLETE!", "Returning to menu...");
        }
    }

    private void renderMenu(final GraphicsContext gc, final double viewWidth, final double viewHeight) {
        final double centerX = viewWidth / 2;
        final double startY = viewHeight / 2 - 150;

        gc.setFill(Color.rgb(22, 31, 56));
        gc.fillRect(0, 0, viewWidth, viewHeight);
        gc.setFill(Color.rgb(80, 166, 255));
        gc.fillRect(0, viewHeight - 150, viewWidth, 150);
        gc.setFill(Color.rgb(88, 190, 92));
        gc.fillRect(0, viewHeight - 105, viewWidth, 105);
        gc.setFill(Color.rgb(55, 133, 63));
        gc.fillRect(0, viewHeight - 75, viewWidth, 75);

        gc.setFill(Color.rgb(255, 211, 77));
        gc.fillRoundRect(centerX - 210, startY - 50, 420, 82, 22, 22);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(TITLE_FONT_SIZE));
        gc.fillText("PLATFORMER", centerX - 165, startY);

        drawMenuOption(gc, centerX, startY + 70, "1", "Level 1");
        drawMenuOption(gc, centerX, startY + 125, "2", "Level 2");
        drawMenuOption(gc, centerX, startY + 180, "3", "Level 3");

        gc.setFill(Color.rgb(255, 255, 255, 0.88));
        gc.setFont(Font.font(FONT_SIZE));
        gc.fillText("Arrows: move    Space: jump    ESC: pause", centerX - 190, startY + 250);
    }

    private void drawMenuOption(
        final GraphicsContext gc,
        final double centerX,
        final double y,
        final String key,
        final String text
    ) {
        gc.setFill(Color.rgb(255, 255, 255, 0.14));
        gc.fillRoundRect(centerX - 170, y - 30, 340, 42, 14, 14);
        gc.setStroke(Color.rgb(255, 255, 255, 0.22));
        gc.strokeRoundRect(centerX - 170, y - 30, 340, 42, 14, 14);
        gc.setFill(Color.rgb(255, 211, 77));
        gc.setFont(Font.font(MENU_FONT_SIZE));
        gc.fillText(key, centerX - 140, y);
        gc.setFill(Color.WHITE);
        gc.fillText(text, centerX - 90, y);
    }

    private void renderCenteredMessage(
        final GraphicsContext gc,
        final double viewWidth,
        final double viewHeight,
        final String title,
        final String subtitle
    ) {
        gc.setFill(Color.rgb(0, 0, 0, 0.62));
        gc.fillRect(0, 0, viewWidth, viewHeight);
        gc.setFill(Color.rgb(255, 211, 77));
        gc.fillRoundRect(viewWidth / 2 - 230, viewHeight / 2 - 95, 460, 150, 24, 24);
        gc.setFill(Color.rgb(20, 28, 48));
        gc.fillRoundRect(viewWidth / 2 - 220, viewHeight / 2 - 85, 440, 130, 20, 20);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(TITLE_FONT_SIZE));
        gc.fillText(title, viewWidth / 2 - 190, viewHeight / 2 - 25);
        gc.setFont(Font.font(MENU_FONT_SIZE));
        gc.fillText(subtitle, viewWidth / 2 - 120, viewHeight / 2 + 20);
    }
}
// CHECKSTYLE: MagicNumber ON
