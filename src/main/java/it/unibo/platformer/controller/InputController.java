package it.unibo.platformer.controller;

import java.util.EnumSet;
import java.util.Set;

import it.unibo.platformer.model.entities.players.Player;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Handles keyboard input and exposes game actions to the controller layer.
 */
public final class InputController {

    private static final KeyCode KEY_LEFT = KeyCode.LEFT;
    private static final KeyCode KEY_RIGHT = KeyCode.RIGHT;
    private static final KeyCode KEY_JUMP = KeyCode.SPACE;
    private static final KeyCode KEY_PAUSE = KeyCode.ESCAPE;
    private static final KeyCode KEY_MENU = KeyCode.M;
    private static final KeyCode KEY_RESTART = KeyCode.R;
    private static final KeyCode KEY_LEVEL_ONE = KeyCode.DIGIT1;
    private static final KeyCode KEY_LEVEL_TWO = KeyCode.DIGIT2;
    private static final KeyCode KEY_LEVEL_THREE = KeyCode.DIGIT3;

    private static final Set<KeyCode> CONSUMABLE_KEYS = EnumSet.of(
        KEY_JUMP,
        KEY_PAUSE,
        KEY_MENU,
        KEY_RESTART,
        KEY_LEVEL_ONE,
        KEY_LEVEL_TWO,
        KEY_LEVEL_THREE
    );

    private final Set<KeyCode> keysPressed;
    private final Set<KeyCode> keysToConsume;

    /**
     * Creates an empty keyboard state.
     */
    public InputController() {
        this.keysPressed = EnumSet.noneOf(KeyCode.class);
        this.keysToConsume = EnumSet.noneOf(KeyCode.class);
    }

    /**
     * Connects the keyboard events of the scene to this controller.
     *
     * @param scene the JavaFX scene that receives keyboard events
     */
    public void register(final Scene scene) {
        scene.setOnKeyPressed(event -> pressKey(event.getCode()));
        scene.setOnKeyReleased(event -> releaseKey(event.getCode()));
    }

    /**
     * Records a pressed key and marks it as consumable if it was just pressed.
     *
     * @param key the pressed key
     */
    public void pressKey(final KeyCode key) {
        if (!this.keysPressed.contains(key) && CONSUMABLE_KEYS.contains(key)) {
            this.keysToConsume.add(key);
        }
        this.keysPressed.add(key);
    }

    /**
     * Records that a key has been released.
     *
     * @param key the released key
     */
    public void releaseKey(final KeyCode key) {
        this.keysPressed.remove(key);
    }

    /**
     * Applies movement and jump input to the player.
     *
     * @param player the player controlled by keyboard input
     */
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

    /**
     * Checks whether the jump key is currently pressed.
     *
     * @return true if jump is pressed
     */
    public boolean isJumpPressed() {
        return this.keysPressed.contains(KEY_JUMP);
    }

    /**
     * Consumes a pending jump action.
     *
     * @return true if jump was pressed since the previous consumption
     */
    public boolean consumeJumpPressed() {
        return this.keysToConsume.remove(KEY_JUMP);
    }

    /**
     * Checks whether the left key is currently pressed.
     *
     * @return true if left is pressed
     */
    public boolean isLeftPressed() {
        return this.keysPressed.contains(KEY_LEFT);
    }

    /**
     * Checks whether the right key is currently pressed.
     *
     * @return true if right is pressed
     */
    public boolean isRightPressed() {
        return this.keysPressed.contains(KEY_RIGHT);
    }

    /**
     * Consumes a pending pause action.
     *
     * @return true if pause was pressed since the previous consumption
     */
    public boolean consumePausePressed() {
        return this.keysToConsume.remove(KEY_PAUSE);
    }

    /**
     * Consumes a pending menu action.
     *
     * @return true if menu was pressed since the previous consumption
     */
    public boolean consumeMenuPressed() {
        return this.keysToConsume.remove(KEY_MENU);
    }

    /**
     * Consumes a pending restart action.
     *
     * @return true if restart was pressed since the previous consumption
     */
    public boolean consumeRestartPressed() {
        return this.keysToConsume.remove(KEY_RESTART);
    }

    /**
     * Consumes a pending level selection.
     *
     * @return the selected level number, or zero when no level was selected
     */
    public int consumeSelectedLevel() {
        int selectedLevel = 0;
        if (this.keysToConsume.remove(KEY_LEVEL_ONE)) {
            selectedLevel = 1;
        }
        if (this.keysToConsume.remove(KEY_LEVEL_TWO) && selectedLevel == 0) {
            selectedLevel = 2;
        }
        if (this.keysToConsume.remove(KEY_LEVEL_THREE) && selectedLevel == 0) {
            selectedLevel = 3;
        }
        return selectedLevel;
    }
}
