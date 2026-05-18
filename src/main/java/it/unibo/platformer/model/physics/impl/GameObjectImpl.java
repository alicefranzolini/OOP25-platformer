package it.unibo.platformer.model.physics.impl;

import it.unibo.platformer.model.physics.api.GameObject;

public class GameObjectImpl implements GameObject{
    private VectorImpl position;
    private VectorImpl speed;
    private float width;
    private float height;

    public GameObjectImpl(float x, float y, float width, float height){
        this.position = new VectorImpl(x, y);
        this.speed = new VectorImpl(0, 0);
        this.width = width;
        this.height = height;
    }

    @Override
    public void setPosition(float x, float y){
        this.position.setX(x);
        this.position.setY(y);
    }

    @Override
    public void setSpeed(float x, float y){
        this.speed.setX(x);
        this.speed.setY(y);
    }

    @Override
    public float getWidth(){
        return this.width;
    }

    @Override
    public float getHeight(){
        return this.height;
    }

    @Override
    public VectorImpl getPosition(){
        return this.position;
    }

    @Override
    public VectorImpl getSpeed(){
        return this.speed;
    }

    @Override
    public String toString(){
        return "Position: "+this.position+"- Speed: "+this.speed+"- Width: "+this.width+"- Height: "+this.height;
    }

}
