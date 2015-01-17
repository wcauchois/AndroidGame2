package net.cloudhacking.androidgame2.engine.utils;

import android.util.FloatMath;

/**
 * Created by Andrew on 1/15/2015.
 */
public class PointF {

    public float x;
    public float y;

    public PointF() {
        this.x = 0f;
        this.y = 0f;
    }

    public PointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointF(Vec2 v) {
        this.x = v.getX();
        this.y = v.getY();
    }

    public float distTo(PointF other) {
        float dx = other.x - x;
        float dy = other.y - y;
        return FloatMath.sqrt(dx*dx + dy*dy);
    }

    public Vec2 vecTowards(PointF other) {
        return new Vec2(other.x - x, other.y - y);
    }


    @Override
    public String toString() {
        return "PointF(" + this.x + ", " + this.y + ")";
    }
}
