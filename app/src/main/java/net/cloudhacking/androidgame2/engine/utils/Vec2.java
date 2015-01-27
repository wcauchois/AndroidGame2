package net.cloudhacking.androidgame2.engine.utils;

import android.util.FloatMath;

/**
 * Created by wcauchois on 1/8/15.
 */
public class Vec2 {
    public float x, y;

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
        return x;
    }

    public float getY() {
        return y;
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

    public Vec2 increment(Vec2 other) {
        x += other.x;
        y += other.y;
        return this;
    }

    public Vec2 negate() {
        return new Vec2(-x, -y);
    }

    public Vec2 scale(float s) {
        return new Vec2(s * x, s * y);
    }

    public Vec2 setNorm(float s) {
        return scale( s / norm() );
    }

    public Vec2 normalize() {
        return scale( 1 / norm() );
    }

    public float norm() {
        return FloatMath.sqrt(x * x + y * y);
    }

    public float sqNorm() {
        return (x * x + y * y);
    }

    public float manhattanNorm() {
        return Math.abs(x) + Math.abs(y);
    }

    public float dot(Vec2 other) {
        return (x * other.x + y * other.y);
    }

    public float cross(Vec2 other) {
        return (x * other.y - y * other.x);
    }

    public float angle() {
        return (float)Math.atan2(y,x);
    }

    public Vec2 copy() {
        return new Vec2(x, y);
    }

    public Vec2 round() {
        x = Math.round(x);
        y = Math.round(y);
        return this;
    }

    public Vec2 floor() {
        x = (float)Math.floor(x);
        y = (float)Math.floor(y);
        return this;
    }

    public Vec2 ceil() {
        x = (float)Math.ceil(x);
        y = (float)Math.ceil(y);
        return this;
    }


    public String toString() {
        return "Vec2(" + x + ", " + y + ")";
    }
}
