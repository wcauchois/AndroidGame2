package net.cloudhacking.androidgame2.engine.element.shape;

import android.util.FloatMath;

import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.gl.GLColor;
import net.cloudhacking.androidgame2.engine.utils.PointF;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/3/15.
 */
public class Circle extends Renderable {

    public static final int SEGMENTS = 30;
    public static final int BUFFER_SIZE = 2*(SEGMENTS+1);

    private PointF mCenter;
    private float mRadius;
    private float mThickness;

    public FloatBuffer mVertexBuffer;
    private boolean mNeedBufferUpdate;


    public Circle(float radius, float thickness, GLColor color) {
        this(new PointF(), radius, thickness, color);
    }

    public Circle(PointF center, float radius, float thickness, GLColor c) {
        super(0, 0, 0, 0);
        mCenter = center;
        mRadius = radius;
        mThickness = thickness;
        setPos(mCenter);
        setColorM(GLColor.CLEAR);
        setColorA(c);
        updateVertices();
    }

    public PointF getCenter() {
        return mCenter;
    }

    public float getRadius() {
        return mRadius;
    }

    public float getThickness() {
        return mThickness;
    }

    public void setRadius(float r) {
        mRadius = r;
        mNeedBufferUpdate = true;
    }

    public void setThickness(float thickness) {
        mThickness = thickness;
        mNeedBufferUpdate = true;
    }


    private void updateVertices() {
        float d = 2*mRadius + mThickness;
        setActualWidth(d);
        setActualHeight(d);

        float innerR = mRadius-(mThickness/2);
        float outerR = mRadius+(mThickness/2);

        int idx=0;
        float[] vertices = new float[2*BUFFER_SIZE];
        float theta = (float)(2*Math.PI)/SEGMENTS;
        float angle;
        float cos, sin;
        for (int i=0; i<SEGMENTS+1; i++) {
            angle = i*theta;
            cos = FloatMath.cos(angle);
            sin = FloatMath.sin(angle);

            // outer
            vertices[idx++] = outerR*cos;
            vertices[idx++] = outerR*sin;

            // inner
            vertices[idx++] = innerR*cos;
            vertices[idx++] = innerR*sin;
        }

        mVertexBuffer = BufferUtils.makeFloatBuffer(vertices);
        mNeedBufferUpdate = false;
    }


    public boolean contains(PointF p) {
        return mCenter.distTo(p) < mRadius;
    }


    @Override
    public void update() {
        if (mNeedBufferUpdate) {
            updateVertices();
        }
        super.update();
    }

    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);
        gls.drawTriangleStrip(mVertexBuffer, 0, BUFFER_SIZE);
    }

}
