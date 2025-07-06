package de.schoolgame.primitives;

import java.io.*;
import java.util.Objects;

import static de.schoolgame.primitives.Direction.*;
import static java.lang.Math.abs;

@SuppressWarnings("unused")
public class Rect implements Externalizable {
    @Serial
    private static final long serialVersionUID = 1L;
    public Vec2f pos;
    public Vec2f size;

    public Rect(){
        this.pos = new Vec2f(0.0f, 0.0f);
        this.size = new Vec2f(0.0f, 0.0f);
    }

    public Rect(Vec2f pos, Vec2f size) {
        this.pos = pos;
        this.size = size;
    }

    public Vec2f end() {
        return new Vec2f(pos.x + size.x, pos.y + size.y);
    }

    public Vec2f mid() {
        return pos.add(new Vec2f(this.size.x * 0.5f, this.size.y * 0.5f));
	}

    public boolean overlap(Rect r){
        if (this.pos.x < r.pos.x - this.size.x) return false;
        if (this.pos.x > r.pos.x + r.size.x) return false;
        if (this.pos.y < r.pos.y - this.size.y) return false;
        return !(this.pos.y > r.pos.y + r.size.y);
    }

    public boolean contains(Vec2f v){
        return v.x >= pos.x && v.x <= pos.x + size.x &&
            v.y >= pos.y && v.y <= pos.y + size.y;
    }

    public Vec2f GetDelta(Rect r){
        final Vec2f d = r.mid().sub(this.mid());
        final Vec2f newl = r.size.add(this.size);
        d.x /= newl.x;
        d.y /= newl.y;
        return d;
    }

    public Direction getDirection(Rect r){
        if(overlap(r)) {
            Rect ex = new Rect(r.pos.sub(new Vec2f(this.size.x * 0.5f, this.size.y * 0.5f)), r.size.add(this.size));
            Vec2f delta = GetDelta(r);

            if(abs(delta.x) > abs(delta.y)) {
                if(delta.x > 0) {
                    return LEFT;
                }else {
                    return RIGHT;
                }
            }else {
                if(delta.y > 0) {
                    return DOWN;
                }else {
                    return UP;
                }
            }
        }
        return NONE;
    }

	public Direction staticCollisionSolver(Rect r) {
        Direction d = getDirection(r);
        Rect ex = new Rect(r.pos.sub(new Vec2f(this.size.x * 0.5f, this.size.y * 0.5f)), r.size.add(this.size));
        Vec2f delta = GetDelta(r);

        switch (d) {
            case LEFT:
                this.pos.x = ex.pos.x - this.size.x / 2;
                break;
            case RIGHT:
                this.pos.x = ex.pos.x + ex.size.x - this.size.x / 2;
                break;
            case DOWN:
                this.pos.y = ex.pos.y - this.size.y / 2;
                break;
            case UP:
                this.pos.y = ex.pos.y + ex.size.y - this.size.y / 2;
                break;
        }

        return d;
    }

    public boolean compareInt(Rect r) {
        return (int)pos.x == (int)r.pos.x &&
               (int)pos.y == (int)r.pos.y &&
               (int)size.x == (int)r.size.x &&
               (int)size.y == (int)r.size.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rect rect = (Rect) o;
        return Objects.equals(pos, rect.pos) && Objects.equals(size, rect.size);
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
        pos = (Vec2f) in.readObject();
        size = (Vec2f) in.readObject();
    }
}
