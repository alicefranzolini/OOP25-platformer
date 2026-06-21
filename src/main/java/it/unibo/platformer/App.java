package it.unibo.platformer;

import javafx.application.Application;

/**
 * Starts the JavaFX application from a regular Java main class.
 */
public final class App {

    private App() {
    }

    /**
     * Launches the platformer application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        Application.launch(Main.class, args);
    }
}
