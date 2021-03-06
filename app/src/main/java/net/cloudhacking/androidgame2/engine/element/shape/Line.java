package net.cloudhacking.androidgame2.engine.element.shape;

import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.QuadDrawer;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.gl.GLColor;
import net.cloudhacking.androidgame2.engine.utils.PointF;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/3/15.
 */
public class Line extends Renderable {

    private float mThickness;
    private PointF mStart;
    private PointF mEnd;

    private static FloatBuffer sVertexBuffer;

    static {
        float[] vertices = new float[16];

        float l, t, r, b;
        l = +0.0f;
        t = -0.5f;
        r = +1.0f;
        b = +0.5f;

        QuadDrawer.fillVertices(vertices, l, t, r, b);
        QuadDrawer.fillUVCoords(vertices, l, t, r, b);  // false coordinates; tex color won't be used

        sVertexBuffer = BufferUtils.makeFloatBuffer(vertices);
    }

    public Line(PointF start, PointF end, float thickness, GLColor color) {
        super(0,0,0,0);
        setPos(start);
        setActualWidth(1);
        setActualHeight(1);
        setThickness(thickness);
        setEndPoints(start, end);
        setColor(color);
    }

    public void setColor(GLColor c) {
        setColorM(GLColor.TRANSPARENT);
        setColorA(c);
    }

    public void setThickness(float thickness) {
        setScaleY(thickness);
        mThickness = thickness;
    }

    public void setEndPoints(PointF start, PointF end) {
        setPos(start);
        setScaleX(start.distTo(end));
        setRotation(start.vecTowards(end).angle());
        mStart = start;
        mEnd = end;
    }

    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);
        gls.drawQuad(sVertexBuffer);
    }

}
