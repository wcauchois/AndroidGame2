package net.cloudhacking.androidgame2.engine.element.shape;

import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/4/15.
 */
public class PixelLines extends Renderable {

    /**
     * Note: Each pair of vertices in the vertex buffer must define the
     * end-points of each line segment.
     */

    private FloatBuffer mVertexBuffer;
    private int mVertexCount;

    public PixelLines(float[] color) {
        this(new float[] {}, color);
    }

    public PixelLines(float[] vertices, float[] color) {
        super(0, 0, 0, 0);
        setVertices(vertices);
        setColor(color);
    }

    public void setVertices(float[] vertices) {
        mVertexCount = vertices.length/2;
        mVertexBuffer = BufferUtils.makeFloatBuffer(vertices);
    }

    public void setColor(float[] color) {
        setColorM(new float[]{0, 0, 0, 0});
        setColorA(color);
    }

    @Override
    public void draw(BasicGLScript gls) {
        if (mVertexCount == 0) return;
        super.draw(gls);
        gls.drawLines(mVertexBuffer, 0, mVertexCount);
    }



}
