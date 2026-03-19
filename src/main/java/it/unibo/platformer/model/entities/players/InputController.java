package it.unibo.platformer.model.entities.players;

import javafx.scene.Scene;  // la Scene è la scena JavaFX del gioco, registra gli eventi della tastiera
import javafx.scene.input.KeyCode;  // KeyCode rappresenta un tasto della tastiera.
import java.util.HashSet;
import java.util.Set; // SET: per non permettere duplicati , veloce da controllare

/**
 * 
 * Gestisce gli input da tastiera e li traduce in azioni del player.
 *
 */

// clsse per la gestione degli input 
public class InputController {

    private final Set<KeyCode> keysPressed = new HashSet<>();

    // Tasti di controllo
    private static final KeyCode KEY_LEFT  = KeyCode.LEFT;
    private static final KeyCode KEY_RIGHT = KeyCode.RIGHT;
    private static final KeyCode KEY_JUMP  = KeyCode.SPACE;
    private static final KeyCode KEY_RUN   = KeyCode.SHIFT;

    /**
     * 
     * Registra gli event listener sulla scena JavaFX.
     * serve per collegare la tastiera alla scena JavaFX
     * 
     * Da chiamare una volta sola all'avvio.
     */
    public void register(Scene scene) {
        scene.setOnKeyPressed(e  -> keysPressed.add(e.getCode()));
        /**
         *  quando viene premuto un tasto JavaFX genera un evento , e.getCode() prende il codice del tasto digitato, il tasto viene aggiunto al Set
         */
        scene.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));
        /**
         *  quando un tasto viene rilasciato , viene rimosso dal Set 
         */
    }

    /**
     * Applica gli input al player.
     * Da chiamare ogni frame prima di update().
     */
    public void handleInput(Player player) {
        boolean left  = keysPressed.contains(KEY_LEFT);
        boolean right = keysPressed.contains(KEY_RIGHT);

        if (left && !right)       player.moveLeft();
        else if (right && !left)  player.moveRight();
        else                      player.stopX();

        // Il salto viene gestito da Player nella settimana 2 (Sabrina)
        // Qui esponiamo solo il metodo per sapere se il tasto è premuto
    }

    // valuto con delle booleane le varie opzioni

    public boolean isJumpPressed() { return keysPressed.contains(KEY_JUMP); }
    public boolean isRunPressed()  { return keysPressed.contains(KEY_RUN);  }
    public boolean isLeftPressed() { return keysPressed.contains(KEY_LEFT); }
    public boolean isRightPressed(){ return keysPressed.contains(KEY_RIGHT);}
}
