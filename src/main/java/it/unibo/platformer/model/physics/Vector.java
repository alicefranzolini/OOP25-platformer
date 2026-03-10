package it.unibo.platformer.model.physics;

public class Vector {
    
    private float x;
    private float y;

    /*Builder*/
    public Vector(){};

    public Vector(float x, float y){
        this.x = x;
        this.y = y;
    }

    /*Setter*/
    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    /*Getter*/
    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    /*Methods*/
    public void add(Vector v){
        this.x += v.getX();
        this.y += v.getY();
    }

    public void sub(Vector v){
        this.x -= v.getX();
        this.y -= v.getY();
    }

    public void scale(float num){   /*multiplication for a number*/
        this.x = this.x * num;
        this.y = this.y * num;
    }

    public Vector clone(){
        return new Vector(this.x, this.y);
    }

    @Override
    public String toString(){
        return "Coord. X = "+this.x+" - Coord. Y = "+this.y;
    }

}
