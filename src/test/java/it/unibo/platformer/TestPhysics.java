package it.unibo.platformer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.unibo.platformer.model.physics.api.BasicPhysics;
import it.unibo.platformer.model.physics.api.CollisionDetector;
import it.unibo.platformer.model.physics.api.GameObject;
import it.unibo.platformer.model.physics.api.Vector;
import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import it.unibo.platformer.model.physics.impl.CollisionDetectorImpl;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;
import it.unibo.platformer.model.physics.impl.VectorImpl;

public class TestPhysics {
    
    @Test
    public void TestGetSetVector(){
        VectorImpl v1 = new VectorImpl();
        v1.setX(10);
        v1.setY(20);
        assertEquals(10, v1.getX());
        assertEquals(20, v1.getY());
    }

    @Test
    public void TestVectorAdd(){
        VectorImpl v1 = new VectorImpl(10, 20);
        VectorImpl v2 = new VectorImpl(10, 30);
        v1.add(v2);
        assertEquals(20, v1.getX());
        assertEquals(50, v1.getY());
    }
    
}
