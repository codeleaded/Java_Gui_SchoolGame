package de.schoolgame.utils.primitives;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unused")
public class Vec2f implements Serializable {
    public static final Vec2f ZERO = new Vec2f(0, 0);
    public static final Vec2f ONE = new Vec2f(1, 1);

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

    public float len2() {
        return x * x + y * y;
    }

    public float len() {
        return (float) Math.sqrt(len2());
    }

    public Vec2f max(Vec2f vec) {
        return new Vec2f(Math.max(x, vec.x), Math.max(y, vec.y));
    }

    public Vec2f min(Vec2f vec) {
        return new Vec2f(Math.min(x, vec.x), Math.min(y, vec.y));
    }

    public Vec2f clamp(Vec2f min, Vec2f max) {
        return new Vec2f(Math.clamp(x, min.x, max.x), Math.clamp(y, min.y, max.y));
    }

    @Override
    public String toString() {
        return "Vec2f [x=" + x + ", y=" + y + "]";
    }

    public Vec2i toVec2i() {
        return new Vec2i((int) x, (int) y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vec2f vec2f = (Vec2f) o;
        return Float.compare(x, vec2f.x) == 0 && Float.compare(y, vec2f.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
