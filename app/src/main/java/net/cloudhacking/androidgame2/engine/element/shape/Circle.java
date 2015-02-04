package net.cloudhacking.androidgame2.engine.element.shape;

import android.util.FloatMath;

import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/3/15.
 */
public class Circle extends Renderable {

    public static final int SEGMENTS = 30;

    private PointF mCenter;
    private float mRadius;
    private float mThickness;

    public FloatBuffer mVertexBuffer;
    private boolean mNeedBufferUpdate;


    public Circle(float radius, float thickness, float[] color) {
        this(new PointF(), radius, thickness, color);
    }

    public Circle(PointF center, float radius, float thickness, float[] color) {
        super(0, 0, 0, 0);
        mCenter = center;
        mRadius = radius;
        mThickness = thickness;
        setPos(mCenter);
        setColorM(new float[]{0, 0, 0, 0});
        setColorA(color);
        mNeedBufferUpdate = true;
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
        setWidth(d);
        setHeight(d);

        float innerR = mRadius-(mThickness/2);
        float outerR = mRadius+(mThickness/2);

        int idx=0;
        float[] vertices = new float[4*(SEGMENTS+1)];
        float theta = (float)(2*Math.PI)/SEGMENTS;
        float angle;
        float cos, sin;
        for (int i=0; i<=SEGMENTS; i++) {
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
        gls.drawTriangleStrip(mVertexBuffer, 0, 2*(SEGMENTS+1));
    }

}
