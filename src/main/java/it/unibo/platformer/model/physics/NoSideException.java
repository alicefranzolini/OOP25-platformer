package it.unibo.platformer.model.physics;

import java.io.IOException;

/**
 * This is the exception if there isn't sides.
 */
public class NoSideException extends IOException {
    private static final String MSG = "No side found";

    /**
     * The exception builder.
     */
    public NoSideException() {
        super(MSG);
    }

}
