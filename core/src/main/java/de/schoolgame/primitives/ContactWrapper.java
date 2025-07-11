package de.schoolgame.primitives;

public class ContactWrapper {
    public Direction d;
    public Vec2f cp;
    public float t;
    
    public ContactWrapper(){
        this.d = Direction.NONE;
        this.cp = Vec2f.ZERO;
        this.t = 0.0f;
    }
    public ContactWrapper(Direction d,Vec2f cp,float t){
        this.d = d;
        this.cp = cp;
        this.t = t;
    }
}
