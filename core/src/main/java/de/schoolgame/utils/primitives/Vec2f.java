package de.schoolgame.utils.primitives;

@SuppressWarnings("unused")
public class Vec2f {
    public float x, y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2f(Vec2f vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    public Vec2f add(Vec2f vec) {
        return new Vec2f(x + vec.x, y + vec.y);
    }

    public Vec2f add(float x, float y) {
        return new Vec2f(x + this.x, y + this.y);
    }

    public Vec2f add(float f) {
        return new Vec2f(x + f, y + f);
    }

    public Vec2f sub(Vec2f vec) {
        return new Vec2f(x - vec.x, y - vec.y);
    }

    public Vec2f sub(float x, float y) {
        return new Vec2f(this.x - x, this.y - y);
    }

    public Vec2f sub(float f) {
        return new Vec2f(x - f, y - f);
    }

    public Vec2f mul(Vec2f vec) {
        return new Vec2f(x * vec.x, y * vec.y);
    }

    public Vec2f mul(float x, float y) {
        return new Vec2f(x * this.x, y * this.y);
    }

    public Vec2f mul(float f) {
        return new Vec2f(x * f, y * f);
    }

    public Vec2f div(Vec2f vec) {
        return new Vec2f(x / vec.x, y / vec.y);
    }

    public Vec2f div(float x, float y) {
        return new Vec2f(this.x / x, this.y / y);
    }

    public Vec2f div(float f) {
        return new Vec2f(x / f, y / f);
    }

    public Vec2f scl(float d) {
        return mul(d);
    }

    public float len() {
        return (float) Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "Vec2f [x=" + x + ", y=" + y + "]";
    }
}
