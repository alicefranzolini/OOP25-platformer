package it.unibo.platformer;

import it.unibo.platformer.controller.GameManager;
import java.net.URL;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Creates the JavaFX window and runs the main game loop.
 */
public final class Main extends Application {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final long INITIAL_TIME = 0L;
    private static final double NANOSECONDS_PER_SECOND = 1_000_000_000.0;
    private static final double BACKGROUND_MUSIC_VOLUME = 0.35;
    private static final String BACKGROUND_MUSIC_PATH = "/audio/background.mp3";

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
        final MediaPlayer backgroundMusic = createBackgroundMusic();

        stage.setTitle("Platformer");
        stage.setScene(scene);
        stage.setOnShown(event -> canvas.requestFocus());
        stage.setOnCloseRequest(event -> backgroundMusic.dispose());
        stage.show();
        backgroundMusic.play();

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

    private MediaPlayer createBackgroundMusic() {
        final URL musicResource = Main.class.getResource(BACKGROUND_MUSIC_PATH);
        if (musicResource == null) {
            throw new IllegalStateException("Missing background music resource: " + BACKGROUND_MUSIC_PATH);
        }

        final Media media = new Media(musicResource.toExternalForm());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(BACKGROUND_MUSIC_VOLUME);
        return mediaPlayer;
    }
}
