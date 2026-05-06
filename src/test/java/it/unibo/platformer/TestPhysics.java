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
    public void testVectorAdd() {
        Vector v = new VectorImpl(1, 2);
        v.add(new VectorImpl(3, 4));
        assertEquals(4, v.getX());
        assertEquals(6, v.getY());
    }

    @Test
    public void testVectorSetters() {
        Vector v = new VectorImpl(0, 0);
        v.setX(5);
        v.setY(7);
        assertEquals(5, v.getX());
        assertEquals(7, v.getY());
    }

    @Test
    public void testGameObjectPosition() {
        GameObject obj = new GameObjectImpl(10, 20, 30, 40);
        assertEquals(10, obj.getPosition().getX());
        assertEquals(20, obj.getPosition().getY());
        assertEquals(30, obj.getWidth());
        assertEquals(40, obj.getHeight());
    }

    @Test
    public void testGameObjectSpeed() {
        GameObject obj = new GameObjectImpl(0, 0, 10, 10);
        obj.setSpeed(3, 4);
        assertEquals(3, obj.getSpeed().getX());
        assertEquals(4, obj.getSpeed().getY());
    }

    @Test
    public void testPhysicsGravity() {
        BasicPhysics physics = new BasicPhysicsImpl();
        GameObject obj = new GameObjectImpl(0, 0, 10, 10);

        float initialSpeedY = obj.getSpeed().getY();
        physics.update(obj);

        assertTrue(obj.getSpeed().getY() > initialSpeedY,
            "Vertical speed error");
    }

    @Test
    public void testPhysicsPositionUpdate() {
        BasicPhysics physics = new BasicPhysicsImpl();
        GameObject obj = new GameObjectImpl(0, 0, 10, 10);

        physics.update(obj);

        assertNotEquals(0, obj.getPosition().getY(),
            "The position must change");
    }

    @Test
    public void testCollisionTrue() {
        GameObject a = new GameObjectImpl(0, 0, 50, 50);
        GameObject b = new GameObjectImpl(25, 25, 50, 50);

        CollisionDetector cd = new CollisionDetectorImpl();

        assertTrue(cd.collision(a, b));
    }

    @Test
    public void testCollisionFalse() {
        GameObject a = new GameObjectImpl(0, 0, 50, 50);
        GameObject b = new GameObjectImpl(200, 200, 50, 50);

        CollisionDetector cd = new CollisionDetectorImpl();

        assertFalse(cd.collision(a, b));
    }

    @Test
    public void testCollisionTouchingEdges() {
        GameObject a = new GameObjectImpl(0, 0, 50, 50);
        GameObject b = new GameObjectImpl(50, 0, 50, 50);

        CollisionDetector cd = new CollisionDetectorImpl();

        assertFalse(cd.collision(a, b),
            "The touching borders aren't in collision");
    }
}
