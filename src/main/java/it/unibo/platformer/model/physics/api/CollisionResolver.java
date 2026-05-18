package it.unibo.platformer.model.physics.api;

import java.util.List;

import it.unibo.platformer.model.physics.NoSideException;
import it.unibo.platformer.model.physics.impl.CollisionResult;

public interface CollisionResolver {

    public void ResolveAll(List<CollisionResult> collisions) throws NoSideException;

    public void ResolveOne(CollisionResult res) throws NoSideException;
    
}
