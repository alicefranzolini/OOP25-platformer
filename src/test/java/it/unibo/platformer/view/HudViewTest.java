package it.unibo.platformer.view;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.unibo.platformer.controller.GameManager.GameState;
import it.unibo.platformer.model.score.ScoreSystem;
import javafx.scene.canvas.Canvas;
import org.junit.jupiter.api.Test;

class HudViewTest {

    private static final int FIRST_LEVEL = 1;
    private static final double VIEW_WIDTH = 1280.0;
    private static final double VIEW_HEIGHT = 720.0;

    @Test
    void rendersMenuWithoutErrors() {
        final Canvas canvas = new Canvas(VIEW_WIDTH, VIEW_HEIGHT);
        final HudView hudView = new HudView();

        assertDoesNotThrow(() -> hudView.render(
            canvas.getGraphicsContext2D(),
            new ScoreSystem(),
            FIRST_LEVEL,
            GameState.MENU,
            VIEW_WIDTH,
            VIEW_HEIGHT
        ));
    }
}
