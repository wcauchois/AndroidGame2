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
        x = v.getX();
        y = v.getY();
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


    public PointF moveX(float x) {
        this.x += x;
        return this;
    }

    public PointF moveY(float y) {
        this.y += y;
        return this;
    }

    public PointF move(Vec2 dir) {
        x += dir.x;
        y += dir.y;
        return this;
    }


    public PointF copy() {
        return new PointF(x, y);
    }

    public PointF add(Vec2 v) {
        return new PointF(x + v.x, y + v.y);
    }



    public float distTo(PointF other) {
        float dx = other.x - x;
        float dy = other.y - y;
        return FloatMath.sqrt(dx*dx + dy*dy);
    }

    public PointF pointBetween(PointF other) {
        return new PointF((x+other.x)/2, (y+other.y)/2);
    }

    public float distFromOrigin() {
        return FloatMath.sqrt(x * x + y * y);
    }

    public float manhattanDistTo(PointF other) {
        return Math.abs(other.x - x) + Math.abs(other.y - y);
    }

    public Vec2 vecTowards(PointF other) {
        return new Vec2(other.x - x, other.y - y);
    }

    public Vec2 toVec() {
        return new Vec2(x, y);
    }

    public PointF scale(int s) {
        return new PointF(s*x, s*y);
    }

    public PointF scale(PointF s) {
        return new PointF(s.x*x, s.y*y);
    }

    public PointF round() {
        x = Math.round(x);
        y = Math.round(y);
        return this;
    }

    public PointF floor() {
        x = (float)Math.floor(x);
        y = (float)Math.floor(y);
        return this;
    }

    public PointF ceil() {
        x = (float)Math.ceil(x);
        y = (float)Math.ceil(y);
        return this;
    }


    @Override
    public String toString() {
        return "PointF(" + x + ", " + y + ")";
    }
}
