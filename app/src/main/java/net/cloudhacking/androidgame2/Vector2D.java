package net.cloudhacking.androidgame2;

import java.util.Random;

/**
 * Created by wcauchois on 12/31/14.
 */
public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D unit() {
        double invMag = 1.0 / magnitude();
        return new Vector2D(x * invMag, y / invMag);
    }

    public Vector2D scale(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    public Vector2D invert() {
        return new Vector2D(-x, -y);
    }

    public void addInPlace(Vector2D other) {
        x += other.x;
        y += other.y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    public static Vector2D randomUnit() {
        Random rand = new Random();
        return new Vector2D(
                rand.nextDouble() - 0.5, rand.nextDouble() - 0.5
        ).unit();
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
