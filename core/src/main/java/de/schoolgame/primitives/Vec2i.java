package de.schoolgame.primitives;

import java.io.*;
import java.util.Objects;

@SuppressWarnings("unused")
public class Vec2i implements Externalizable {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final Vec2i ZERO = new Vec2i();
    public static final Vec2i ONE = new Vec2i(1, 1);

    public int x, y;

    public Vec2i() {
        x = y = 0;
    }

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vec2i(Vec2i vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vec2i add(Vec2i vec) {
        return new Vec2i(x + vec.x, y + vec.y);
    }

    public Vec2i add(int x, int y) {
        return new Vec2i(x + this.x, y + this.y);
    }

    public Vec2i add(int f) {
        return new Vec2i(x + f, y + f);
    }

    public Vec2i sub(Vec2i vec) {
        return new Vec2i(x - vec.x, y - vec.y);
    }

    public Vec2i sub(int x, int y) {
        return new Vec2i(this.x - x, this.y - y);
    }

    public Vec2i sub(int f) {
        return new Vec2i(x - f, y - f);
    }

    public Vec2i mul(Vec2i vec) {
        return new Vec2i(x * vec.x, y * vec.y);
    }

    public Vec2i mul(int x, int y) {
        return new Vec2i(x * this.x, y * this.y);
    }

    public Vec2i mul(int f) {
        return new Vec2i(x * f, y * f);
    }

    public Vec2i div(Vec2i vec) {
        return new Vec2i(x / vec.x, y / vec.y);
    }

    public Vec2i div(int x, int y) {
        return new Vec2i(this.x / x, this.y / y);
    }

    public Vec2i div(int f) {
        return new Vec2i(x / f, y / f);
    }

    public Vec2i scl(int d) {
        return mul(d);
    }

    public int len2() {
        return x * x + y * y;
    }

    public int len() {
        return (int) Math.sqrt(len2());
    }

    public Vec2i max(Vec2i vec) {
        return new Vec2i(Math.max(x, vec.x), Math.max(y, vec.y));
    }

    public Vec2i min(Vec2i vec) {
        return new Vec2i(Math.min(x, vec.x), Math.min(y, vec.y));
    }

    public Vec2i clamp(Vec2i min, Vec2i max) {
        return new Vec2i(Math.clamp(x, min.x, max.x), Math.clamp(y, min.y, max.y));
    }

    public Vec2i neg() {
        return new Vec2i(-x, -y);
    }

    public Vec2i[] around() {
        return new Vec2i[] {
            new Vec2i(x, y + 1),
            new Vec2i(x + 1, y + 1),
            new Vec2i(x + 1, y),
            new Vec2i(x + 1, y - 1),
            new Vec2i(x, y - 1),
            new Vec2i(x - 1, y - 1),
            new Vec2i(x - 1, y),
            new Vec2i(x - 1, y + 1)
        };
    }

    @Override
    public String toString() {
        return "Vec2i [x=" + x + ", y=" + y + "]";
    }

    public Vec2f toVec2f() {
        return new Vec2f(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vec2i vec2i = (Vec2i) o;
        return x == vec2i.x && y == vec2i.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(x);
        out.writeInt(y);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        x = in.readInt();
        y = in.readInt();
    }
}
