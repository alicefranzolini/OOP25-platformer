package it.unibo.platformer;

import it.unibo.platformer.controller.GameManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;


public class Main extends Application {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    @Override
    public void start(final Stage stage) {
        final Canvas canvas = new Canvas(WIDTH, HEIGHT);
        final GraphicsContext gc = canvas.getGraphicsContext2D();

        final Group root = new Group(canvas);
        final Scene scene = new Scene(root);

        final GameManager gameManager = new GameManager();
        gameManager.getInputController().register(scene);

        stage.setTitle("Platformer");
        stage.setScene(scene);
        stage.show();

        gameManager.startGame();

        new AnimationTimer() {
            private long lastUpdate;

            @Override
            public void handle(final long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                final double deltaTime = (now - lastUpdate) / 1_000_000_000.0;
                lastUpdate = now;

                gameManager.update(deltaTime);
                gameManager.render(gc);
            }
        }.start();
    }

    public static void main(final String[] args) {
        launch(args);
    }
    
}
