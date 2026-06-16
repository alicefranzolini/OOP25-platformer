package it.unibo.platformer.model.physics;

import java.io.IOException;

public class NoSideException extends IOException{
    private final static String msg = "No side found";

    public NoSideException(){
        super(msg);
    }
    
}
