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
 * JavaFX application entry point for the platformer.
 */
public final class App extends Application {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    /**
     * Creates the window, canvas, input controller, and game loop.
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
                if (this.lastUpdate == 0) {
                    this.lastUpdate = now;
                    return;
                }

                final double deltaTime = (now - this.lastUpdate) / 1_000_000_000.0;
                this.lastUpdate = now;

                gameManager.setViewSize(canvas.getWidth(), canvas.getHeight());
                gameManager.update(deltaTime);
                gameManager.render(gc);
            }
        }.start();
    }

    /**
     * Starts the JavaFX application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
