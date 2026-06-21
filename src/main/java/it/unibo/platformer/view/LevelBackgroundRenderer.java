package it.unibo.platformer.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Draws the background used by the playable levels.
 */
public final class LevelBackgroundRenderer {

    private static final int LEVEL_TWO = 2;
    private static final int LEVEL_THREE = 3;
    private static final double ORIGIN = 0.0;
    private static final int INITIAL_INDEX = 0;
    private static final int LEVEL_ONE_SKY_RED = 113;
    private static final int LEVEL_ONE_SKY_GREEN = 190;
    private static final int LEVEL_ONE_SKY_BLUE = 244;
    private static final int LEVEL_TWO_SKY_RED = 99;
    private static final int LEVEL_TWO_SKY_GREEN = 170;
    private static final int LEVEL_TWO_SKY_BLUE = 224;
    private static final int LEVEL_THREE_SKY_RED = 78;
    private static final int LEVEL_THREE_SKY_GREEN = 126;
    private static final int LEVEL_THREE_SKY_BLUE = 193;
    private static final double SUN_BASE_X = 180.0;
    private static final double SUN_BASE_Y = 80.0;
    private static final double SUN_LEVEL_X_STEP = 45.0;
    private static final double SUN_LEVEL_Y_STEP = 10.0;
    private static final double SUN_SIZE = 70.0;
    private static final double SUN_INNER_OFFSET = 12.0;
    private static final double SUN_INNER_SIZE = 46.0;
    private static final int SUN_RED = 255;
    private static final int SUN_GREEN = 222;
    private static final int SUN_BLUE = 89;
    private static final double SUN_ALPHA = 0.9;
    private static final int SUN_INNER_RED = 255;
    private static final int SUN_INNER_GREEN = 244;
    private static final int SUN_INNER_BLUE = 170;
    private static final double SUN_INNER_ALPHA = 0.65;
    private static final double CLOUD_START_X = 260.0;
    private static final double CLOUD_SPACING = 520.0;
    private static final double CLOUD_BASE_Y = 85.0;
    private static final double CLOUD_VARIATION_STEP = 18.0;
    private static final int CLOUD_VARIATIONS = 3;
    private static final int CLOUD_RED = 255;
    private static final int CLOUD_GREEN = 255;
    private static final int CLOUD_BLUE = 255;
    private static final double CLOUD_ALPHA = 0.78;
    private static final double CLOUD_LEFT_WIDTH = 70.0;
    private static final double CLOUD_LEFT_HEIGHT = 34.0;
    private static final double CLOUD_MIDDLE_X_OFFSET = 35.0;
    private static final double CLOUD_MIDDLE_Y_OFFSET = 18.0;
    private static final double CLOUD_MIDDLE_WIDTH = 78.0;
    private static final double CLOUD_MIDDLE_HEIGHT = 48.0;
    private static final double CLOUD_RIGHT_X_OFFSET = 82.0;
    private static final double CLOUD_RIGHT_WIDTH = 76.0;
    private static final double CLOUD_RIGHT_HEIGHT = 34.0;
    private static final double HILL_START_X = -80.0;
    private static final double HILL_SPACING = 360.0;
    private static final int HILL_FRONT_RED = 84;
    private static final int HILL_FRONT_GREEN = 174;
    private static final int HILL_FRONT_BLUE = 91;
    private static final int HILL_BACK_RED = 63;
    private static final int HILL_BACK_GREEN = 145;
    private static final int HILL_BACK_BLUE = 76;
    private static final double HILL_FRONT_Y_OFFSET = 245.0;
    private static final double HILL_FRONT_WIDTH = 280.0;
    private static final double HILL_FRONT_HEIGHT = 220.0;
    private static final double HILL_BACK_X_OFFSET = 120.0;
    private static final double HILL_BACK_Y_OFFSET = 195.0;
    private static final double HILL_BACK_WIDTH = 260.0;
    private static final double HILL_BACK_HEIGHT = 170.0;
    private static final int GROUND_SHADOW_RED = 67;
    private static final int GROUND_SHADOW_GREEN = 128;
    private static final int GROUND_SHADOW_BLUE = 73;
    private static final double GROUND_SHADOW_ALPHA = 0.45;
    private static final int GROUND_DARK_RED = 42;
    private static final int GROUND_DARK_GREEN = 93;
    private static final int GROUND_DARK_BLUE = 59;
    private static final double GROUND_DARK_ALPHA = 0.35;
    private static final double GROUND_SHADOW_HEIGHT = 175.0;
    private static final double GROUND_DARK_Y = 500.0;
    private static final double GROUND_DARK_HEIGHT = 18.0;

    /**
     * Draws the background for the selected level.
     *
     * @param gc the canvas graphics context
     * @param levelNumber the current level number
     * @param levelWidth the complete level width
     * @param levelHeight the complete level height
     */
    public void render(
        final GraphicsContext gc,
        final int levelNumber,
        final double levelWidth,
        final double levelHeight
    ) {
        final double backgroundWidth = Math.max(levelWidth, gc.getCanvas().getWidth());
        final double backgroundHeight = Math.max(levelHeight, gc.getCanvas().getHeight());

        gc.setFill(getSkyColor(levelNumber));
        gc.fillRect(ORIGIN, ORIGIN, backgroundWidth, backgroundHeight);
        drawSun(gc, levelNumber);
        drawClouds(gc, backgroundWidth);
        drawHills(gc, backgroundWidth, backgroundHeight);
        drawGroundShadow(gc, backgroundWidth, backgroundHeight);
    }

    private Color getSkyColor(final int levelNumber) {
        switch (levelNumber) {
            case LEVEL_TWO:
                return Color.rgb(LEVEL_TWO_SKY_RED, LEVEL_TWO_SKY_GREEN, LEVEL_TWO_SKY_BLUE);
            case LEVEL_THREE:
                return Color.rgb(LEVEL_THREE_SKY_RED, LEVEL_THREE_SKY_GREEN, LEVEL_THREE_SKY_BLUE);
            default:
                return Color.rgb(LEVEL_ONE_SKY_RED, LEVEL_ONE_SKY_GREEN, LEVEL_ONE_SKY_BLUE);
        }
    }

    private void drawSun(final GraphicsContext gc, final int levelNumber) {
        final double sunX = SUN_BASE_X + levelNumber * SUN_LEVEL_X_STEP;
        final double sunY = SUN_BASE_Y + levelNumber * SUN_LEVEL_Y_STEP;
        gc.setFill(Color.rgb(SUN_RED, SUN_GREEN, SUN_BLUE, SUN_ALPHA));
        gc.fillOval(sunX, sunY, SUN_SIZE, SUN_SIZE);
        gc.setFill(Color.rgb(SUN_INNER_RED, SUN_INNER_GREEN, SUN_INNER_BLUE, SUN_INNER_ALPHA));
        gc.fillOval(
            sunX + SUN_INNER_OFFSET,
            sunY + SUN_INNER_OFFSET,
            SUN_INNER_SIZE,
            SUN_INNER_SIZE
        );
    }

    private void drawClouds(final GraphicsContext gc, final double backgroundWidth) {
        int cloudIndex = INITIAL_INDEX;
        for (double x = CLOUD_START_X; x < backgroundWidth; x += CLOUD_SPACING) {
            final double y = CLOUD_BASE_Y + cloudIndex % CLOUD_VARIATIONS * CLOUD_VARIATION_STEP;
            gc.setFill(Color.rgb(CLOUD_RED, CLOUD_GREEN, CLOUD_BLUE, CLOUD_ALPHA));
            gc.fillOval(x, y, CLOUD_LEFT_WIDTH, CLOUD_LEFT_HEIGHT);
            gc.fillOval(
                x + CLOUD_MIDDLE_X_OFFSET,
                y - CLOUD_MIDDLE_Y_OFFSET,
                CLOUD_MIDDLE_WIDTH,
                CLOUD_MIDDLE_HEIGHT
            );
            gc.fillOval(x + CLOUD_RIGHT_X_OFFSET, y, CLOUD_RIGHT_WIDTH, CLOUD_RIGHT_HEIGHT);
            cloudIndex++;
        }
    }

    private void drawHills(
        final GraphicsContext gc,
        final double backgroundWidth,
        final double backgroundHeight
    ) {
        for (double x = HILL_START_X; x < backgroundWidth; x += HILL_SPACING) {
            gc.setFill(Color.rgb(HILL_FRONT_RED, HILL_FRONT_GREEN, HILL_FRONT_BLUE));
            gc.fillOval(x, backgroundHeight - HILL_FRONT_Y_OFFSET, HILL_FRONT_WIDTH, HILL_FRONT_HEIGHT);
            gc.setFill(Color.rgb(HILL_BACK_RED, HILL_BACK_GREEN, HILL_BACK_BLUE));
            gc.fillOval(
                x + HILL_BACK_X_OFFSET,
                backgroundHeight - HILL_BACK_Y_OFFSET,
                HILL_BACK_WIDTH,
                HILL_BACK_HEIGHT
            );
        }
    }

    private void drawGroundShadow(
        final GraphicsContext gc,
        final double backgroundWidth,
        final double backgroundHeight
    ) {
        gc.setFill(Color.rgb(GROUND_SHADOW_RED, GROUND_SHADOW_GREEN, GROUND_SHADOW_BLUE, GROUND_SHADOW_ALPHA));
        gc.fillRect(ORIGIN, backgroundHeight - GROUND_SHADOW_HEIGHT, backgroundWidth, GROUND_SHADOW_HEIGHT);
        gc.setFill(Color.rgb(GROUND_DARK_RED, GROUND_DARK_GREEN, GROUND_DARK_BLUE, GROUND_DARK_ALPHA));
        gc.fillRect(ORIGIN, GROUND_DARK_Y, backgroundWidth, GROUND_DARK_HEIGHT);
    }
}
