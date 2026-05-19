package it.unibo.platformer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import it.unibo.platformer.model.physics.impl.BasicPhysicsImpl;
import it.unibo.platformer.model.physics.impl.CollisionDetectorImpl;
import it.unibo.platformer.model.physics.impl.CollisionResult;
import it.unibo.platformer.model.physics.impl.GameObjectImpl;
import it.unibo.platformer.model.physics.impl.VectorImpl;
import it.unibo.platformer.model.physics.impl.CollisionSide;

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

    @Test
    public void TestVectorSub(){
        VectorImpl v1 = new VectorImpl(10, 20);
        VectorImpl v2 = new VectorImpl(20, 30);
        v1.sub(v2);
        assertEquals(-10, v1.getX());
        assertEquals(-10, v1.getY());
    }

    @Test
    public void TestScale(){
        VectorImpl v1 = new VectorImpl(10, 20);
        v1.scale(2);
        assertEquals(20, v1.getX());
        assertEquals(40, v1.getY());
    }

    @Test
    public void TestClone(){
        VectorImpl v1 = new VectorImpl(10,20);
        VectorImpl v2 = v1.clone();

        assertEquals(v1.getClass(), v2.getClass());
        assertEquals(v1.getX(), v2.getX());
        assertEquals(v1.getY(), v2.getY());
    }

    @Test
    public void TestGameObject(){
        GameObjectImpl o = new GameObjectImpl(10, 20, 30, 40);
        VectorImpl pos = new VectorImpl(10, 20);
        VectorImpl speed = new VectorImpl(10,10);
        assertEquals(pos.getX(), o.getPosition().getX());
        assertEquals(pos.getY(), o.getPosition().getY());
        assertEquals(false, o.IsOnGround());
        o.setSpeed(10, 10);
        assertEquals(speed.getX(), o.getSpeed().getX());
        assertEquals(speed.getY(), o.getSpeed().getY());
        assertEquals(30, o.getWidth());
        assertEquals(40, o.getHeight());
        o.SetOnGround(true);
        assertEquals(true, o.IsOnGround());
    }

    @Test
    public void TestCollisionResult(){
        GameObjectImpl dinamicobj = new GameObjectImpl(10, 20, 10, 20);
        GameObjectImpl staticobj = new GameObjectImpl(20, 30, 20, 30);
        CollisionSide side = CollisionSide.TOP;

        CollisionResult collision = new CollisionResult(dinamicobj, staticobj, side);

        assertEquals(side, collision.getSide());
        assertEquals(dinamicobj, collision.getDynamicObj());
        assertEquals(staticobj, collision.getStaticObj());
    }
    
}
