package it.unibo.platformer.model.physics;

public class GameObject {
    private Vector position;
    private Vector speed;
    private float width;
    private float height;

    public GameObject(float x, float y, float width, float height){
        this.position = new Vector(x, y);
        this.speed = new Vector(0, 0);
        this.width = width;
        this.height = height;
    }

    public void setPosition(float x, float y){
        this.position.setX(x);
        this.position.setY(y);
    }

    public void setSpeed(float x, float y){
        this.speed.setX(x);
        this.speed.setY(y);
    }

    public float getWidth(){
        return this.width;
    }

    public float getHeight(){
        return this.height;
    }

    public Vector getPosition(){
        return this.position;
    }

    public Vector getSpeed(){
        return this.speed;
    }

    @Override
    public String toString(){
        return "Position: "+this.position+"- Speed: "+this.speed+"- Width: "+this.width+"- Height: "+this.height;
    }

}
