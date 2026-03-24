package it.unibo.platformer;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import it.unibo.platformer.model.physics.*;
// -------------------------
// TEST VECTOR
// -------------------------
public class TestPhysics {
    @Test
    public void testVectorAdd() {
        Vector v = new Vector(1, 2);
        v.add(new Vector(3, 4));
        assertEquals(4, v.getX());
        assertEquals(6, v.getY());
    }

    @Test
    public void testVectorSetters() {
        Vector v = new Vector(0, 0);
        v.setX(5);
        v.setY(7);
        assertEquals(5, v.getX());
        assertEquals(7, v.getY());
    }

    // -------------------------
    // TEST GAMEOBJECT
    // -------------------------
    @Test
    public void testGameObjectPosition() {
        GameObject obj = new GameObject(10, 20, 30, 40);
        assertEquals(10, obj.getPosition().getX());
        assertEquals(20, obj.getPosition().getY());
        assertEquals(30, obj.getWidth());
        assertEquals(40, obj.getHeight());
    }

    @Test
    public void testGameObjectSpeed() {
        GameObject obj = new GameObject(0, 0, 10, 10);
        obj.setSpeed(3, 4);
        assertEquals(3, obj.getSpeed().getX());
        assertEquals(4, obj.getSpeed().getY());
    }

    // -------------------------
    // TEST BASICPHYSICS
    // -------------------------
    @Test
    public void testPhysicsGravity() {
        BasicPhysics physics = new BasicPhysics();
        GameObject obj = new GameObject(0, 0, 10, 10);

        float initialSpeedY = obj.getSpeed().getY();
        physics.update(obj);

        assertTrue(obj.getSpeed().getY() > initialSpeedY,
            "La gravità deve aumentare la velocità verticale");
    }

    @Test
    public void testPhysicsPositionUpdate() {
        BasicPhysics physics = new BasicPhysics();
        GameObject obj = new GameObject(0, 0, 10, 10);

        physics.update(obj);

        assertNotEquals(0, obj.getPosition().getY(),
            "La posizione deve cambiare dopo l'update");
    }

    // -------------------------
    // TEST COLLISIONDETECTOR
    // -------------------------
    @Test
    public void testCollisionTrue() {
        GameObject a = new GameObject(0, 0, 50, 50);
        GameObject b = new GameObject(25, 25, 50, 50);

        CollisionDetector cd = new CollisionDetector();

        assertTrue(cd.collision(a, b));
    }

    @Test
    public void testCollisionFalse() {
        GameObject a = new GameObject(0, 0, 50, 50);
        GameObject b = new GameObject(200, 200, 50, 50);

        CollisionDetector cd = new CollisionDetector();

        assertFalse(cd.collision(a, b));
    }

    @Test
    public void testCollisionTouchingEdges() {
        GameObject a = new GameObject(0, 0, 50, 50);
        GameObject b = new GameObject(50, 0, 50, 50);

        CollisionDetector cd = new CollisionDetector();

        assertFalse(cd.collision(a, b),
            "I bordi che si toccano NON sono collisione");
    }
}
