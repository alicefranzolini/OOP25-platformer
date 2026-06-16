package it.unibo.platformer.model.physics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import it.unibo.platformer.model.physics.impl.CollisionDetectorImpl;
import it.unibo.platformer.model.physics.impl.CollisionResult;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;
import it.unibo.platformer.model.physics.impl.VectorImpl;
import it.unibo.platformer.model.physics.impl.CollisionSide;

/**
 * The test for physics.
 */
public class TestPhysics {

    private static final double VAL_10 = 10.0;
    private static final double VAL_20 = 20.0;
    private static final double VAL_30 = 30.0;
    private static final double VAL_40 = 40.0;
    private static final double VAL_50 = 50.0;
    private static final double VAL_10_NEG = -10.0;


    /**
     * The test for Vector creation.
     */
    @Test
    public void testGetSetVector() {
        final VectorImpl v1 = new VectorImpl();
        v1.setX((int) VAL_10);
        v1.setY((int) VAL_20);
        assertEquals(VAL_10, v1.getX());
        assertEquals(VAL_20, v1.getY());
    }

    /**
     * Test for the addition of vectors.
     */
    @Test
    public void testVectorAdd() {
        final VectorImpl v1 = new VectorImpl((int) VAL_10, (int) VAL_20);
        final VectorImpl v2 = new VectorImpl((int) VAL_20, (int) VAL_30);
        v1.add(v2);
        assertEquals(VAL_30, v1.getX());
        assertEquals(VAL_50, v1.getY());
    }

    /**
     * Test for the subtraction of vectors.
     */
    @Test
    public void testVectorSub() {
        final VectorImpl v1 = new VectorImpl((int) VAL_10, (int) VAL_20);
        final VectorImpl v2 = new VectorImpl((int) VAL_20, (int) VAL_30);
        v1.sub(v2);
        assertEquals(VAL_10_NEG, v1.getX());
        assertEquals(VAL_10_NEG, v1.getY());
    }

    /**
     * Test for the multiplication of vectors.
     */
    @Test
    public void testScale() {
        final VectorImpl v1 = new VectorImpl((int) VAL_10, (int) VAL_20);
        v1.scale(2);
        assertEquals(VAL_20, v1.getX());
        assertEquals(VAL_40, v1.getY());
    }

    /**
     * Test for the clonation of vectors.
     */
    @Test
    public void testClone() {
        final VectorImpl v1 = new VectorImpl((int) VAL_10, (int) VAL_20);
        final VectorImpl v2 = v1.clone();

        assertEquals(v1.getClass(), v2.getClass());
        assertEquals(v1.getX(), v2.getX());
        assertEquals(v1.getY(), v2.getY());
    }

    /**
     * Test for GameObject.
     */
    @Test
    public void testGameObject() {
        final GameObjectImpl o = new GameObjectImpl((int) VAL_10, (int) VAL_20, (int) VAL_30, (int) VAL_40);
        final VectorImpl pos = new VectorImpl((int) VAL_10, (int) VAL_20);
        final VectorImpl speed = new VectorImpl((int) VAL_10, (int) VAL_10);
        assertEquals(pos.getX(), o.getPosition().getX());
        assertEquals(pos.getY(), o.getPosition().getY());
        assertEquals(false, o.isOnGround());
        o.setSpeed((int) VAL_10, (int) VAL_10);
        assertEquals(speed.getX(), o.getSpeed().getX());
        assertEquals(speed.getY(), o.getSpeed().getY());
        assertEquals(VAL_30, o.getWidth());
        assertEquals(VAL_40, o.getHeight());
        o.setHeight((int) VAL_10);
        assertEquals(VAL_10, o.getHeight());
        o.setWidth((int) VAL_10);
        assertEquals(VAL_10, o.getWidth());
        o.setOnGround(true);
        assertEquals(true, o.isOnGround());
    }

    /**
     * Test for collision result.
     */
    @Test
    public void testCollisionResult() {
        final GameObjectImpl dinamicobj = new GameObjectImpl((int) VAL_10, (int) VAL_20, (int) VAL_10, (int) VAL_20);
        final GameObjectImpl staticobj = new GameObjectImpl((int) VAL_20, (int) VAL_30, (int) VAL_20, (int) VAL_30);
        final CollisionSide side = CollisionSide.TOP;

        final CollisionResult collision = new CollisionResult(dinamicobj, staticobj, side);

        assertEquals(side, collision.getSide());
        assertEquals(dinamicobj, collision.getDynamicObj());
        assertEquals(staticobj, collision.getStaticObj());
    }

    /**
     * Test for collision.
     */
    @Test
    public void testCollisionDetector() {
        final GameObjectImpl o1 = new GameObjectImpl((int) VAL_10, (int) VAL_20, (int) VAL_30, (int) VAL_40);
        final GameObjectImpl o2 = new GameObjectImpl((int) VAL_20, (int) VAL_30, (int) VAL_40, (int) VAL_50);

        final CollisionDetectorImpl collDet = new CollisionDetectorImpl();

        assertEquals(true, collDet.collision(o1, o2));

        assertNotNull(collDet.getCollisionResult(o1, o2));
        final CollisionResult res = new CollisionResult(o1, o2, CollisionSide.LEFT);
        assertEquals(res.getSide(), collDet.getCollisionResult(o1, o2).getSide());

    }

}
