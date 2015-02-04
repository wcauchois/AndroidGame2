package net.cloudhacking.androidgame2.engine.element.shape;

import android.util.FloatMath;

import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/4/15.
 */
public class FilledCircle extends Renderable {

    public static final int SEGMENTS = 30;

    /**
     * Only really need to generate one vertex buffer and
     * then just scale the vertices
     */
    public static FloatBuffer sVertexBuffer;

    static {
        int idx=0;

        float[] vertices = new float[2*(SEGMENTS+1)];
        vertices[idx++] = 0.0f;
        vertices[idx++] = 0.0f;

        float theta = (float)(2*Math.PI)/SEGMENTS;
        float angle;
        for (int i=1; i<=SEGMENTS; i++) {
            angle = i*theta;
            vertices[idx++] = FloatMath.cos(angle);
            vertices[idx++] = FloatMath.sin(angle);
        }

        sVertexBuffer = BufferUtils.makeFloatBuffer(vertices);
    }

    private float mRadius;

    public FilledCircle(float radius, float[] color) {
        this(new PointF(), radius, color);
    }

    public FilledCircle(PointF center, float radius, float[] color) {
        super(0,0,0,0);
        setPos(center);
        mRadius = radius;
        setScale(mRadius);
        setWidth(2);
        setHeight(2);
        setColorM(new float[] {0,0,0,0});
        setColorA(color);
    }


    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);
        gls.drawConvexFilled(sVertexBuffer, 0, SEGMENTS+1);
    }

}
