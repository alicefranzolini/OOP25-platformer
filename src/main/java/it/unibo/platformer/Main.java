package it.unibo.platformer;

import it.unibo.platformer.controller.GameManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Creates the JavaFX window and runs the main game loop.
 */
public final class Main extends Application {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final long INITIAL_TIME = 0L;
    private static final double NANOSECONDS_PER_SECOND = 1_000_000_000.0;

    /**
     * Creates the JavaFX application.
     */
    public Main() {
        super();
    }

    /**
     * Creates the window, canvas, input controller and game loop.
     *
     * @param stage the main JavaFX stage
     */
    @Override
    public void start(final Stage stage) {
        final Canvas canvas = new Canvas(WIDTH, HEIGHT);
        canvas.setFocusTraversable(true);
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        final StackPane root = new StackPane(canvas);
        final Scene scene = new Scene(root, WIDTH, HEIGHT);
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        final GameManager gameManager = new GameManager(WIDTH, HEIGHT);
        gameManager.getInputController().register(scene);

        stage.setTitle("Platformer");
        stage.setScene(scene);
        stage.setOnShown(event -> canvas.requestFocus());
        stage.show();

        new AnimationTimer() {
            private long lastUpdate;

            @Override
            public void handle(final long now) {
                if (this.lastUpdate == INITIAL_TIME) {
                    this.lastUpdate = now;
                    return;
                }

                final double deltaTime = (now - this.lastUpdate) / NANOSECONDS_PER_SECOND;
                this.lastUpdate = now;

                gameManager.setViewSize(canvas.getWidth(), canvas.getHeight());
                gameManager.update(deltaTime);
                gameManager.render(gc);
            }
        }.start();
    }
}
