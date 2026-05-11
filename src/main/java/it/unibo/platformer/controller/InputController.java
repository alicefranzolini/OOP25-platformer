package it.unibo.platformer.controller;

import javafx.scene.Scene;  // The Scene is the JavaFX scene of the game; it registers keyboard events
import javafx.scene.input.KeyCode;  // KeyCode represents a keyboard key
import java.util.HashSet;
import java.util.Set; // SET: prevents duplicates, fast to check

import it.unibo.platformer.model.entities.players.Player;

/**
 * Handles keyboard input and translates it into player actions.
 */

// Class for input handling
public class InputController {

    private final Set<KeyCode> keysPressed = new HashSet<>();

    // Control keys
    private static final KeyCode KEY_LEFT  = KeyCode.LEFT;
    private static final KeyCode KEY_RIGHT = KeyCode.RIGHT;
    private static final KeyCode KEY_JUMP  = KeyCode.SPACE;
    private static final KeyCode KEY_RUN   = KeyCode.SHIFT;

    /**
     * Registers event listeners on the JavaFX Scene.
     * This connects the keyboard to the JavaFX scene.
     *
     * Should be called once at startup.
     */
    public void register(Scene scene) {
        scene.setOnKeyPressed(e  -> keysPressed.add(e.getCode()));
        // When a key is pressed, JavaFX generates an event; e.getCode() retrieves the key code and adds it to the Set

        scene.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));
        // When a key is released, it is removed from the Set
    }

    /**
     * Applies input to the player.
     * Should be called every frame before update().
     */
    public void handleInput(Player player) {
        boolean left  = keysPressed.contains(KEY_LEFT);
        boolean right = keysPressed.contains(KEY_RIGHT);

        if (left && !right)       player.moveLeft();
        else if (right && !left)  player.moveRight();
        else                      player.stopX();

        // Jump is handled inside Player (week 2)
        // Here we only expose methods to check if a key is pressed
    }

    // Boolean checks for specific keys

    public boolean isJumpPressed() { 
        return keysPressed.contains(KEY_JUMP); 
    }
    public boolean isRunPressed()  { 
        return keysPressed.contains(KEY_RUN);  
    }
    public boolean isLeftPressed() { 
        return keysPressed.contains(KEY_LEFT); 
    }
    public boolean isRightPressed(){ 
        return keysPressed.contains(KEY_RIGHT);
    }
}
