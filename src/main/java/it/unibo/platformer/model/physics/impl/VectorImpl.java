package it.unibo.platformer.model.physics.impl;

import it.unibo.platformer.model.physics.api.Vector;

public class VectorImpl implements Vector{
    
    private float x;
    private float y;

    /*Builder*/
    public VectorImpl(){};

    public VectorImpl(float x, float y){
        this.x = x;
        this.y = y;
    }

    /*Setter*/
    @Override
    public void setX(float x){
        this.x = x;
    }
    
    @Override
    public void setY(float y){
        this.y = y;
    }

    /*Getter*/
    @Override
    public float getX(){
        return this.x;
    }

    @Override
    public float getY(){
        return this.y;
    }

    /*Methods*/
    @Override
    public void add(VectorImpl v){
        this.x += v.getX();
        this.y += v.getY();
    }

    @Override
    public void sub(VectorImpl v){
        this.x -= v.getX();
        this.y -= v.getY();
    }

    @Override
    public void scale(float num){   /*multiplication for a number*/
        this.x = this.x * num;
        this.y = this.y * num;
    }

    public VectorImpl clone(){
        return new VectorImpl(this.x, this.y);
    }

    @Override
    public String toString(){
        return "Coord. X = "+this.x+" - Coord. Y = "+this.y;
    }

}
