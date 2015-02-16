package net.cloudhacking.androidgame2.engine.element.shape;

import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.QuadDrawer;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.gl.GLColor;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 2/9/2015.
 */
public class ColorBlock extends Renderable {

    private static FloatBuffer mVertexBuffer;

    static {
        float[] vertices = new float[16];
        QuadDrawer.fillVertices(vertices, -0.5f, -0.5f, +0.5f, +0.5f);
        QuadDrawer.fillUVCoords(vertices, -0.5f, -0.5f, +0.5f, +0.5f);
        mVertexBuffer = BufferUtils.makeFloatBuffer(vertices);
    }

    public ColorBlock(float w, float h, GLColor color) {
        super(0,0,1,1);
        setScaleX(w);
        setScaleY(h);
        setColorM(GLColor.TRANSPARENT);
        setColorA(color);
    }

    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);
        gls.drawQuad(mVertexBuffer);
    }

}
