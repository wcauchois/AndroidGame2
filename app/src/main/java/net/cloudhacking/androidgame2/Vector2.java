package net.cloudhacking.androidgame2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class Vector2 {
    private float x, y;

    public Vector2() {
        this.x = 0.0f;
        this.y = 0.0f;
    }

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
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

    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 negate() {
        return new Vector2(-x, -y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
