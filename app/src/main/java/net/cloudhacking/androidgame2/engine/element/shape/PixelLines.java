package net.cloudhacking.androidgame2.engine.element.shape;

import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/4/15.
 */
public class PixelLines extends Renderable {

    // TODO: this does not work properly, need to figure out how GL_LINES works...

    private FloatBuffer mVertexBuffer;
    private int mLength;

    public PixelLines(float[] color) {
        this(new float[] {}, color);
    }

    public PixelLines(float[] vertices, float[] color) {
        super(0, 0, 0, 0);
        setVertices(vertices);
        setColor(color);
    }

    public void setVertices(float[] vertices) {
        mLength = vertices.length;
        mVertexBuffer = BufferUtils.makeFloatBuffer(vertices);
    }

    public void setColor(float[] color) {
        setColorM(new float[]{0, 0, 0, 0});
        setColorA(color);
    }

    @Override
    public void draw(BasicGLScript gls) {
        if (mLength==0) return;
        super.draw(gls);
        gls.drawLines(mVertexBuffer, 0, mLength);
    }



}
