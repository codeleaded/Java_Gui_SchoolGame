package de.schoolgame.primitives;

import java.io.*;
import java.util.Objects;

@SuppressWarnings("unused")
public class Vec3f implements Externalizable {
    @Serial
    private static final long serialVersionUID = 1L;
    public static final Vec3f ZERO = new Vec3f();
    public static final Vec3f ONE = new Vec3f(1, 1, 1);

    public float x, y, z;

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
        return new Vec3f(this.x - x, this.y - y, this.z - z);
    }

    public Vec3f sub(float f) {
        return new Vec3f(x - f, y - f, z - f);
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
        return new Vec3f(this.x / x, this.y / y, this.z / z);
    }

    public Vec3f div(float f) {
        return new Vec3f(x / f, y / f, z / f);
    }

    public Vec3f scl(float d) {
        return mul(d);
    }

    public float len() {
        return (float) Math.sqrt(len2());
    }

    public float len2() {
        return x * x + y * y + z * z;
    }

    public float dot(Vec3f v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vec3f max(Vec3f v) {
        return new Vec3f(Math.max(x, v.x), Math.max(y, v.y), Math.max(z, v.z));
    }

    public Vec3f min(Vec3f v) {
        return new Vec3f(Math.min(x, v.x), Math.min(y, v.y), Math.min(z, v.z));
    }

    public Vec3f clamp(Vec3f min, Vec3f max) {
        return new Vec3f(Math.clamp(x, min.x, max.x), Math.clamp(y, min.y, max.y), Math.clamp(z, min.z, max.z));
    }

    @Override
    public String toString() {
        return "Vec3f{" +
            "x=" + x +
            ", y=" + y +
            ", z=" + z +
            '}';
    }

    public Vec2f toVec2f() {
        return new Vec2f(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vec3f vec3f = (Vec3f) o;
        return Float.compare(x, vec3f.x) == 0 && Float.compare(y, vec3f.y) == 0 && Float.compare(z, vec3f.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeFloat(x);
        out.writeFloat(y);
        out.writeFloat(z);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        x = in.readFloat();
        y = in.readFloat();
        z = in.readFloat();
    }
}
