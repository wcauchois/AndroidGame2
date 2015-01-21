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


    public void moveX(float x) {
        this.x += x;
    }

    public void moveY(float y) {
        this.y += y;
    }

    public void move(Vec2 dir) {
        this.x += dir.x;
        this.y += dir.y;
    }


    public PointF copy() {
        return new PointF(this.x, this.y);
    }

    public PointF add(Vec2 v) {
        return new PointF(this.x + v.x, this.y + v.y);
    }



    public float distTo(PointF other) {
        float dx = other.x - x;
        float dy = other.y - y;
        return FloatMath.sqrt(dx*dx + dy*dy);
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
        this.x = Math.round(this.x);
        this.y = Math.round(this.y);
        return this;
    }

    public PointF floor() {
        this.x = (float)Math.floor(this.x);
        this.y = (float)Math.floor(this.y);
        return this;
    }

    public PointF ceil() {
        this.x = (float)Math.ceil(this.x);
        this.y = (float)Math.ceil(this.y);
        return this;
    }


    @Override
    public String toString() {
        return "PointF(" + this.x + ", " + this.y + ")";
    }
}
