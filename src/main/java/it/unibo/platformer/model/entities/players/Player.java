package it.unibo.platformer.model.entities.players;
//ci troviamo all'interno della classe player
import it.unibo.platformer.model.entities.DynamicEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/*
*   DynamicEntity : eredito la classe base
*   GraphicContext : disegna personaggio
*   Color: colori del disegno
*
 */
/**
 * Personaggio principale controllato dal giocatore.
 * Gestisce movimento orizzontale e sprite switching base.
*
*/
public class Player extends DynamicEntity {
// estende la classe DinamicEntity
    private static final double MOVE_SPEED  = 180.0; // pixel/s 
    // velocità orizzontale del player 
    private static final double SMALL_W     = 16.0;     // larghezza del personaggio
    private static final double SMALL_H     = 24.0;     //altezza del personaggio
    
    //static final significa che sono valori fissi, uguali per tutti i player.

    // Direzione in cui guarda il player (per il flip dello sprite)
    private boolean facingRight;
    // true : guarda a destra 
    // false : guarda a sinistra

    //PROVA A SVILUPPARE PIU' DINAMICAMENTE -->
    
    // Sprite switching base (IDLE / WALK / JUMP)
    public enum SpriteState { IDLE, WALK, JUMP }    // enum : insieme di stati posssibili del player
    private SpriteState spriteState;    //memorizza lo stato attuale del personaggio
/* 
*   IDLE: fermo , WALK : cammina , JUMP : salta
*/

    // Animazione camminata
    private double walkAnimTimer;   // accumola il tempo passato
    private int walkFrame;  // indica quale frame dell'animazione stai mostrando
    private static final double WALK_FRAME_DURATION = 0.1;  // ogni 0.1 cambia frame
    private static final int WALK_FRAMES = 3;   /// l'animazione ha 3 frame
    // quando il player cammina, i frame cambiano ciclicamente

   // COSTRUTTORE : chiamato quando crei un player

    public Player(double x, double y) {
        super(x, y, SMALL_W, SMALL_H);  // (posizione iniziale x, posizione iniziale y, larghezza 16, altezza 24)
        this.facingRight = true;    // all'inizio il player guarda a destra
        this.spriteState = SpriteState.IDLE;    //stato iniziale: fermo
        this.walkAnimTimer = 0;
        this.walkFrame = 0;  // timer e frame inizale
    }

    /*
    *   metodo update : chiamato ogni frame del gioco
    */
    @Override
    public void update(double deltaTime) {
        // Il movimento viene applicato da InputController,
        // qui aggiorniamo solo animazioni e sprite state.
        updateSpriteState();    //controlla lo stato del personaggio (fermo, in camminata, in salto)
        updateAnimation(deltaTime); //se cammina cmabia il frame

        // Aggiorna posizione tramite velocità (eredita da DynamicEntity)
        super.update(deltaTime); 
    }

        /*
        * meteodo updateSpriteState : decide in che stato si t5rova il personaggio
        * */
    private void updateSpriteState() {
        if (!onGround) {
            spriteState = SpriteState.JUMP;
            // controlla se il personaggio si trova sul pavimento: se è false o salta  o sta cadento : stato JUMP

        } else if (Math.abs(velocityX) > 1.0) {
            spriteState = SpriteState.WALK;
            // controlla la velocità, se è diversa da zero (controlla con valore assoluto, non importa direzione)
            if (velocityX > 0) facingRight = true;
            else facingRight = false;
/*
*   velocità positiva: guarda a destra
*   velocità negativa: guarda a sinistra
*/
        } else {
            spriteState = SpriteState.IDLE;
            //se non è in aria e non si sta  muovendo, è fermo
        }
    }
/*
* 
*   Metodo updateAnimation: aggirona i frame dell'animazione
*
*/
    private void updateAnimation(double deltaTime) {
        if (spriteState == SpriteState.WALK) { 
            walkAnimTimer += deltaTime;     // se il player cammina , aggiugo il tempo trascorso
            if (walkAnimTimer >= WALK_FRAME_DURATION) {
                walkAnimTimer = 0;
                walkFrame = (walkFrame + 1) % WALK_FRAMES;
            }
            // cambio frame ogni 0.1 sec
        } else {
            walkFrame = 0;
            walkAnimTimer = 0;
        } 
        //se il player è fermo o salto : resetto il frame a 0 e resetto il timer, l'animazione cammina non continua
    }
/*
* 
*   Metodo render: serve per disegnare il player sullo schermo
*
*/
    @Override
    public void render(GraphicsContext gc) {
        // Placeholder — sarà sostituito con sprite pixel-art
        gc.setFill(Color.RED);
        gc.fillRect(x, y, width, height);

        // Cappello
        gc.setFill(Color.DARKRED);
        gc.fillRect(x + 2, y - 6, width - 4, 8);

        // Debug: mostra stato
        gc.setFill(Color.WHITE);
        gc.fillText(spriteState.name(), x, y - 10);
    }

    /**
     * Muove il player orizzontalmente.
     * Chiamato da InputController.
     */
    public void moveLeft()  { velocityX = -MOVE_SPEED; }    //muove il player verso sinistra
    public void moveRight() { velocityX =  MOVE_SPEED; }    //muove il player verso destra
    public void stopX()     { velocityX = 0; }              // ferma movimento orizzontale

    public boolean isFacingRight()   { return facingRight; }    // dice se il player guarda a destra
    public SpriteState getSpriteState() { return spriteState; }     // restituisce lo stato attuale dello sprite
}
