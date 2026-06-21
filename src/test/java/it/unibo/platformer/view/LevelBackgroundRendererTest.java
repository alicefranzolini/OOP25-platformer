package it.unibo.platformer.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.Test;

class LevelBackgroundRendererTest {

    private static final int FIRST_LEVEL = 1;
    private static final double VIEW_WIDTH = 1280.0;
    private static final double VIEW_HEIGHT = 720.0;
    private static final double LEVEL_WIDTH = 3800.0;

    @Test
    void rendersBackgroundWithoutErrors() {
        final Canvas canvas = new Canvas(VIEW_WIDTH, VIEW_HEIGHT);
        final LevelBackgroundRenderer renderer = new LevelBackgroundRenderer();

        assertDoesNotThrow(() -> renderer.render(
            canvas.getGraphicsContext2D(),
            FIRST_LEVEL,
            LEVEL_WIDTH,
            VIEW_HEIGHT
        ));
    }
}
