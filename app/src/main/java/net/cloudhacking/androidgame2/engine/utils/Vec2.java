package net.cloudhacking.androidgame2.engine.utils;

/**
 * Created by wcauchois on 1/8/15.
 */
public class Vec2 {
    private float x, y;

    public Vec2() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2(int[] xy) {
        this.x = xy[0];
        this.y = xy[1];
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }


    public Vec2 subtract(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    public Vec2 scale(float s) {
        return new Vec2(s * this.x, s * this.y);
    }

    public Vec2 normalize() {
        return scale( 1/dist() );
    }

    public float dist() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public float sqDist() {
        return (this.x * this.x + this.y * this.y);
    }

    public float dot(Vec2 other) {
        return (this.x * other.x + this.y * other.y);
    }

    public float cross(Vec2 other) {
        return (this.x * other.y - this.y * other.x);
    }

    public Vec2 copy() {
        return new Vec2(this.x, this.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
