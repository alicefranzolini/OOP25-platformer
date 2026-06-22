package it.unibo.platformer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;

class InputControllerTest {

    @Test
    void movementKeysStayPressedUntilReleased() {
        final InputController inputController = new InputController();

        inputController.pressKey(KeyCode.RIGHT);
        assertTrue(inputController.isRightPressed());

        inputController.releaseKey(KeyCode.RIGHT);
        assertFalse(inputController.isRightPressed());
    }

    @Test
    void pauseCommandIsConsumedOnlyOnce() {
        final InputController inputController = new InputController();

        inputController.pressKey(KeyCode.ESCAPE);

        assertTrue(inputController.consumePausePressed());
        assertFalse(inputController.consumePausePressed());
    }

    @Test
    void menuCommandIsConsumedOnlyOnce() {
        final InputController inputController = new InputController();

        inputController.pressKey(KeyCode.M);

        assertTrue(inputController.consumeMenuPressed());
        assertFalse(inputController.consumeMenuPressed());
    }

    @Test
    void jumpCommandIsConsumedOnlyOnce() {
        final InputController inputController = new InputController();

        inputController.pressKey(KeyCode.SPACE);

        assertTrue(inputController.consumeJumpPressed());
        assertFalse(inputController.consumeJumpPressed());
        assertTrue(inputController.isJumpPressed());
    }

    @Test
    void restartCommandIsAvailableAfterNewPress() {
        final InputController inputController = new InputController();

        inputController.pressKey(KeyCode.R);
        assertTrue(inputController.consumeRestartPressed());

        inputController.releaseKey(KeyCode.R);
        inputController.pressKey(KeyCode.R);
        assertTrue(inputController.consumeRestartPressed());
    }

    @Test
    void levelSelectionReturnsSelectedLevelOnlyOnce() {
        final InputController inputController = new InputController();

        inputController.pressKey(KeyCode.DIGIT2);

        assertEquals(2, inputController.consumeSelectedLevel());
        assertEquals(0, inputController.consumeSelectedLevel());
    }

    @Test
    void consumingLevelSelectionClearsOtherLevelKeys() {
        final InputController inputController = new InputController();

        inputController.pressKey(KeyCode.DIGIT1);
        inputController.pressKey(KeyCode.DIGIT3);

        assertEquals(1, inputController.consumeSelectedLevel());
        assertEquals(0, inputController.consumeSelectedLevel());
    }
}
