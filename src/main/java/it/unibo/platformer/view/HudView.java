package it.unibo.platformer.view;

import it.unibo.platformer.controller.GameManager.GameState;
import it.unibo.platformer.model.score.ScoreSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Draws the menu, HUD, and simple game state messages.
 */
public final class HudView {

    private static final double ZERO = 0.0;
    private static final double CENTER_DIVISOR = 2.0;
    private static final double TEXT_X = 20;
    private static final double FIRST_TEXT_Y = 32;
    private static final double HUD_BOX_WIDTH = 150;
    private static final double HUD_BOX_HEIGHT = 48;
    private static final double HUD_BOX_DISTANCE = 15;
    private static final double FONT_SIZE = 18;
    private static final double SMALL_FONT_SIZE = 15;
    private static final double TITLE_FONT_SIZE = 48;
    private static final double MENU_FONT_SIZE = 22;
    private static final int LEVEL_BOX_INDEX = 3;
    private static final double HUD_PANEL_X = 12;
    private static final double HUD_PANEL_Y = 12;
    private static final double HUD_PANEL_WIDTH = 690;
    private static final double HUD_PANEL_HEIGHT = 70;
    private static final double HUD_PANEL_ARC = 18;
    private static final double HUD_BOX_Y = 23;
    private static final double HUD_BOX_ARC = 12;
    private static final double HUD_TEXT_X_OFFSET = 12;
    private static final double HUD_LABEL_Y_OFFSET = 2;
    private static final double HUD_VALUE_Y_OFFSET = 25;
    private static final double HUD_BOX_ALPHA = 0.12;
    private static final double HUD_PANEL_ALPHA = 0.78;
    private static final double HUD_STROKE_ALPHA = 0.18;
    private static final int DARK_PANEL_RED = 12;
    private static final int DARK_PANEL_GREEN = 18;
    private static final int DARK_PANEL_BLUE = 34;
    private static final int WHITE_RGB = 255;
    private static final int ACCENT_RED = 255;
    private static final int ACCENT_GREEN = 211;
    private static final int ACCENT_BLUE = 77;
    private static final double OVERLAY_ALPHA = 0.65;
    private static final double MESSAGE_OVERLAY_ALPHA = 0.62;
    private static final int BLACK_RGB = 0;
    private static final double PAUSE_TITLE_X_OFFSET = 80;
    private static final double PAUSE_TITLE_Y_OFFSET = 30;
    private static final double PAUSE_OPTION_FIRST_Y_OFFSET = 15;
    private static final double PAUSE_OPTION_SECOND_Y_OFFSET = 45;
    private static final double PAUSE_OPTION_THIRD_Y_OFFSET = 75;
    private static final int MENU_SKY_RED = 22;
    private static final int MENU_SKY_GREEN = 31;
    private static final int MENU_SKY_BLUE = 56;
    private static final int MENU_WATER_RED = 80;
    private static final int MENU_WATER_GREEN = 166;
    private static final int MENU_WATER_BLUE = 255;
    private static final int MENU_GRASS_RED = 88;
    private static final int MENU_GRASS_GREEN = 190;
    private static final int MENU_GRASS_BLUE = 92;
    private static final int MENU_GROUND_RED = 55;
    private static final int MENU_GROUND_GREEN = 133;
    private static final int MENU_GROUND_BLUE = 63;
    private static final double MENU_START_Y_OFFSET = 150;
    private static final double MENU_WATER_HEIGHT = 150;
    private static final double MENU_GRASS_HEIGHT = 105;
    private static final double MENU_GROUND_HEIGHT = 75;
    private static final double TITLE_PANEL_X_OFFSET = 210;
    private static final double TITLE_PANEL_Y_OFFSET = 50;
    private static final double TITLE_PANEL_WIDTH = 420;
    private static final double TITLE_PANEL_HEIGHT = 82;
    private static final double TITLE_PANEL_ARC = 22;
    private static final double TITLE_TEXT_X_OFFSET = 165;
    private static final double FIRST_MENU_OPTION_Y_OFFSET = 70;
    private static final double SECOND_MENU_OPTION_Y_OFFSET = 125;
    private static final double THIRD_MENU_OPTION_Y_OFFSET = 180;
    private static final double HELP_TEXT_X_OFFSET = 190;
    private static final double HELP_TEXT_Y_OFFSET = 250;
    private static final double HELP_TEXT_ALPHA = 0.88;
    private static final double OPTION_BOX_X_OFFSET = 170;
    private static final double OPTION_BOX_Y_OFFSET = 30;
    private static final double OPTION_BOX_WIDTH = 340;
    private static final double OPTION_BOX_HEIGHT = 42;
    private static final double OPTION_BOX_ARC = 14;
    private static final double OPTION_BOX_ALPHA = 0.14;
    private static final double OPTION_STROKE_ALPHA = 0.22;
    private static final double OPTION_KEY_X_OFFSET = 140;
    private static final double OPTION_TEXT_X_OFFSET = 90;
    private static final double MESSAGE_OUTER_X_OFFSET = 230;
    private static final double MESSAGE_OUTER_Y_OFFSET = 95;
    private static final double MESSAGE_OUTER_WIDTH = 460;
    private static final double MESSAGE_OUTER_HEIGHT = 150;
    private static final double MESSAGE_OUTER_ARC = 24;
    private static final double MESSAGE_INNER_X_OFFSET = 220;
    private static final double MESSAGE_INNER_Y_OFFSET = 85;
    private static final double MESSAGE_INNER_WIDTH = 440;
    private static final double MESSAGE_INNER_HEIGHT = 130;
    private static final double MESSAGE_INNER_ARC = 20;
    private static final int MESSAGE_PANEL_RED = 20;
    private static final int MESSAGE_PANEL_GREEN = 28;
    private static final int MESSAGE_PANEL_BLUE = 48;
    private static final double MESSAGE_TITLE_X_OFFSET = 190;
    private static final double MESSAGE_TITLE_Y_OFFSET = 25;
    private static final double MESSAGE_SUBTITLE_X_OFFSET = 120;
    private static final double MESSAGE_SUBTITLE_Y_OFFSET = 20;

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
        gc.setFill(Color.rgb(DARK_PANEL_RED, DARK_PANEL_GREEN, DARK_PANEL_BLUE, HUD_PANEL_ALPHA));
        gc.fillRoundRect(
            HUD_PANEL_X,
            HUD_PANEL_Y,
            HUD_PANEL_WIDTH,
            HUD_PANEL_HEIGHT,
            HUD_PANEL_ARC,
            HUD_PANEL_ARC
        );
        gc.setStroke(Color.rgb(WHITE_RGB, WHITE_RGB, WHITE_RGB, HUD_STROKE_ALPHA));
        gc.strokeRoundRect(
            HUD_PANEL_X,
            HUD_PANEL_Y,
            HUD_PANEL_WIDTH,
            HUD_PANEL_HEIGHT,
            HUD_PANEL_ARC,
            HUD_PANEL_ARC
        );

        drawHudBox(gc, TEXT_X, "SCORE", String.valueOf(scoreSystem.getScore()));
        drawHudBox(gc, TEXT_X + (HUD_BOX_WIDTH + HUD_BOX_DISTANCE), "COINS", String.valueOf(scoreSystem.getCoins()));
        drawHudBox(
            gc,
            TEXT_X + (HUD_BOX_WIDTH + HUD_BOX_DISTANCE) * CENTER_DIVISOR,
            "LIVES",
            String.valueOf(scoreSystem.getLives())
        );
        drawHudBox(
            gc,
            TEXT_X + (HUD_BOX_WIDTH + HUD_BOX_DISTANCE) * LEVEL_BOX_INDEX,
            "LEVEL",
            String.valueOf(levelNumber)
        );
    }

    private void drawHudBox(
        final GraphicsContext gc,
        final double x,
        final String label,
        final String value
    ) {
        gc.setFill(Color.rgb(WHITE_RGB, WHITE_RGB, WHITE_RGB, HUD_BOX_ALPHA));
        gc.fillRoundRect(x, HUD_BOX_Y, HUD_BOX_WIDTH, HUD_BOX_HEIGHT, HUD_BOX_ARC, HUD_BOX_ARC);
        gc.setFill(Color.rgb(ACCENT_RED, ACCENT_GREEN, ACCENT_BLUE));
        gc.setFont(Font.font(SMALL_FONT_SIZE));
        gc.fillText(label, x + HUD_TEXT_X_OFFSET, FIRST_TEXT_Y + HUD_LABEL_Y_OFFSET);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(FONT_SIZE));
        gc.fillText(value, x + HUD_TEXT_X_OFFSET, FIRST_TEXT_Y + HUD_VALUE_Y_OFFSET);
    }

    private void renderStateMessage(
        final GraphicsContext gc,
        final GameState gameState,
        final double viewWidth,
        final double viewHeight
    ) {
        if (gameState == GameState.PAUSED) {
            gc.setFill(Color.rgb(BLACK_RGB, BLACK_RGB, BLACK_RGB, OVERLAY_ALPHA));
            gc.fillRect(ZERO, ZERO, viewWidth, viewHeight);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font(TITLE_FONT_SIZE));
            gc.fillText(
                "PAUSED",
                viewWidth / CENTER_DIVISOR - PAUSE_TITLE_X_OFFSET,
                viewHeight / CENTER_DIVISOR - PAUSE_TITLE_Y_OFFSET
            );
            gc.setFont(Font.font(MENU_FONT_SIZE));
            gc.fillText(
                "ESC: resume",
                viewWidth / CENTER_DIVISOR - PAUSE_TITLE_X_OFFSET,
                viewHeight / CENTER_DIVISOR + PAUSE_OPTION_FIRST_Y_OFFSET
            );
            gc.fillText(
                "M: menu",
                viewWidth / CENTER_DIVISOR - PAUSE_TITLE_X_OFFSET,
                viewHeight / CENTER_DIVISOR + PAUSE_OPTION_SECOND_Y_OFFSET
            );
            gc.fillText(
                "R: restart",
                viewWidth / CENTER_DIVISOR - PAUSE_TITLE_X_OFFSET,
                viewHeight / CENTER_DIVISOR + PAUSE_OPTION_THIRD_Y_OFFSET
            );
        } else if (gameState == GameState.GAME_OVER) {
            renderCenteredMessage(gc, viewWidth, viewHeight, "GAME OVER", "Press R to restart");
        } else if (gameState == GameState.VICTORY) {
            renderCenteredMessage(gc, viewWidth, viewHeight, "LEVEL COMPLETE!", "Returning to menu...");
        }
    }

    private void renderMenu(final GraphicsContext gc, final double viewWidth, final double viewHeight) {
        final double centerX = viewWidth / CENTER_DIVISOR;
        final double startY = viewHeight / CENTER_DIVISOR - MENU_START_Y_OFFSET;

        gc.setFill(Color.rgb(MENU_SKY_RED, MENU_SKY_GREEN, MENU_SKY_BLUE));
        gc.fillRect(ZERO, ZERO, viewWidth, viewHeight);
        gc.setFill(Color.rgb(MENU_WATER_RED, MENU_WATER_GREEN, MENU_WATER_BLUE));
        gc.fillRect(ZERO, viewHeight - MENU_WATER_HEIGHT, viewWidth, MENU_WATER_HEIGHT);
        gc.setFill(Color.rgb(MENU_GRASS_RED, MENU_GRASS_GREEN, MENU_GRASS_BLUE));
        gc.fillRect(ZERO, viewHeight - MENU_GRASS_HEIGHT, viewWidth, MENU_GRASS_HEIGHT);
        gc.setFill(Color.rgb(MENU_GROUND_RED, MENU_GROUND_GREEN, MENU_GROUND_BLUE));
        gc.fillRect(ZERO, viewHeight - MENU_GROUND_HEIGHT, viewWidth, MENU_GROUND_HEIGHT);

        gc.setFill(Color.rgb(ACCENT_RED, ACCENT_GREEN, ACCENT_BLUE));
        gc.fillRoundRect(
            centerX - TITLE_PANEL_X_OFFSET,
            startY - TITLE_PANEL_Y_OFFSET,
            TITLE_PANEL_WIDTH,
            TITLE_PANEL_HEIGHT,
            TITLE_PANEL_ARC,
            TITLE_PANEL_ARC
        );
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(TITLE_FONT_SIZE));
        gc.fillText("PLATFORMER", centerX - TITLE_TEXT_X_OFFSET, startY);

        drawMenuOption(gc, centerX, startY + FIRST_MENU_OPTION_Y_OFFSET, "1", "Level 1");
        drawMenuOption(gc, centerX, startY + SECOND_MENU_OPTION_Y_OFFSET, "2", "Level 2");
        drawMenuOption(gc, centerX, startY + THIRD_MENU_OPTION_Y_OFFSET, "3", "Level 3");

        gc.setFill(Color.rgb(WHITE_RGB, WHITE_RGB, WHITE_RGB, HELP_TEXT_ALPHA));
        gc.setFont(Font.font(FONT_SIZE));
        gc.fillText(
            "Arrows: move    Space: jump    ESC: pause",
            centerX - HELP_TEXT_X_OFFSET,
            startY + HELP_TEXT_Y_OFFSET
        );
    }

    private void drawMenuOption(
        final GraphicsContext gc,
        final double centerX,
        final double y,
        final String key,
        final String text
    ) {
        gc.setFill(Color.rgb(WHITE_RGB, WHITE_RGB, WHITE_RGB, OPTION_BOX_ALPHA));
        gc.fillRoundRect(
            centerX - OPTION_BOX_X_OFFSET,
            y - OPTION_BOX_Y_OFFSET,
            OPTION_BOX_WIDTH,
            OPTION_BOX_HEIGHT,
            OPTION_BOX_ARC,
            OPTION_BOX_ARC
        );
        gc.setStroke(Color.rgb(WHITE_RGB, WHITE_RGB, WHITE_RGB, OPTION_STROKE_ALPHA));
        gc.strokeRoundRect(
            centerX - OPTION_BOX_X_OFFSET,
            y - OPTION_BOX_Y_OFFSET,
            OPTION_BOX_WIDTH,
            OPTION_BOX_HEIGHT,
            OPTION_BOX_ARC,
            OPTION_BOX_ARC
        );
        gc.setFill(Color.rgb(ACCENT_RED, ACCENT_GREEN, ACCENT_BLUE));
        gc.setFont(Font.font(MENU_FONT_SIZE));
        gc.fillText(key, centerX - OPTION_KEY_X_OFFSET, y);
        gc.setFill(Color.WHITE);
        gc.fillText(text, centerX - OPTION_TEXT_X_OFFSET, y);
    }

    private void renderCenteredMessage(
        final GraphicsContext gc,
        final double viewWidth,
        final double viewHeight,
        final String title,
        final String subtitle
    ) {
        gc.setFill(Color.rgb(BLACK_RGB, BLACK_RGB, BLACK_RGB, MESSAGE_OVERLAY_ALPHA));
        gc.fillRect(ZERO, ZERO, viewWidth, viewHeight);
        gc.setFill(Color.rgb(ACCENT_RED, ACCENT_GREEN, ACCENT_BLUE));
        gc.fillRoundRect(
            viewWidth / CENTER_DIVISOR - MESSAGE_OUTER_X_OFFSET,
            viewHeight / CENTER_DIVISOR - MESSAGE_OUTER_Y_OFFSET,
            MESSAGE_OUTER_WIDTH,
            MESSAGE_OUTER_HEIGHT,
            MESSAGE_OUTER_ARC,
            MESSAGE_OUTER_ARC
        );
        gc.setFill(Color.rgb(MESSAGE_PANEL_RED, MESSAGE_PANEL_GREEN, MESSAGE_PANEL_BLUE));
        gc.fillRoundRect(
            viewWidth / CENTER_DIVISOR - MESSAGE_INNER_X_OFFSET,
            viewHeight / CENTER_DIVISOR - MESSAGE_INNER_Y_OFFSET,
            MESSAGE_INNER_WIDTH,
            MESSAGE_INNER_HEIGHT,
            MESSAGE_INNER_ARC,
            MESSAGE_INNER_ARC
        );
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font(TITLE_FONT_SIZE));
        gc.fillText(
            title,
            viewWidth / CENTER_DIVISOR - MESSAGE_TITLE_X_OFFSET,
            viewHeight / CENTER_DIVISOR - MESSAGE_TITLE_Y_OFFSET
        );
        gc.setFont(Font.font(MENU_FONT_SIZE));
        gc.fillText(
            subtitle,
            viewWidth / CENTER_DIVISOR - MESSAGE_SUBTITLE_X_OFFSET,
            viewHeight / CENTER_DIVISOR + MESSAGE_SUBTITLE_Y_OFFSET
        );
    }
}
