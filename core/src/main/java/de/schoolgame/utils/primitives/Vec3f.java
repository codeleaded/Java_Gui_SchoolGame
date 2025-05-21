package de.schoolgame.utils.primitives;

@SuppressWarnings("unused")
public class Vec3f {
    public float x;
    public float y;
    public float z;

    public Vec3f() {
        x = y = z = 0;
    }

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(Vec3f v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vec3f(Vec2f v, float z) {
        this.x = v.x;
        this.y = v.y;
        this.z = z;
    }

    public Vec3f add(Vec3f v) {
        return new Vec3f(x + v.x, y + v.y, z + v.z);
    }

    public Vec3f add(float x, float y, float z) {
        return new Vec3f(x + this.x, y + this.y, z + this.z);
    }

    public Vec3f add(float f) {
        return new Vec3f(x + f, y + f, z + f);
    }

    public Vec3f sub(Vec3f v) {
        return new Vec3f(x - v.x, y - v.y, z - v.z);
    }

    public Vec3f sub(float x, float y, float z) {
        return new Vec3f(x - this.x, y - this.y, z - this.z);
    }

    public Vec3f sub(float f) {
        return new Vec3f(0.0f, 0.0f, 0.0f);
    }

    public Vec3f mul(Vec3f v) {
        return new Vec3f(x * v.x, y * v.y, z * v.z);
    }

    public Vec3f mul(float x, float y, float z) {
        return new Vec3f(x * this.x, y * this.y, z * this.z);
    }

    public Vec3f mul(float f) {
        return new Vec3f(x * f, y * f, z * f);
    }

    public Vec3f div(Vec3f v) {
        return new Vec3f(x / v.x, y / v.y, z / v.z);
    }

    public Vec3f div(float x, float y, float z) {
        return new Vec3f(x / this.x, y / this.y, z / this.z);
    }

    public Vec3f div(float f) {
        return new Vec3f(1.0f, 1.0f, 1.0f);
    }

    public Vec3f scl(float d) {
        return mul(d);
    }

    public float len() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
}
