package it.unibo.platformer.model.entities.players;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class InputController {

    private static final KeyCode KEY_LEFT = KeyCode.LEFT;
    private static final KeyCode KEY_RIGHT = KeyCode.RIGHT;
    private static final KeyCode KEY_JUMP = KeyCode.SPACE;
    private static final KeyCode KEY_RUN = KeyCode.SHIFT;
    private static final KeyCode KEY_PAUSE = KeyCode.ESCAPE;
    private static final KeyCode KEY_RESTART = KeyCode.R;
    private static final KeyCode KEY_LEVEL_ONE = KeyCode.DIGIT1;
    private static final KeyCode KEY_LEVEL_TWO = KeyCode.DIGIT2;
    private static final KeyCode KEY_LEVEL_THREE = KeyCode.DIGIT3;

    private final Set<KeyCode> keysPressed = new HashSet<>();
    private final Set<KeyCode> keysToConsume = new HashSet<>();

    // Connects the keyboard events of the scene to this controller.
    public void register(final Scene scene) {
        scene.setOnKeyPressed(event -> pressKey(event.getCode()));
        scene.setOnKeyReleased(event -> releaseKey(event.getCode()));
    }

    public void pressKey(final KeyCode key) {
        // A key is consumed only when it has just been pressed.
        if (!this.keysPressed.contains(key)) {
            this.keysToConsume.add(key);
        }
        this.keysPressed.add(key);
    }

    public void releaseKey(final KeyCode key) {
        this.keysPressed.remove(key);
    }

    // Applies movement and jump input to the player.
    public void handleInput(final Player player) {
        if (player == null) {
            return;
        }

        final boolean left = isLeftPressed();
        final boolean right = isRightPressed();

        if (left && !right) {
            player.moveLeft();
        } else if (right && !left) {
            player.moveRight();
        } else {
            player.stopX();
        }

        player.setJumpRequested(isJumpPressed());
    }

    public boolean isJumpPressed() {
        return this.keysPressed.contains(KEY_JUMP);
    }

    public boolean isRunPressed() {
        return this.keysPressed.contains(KEY_RUN);
    }

    public boolean isLeftPressed() {
        return this.keysPressed.contains(KEY_LEFT);
    }

    public boolean isRightPressed() {
        return this.keysPressed.contains(KEY_RIGHT);
    }

    public boolean consumePausePressed() {
        // Pause is an action, so it must happen once for each key press.
        return this.keysToConsume.remove(KEY_PAUSE);
    }

    public boolean consumeRestartPressed() {
        // Restart is also consumed once to avoid restarting every frame.
        return this.keysToConsume.remove(KEY_RESTART);
    }

    public int consumeSelectedLevel() {
        if (this.keysToConsume.remove(KEY_LEVEL_ONE)) {
            return 1;
        } else if (this.keysToConsume.remove(KEY_LEVEL_TWO)) {
            return 2;
        } else if (this.keysToConsume.remove(KEY_LEVEL_THREE)) {
            return 3;
        }
        return 0;
    }
}
