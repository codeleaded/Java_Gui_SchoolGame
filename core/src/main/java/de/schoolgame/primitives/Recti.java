package de.schoolgame.primitives;

import java.io.*;
import java.util.Objects;

@SuppressWarnings("unused")
public class Recti implements Externalizable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Vec2i pos;
    public Vec2i size;

    public Recti(){
        this.pos = new Vec2i(0, 0);
        this.size = new Vec2i(0, 0);
    }

    public Recti(Vec2i pos, Vec2i size) {
        this.pos = pos;
        this.size = size;
    }

    public Vec2i end() {
        return new Vec2i(pos.x + size.x, pos.y + size.y);
    }

    public Vec2i mid() {
        return pos.add(new Vec2i(this.size.x / 2, this.size.y / 2));
	}

    public boolean overlap(Recti r){
        if (this.pos.x < r.pos.x - this.size.x) return false;
        if (this.pos.x > r.pos.x + r.size.x)    return false;
        if (this.pos.y < r.pos.y - this.size.y) return false;
        return !(this.pos.y > r.pos.y + r.size.y);
    }

    public boolean contains(Vec2i v){
        return v.x >= pos.x && v.x <= pos.x + size.x &&
            v.y >= pos.y && v.y <= pos.y + size.y;
    }


    public Vec2i getDirection(Direction d){
    	if(d==Direction.RIGHT)  return new Vec2i(1,0);
    	if(d==Direction.LEFT)   return new Vec2i(-1,0);
    	if(d==Direction.UP)     return new Vec2i(0,1);
    	if(d==Direction.DOWN)   return new Vec2i(0,-1);
    	return new Vec2i(0,0);
    }

    public Recti cpy(){
        return new Recti(pos.cpy(),size.cpy());
    }

    public boolean compare(Recti r) {
        return pos.x == r.pos.x &&
               pos.y == r.pos.y &&
               size.x == r.size.x &&
               size.y == r.size.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recti rectf = (Recti) o;
        return Objects.equals(pos, rectf.pos) && Objects.equals(size, rectf.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pos, size);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(pos);
        out.writeObject(size);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pos = (Vec2i) in.readObject();
        size = (Vec2i) in.readObject();
    }
}
