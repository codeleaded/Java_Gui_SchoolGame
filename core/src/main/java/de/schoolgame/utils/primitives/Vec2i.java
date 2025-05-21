package de.schoolgame.utils.primitives;

@SuppressWarnings("unused")
public class Vec2i {
    public int x, y;

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

    public int len() {
        return (int) Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "Vec2i [x=" + x + ", y=" + y + "]";
    }
}
