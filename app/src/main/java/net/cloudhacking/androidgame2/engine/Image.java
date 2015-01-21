package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.foundation.Renderable;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.TextureCache;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 1/18/2015.
 */
public class Image extends Renderable {

    private Texture mTexture = null;
    private RectF mFrame;

    private float[] mVertices;
    private FloatBuffer mVertexBuffer;

    private boolean mNeedBufferUpdate;


    public Image() {
        super(0, 0, 0, 0);
        mVertices = new float[16];
        mVertexBuffer = BufferUtils.makeEmptyFloatBuffer();
        mNeedBufferUpdate = false;
    }

    public Image(Asset asset) {
        this();
        setTexture(asset);
    }

    public Image(Asset asset, int x, int y, int w, int h) {
        this(asset);
        setFrame(x, y, w, h);
    }


    public void setTexture(Asset asset) {
        mTexture = TextureCache.get(asset);
        setFrame(new RectF(0, 0, 1, 1));
    }

    public void setTexture(Texture tex) {
        mTexture = tex;
        setFrame(new RectF(0, 0, 1, 1));
    }

    public Texture getTexture() {
        return mTexture;
    }

    public void setFrame(int x, int y, int w, int h) {
        setFrame(mTexture.getUVRect(x, y, x + w, y + h));
    }

    public void setFrame(RectF frame) {
        mFrame = frame;
        PointF scale = getScale();

        setWidth(frame.width() * scale.x * mTexture.getWidth());
        setHeight(frame.height() * scale.y * mTexture.getHeight());

        updateFrame();
        updateVertices();
    }

    public RectF getFrame() {
        return new RectF(mFrame);
    }


    private void updateFrame() {

        mVertices[2]  = mFrame.left;
        mVertices[3]  = mFrame.top;

        mVertices[6]  = mFrame.right;
        mVertices[7]  = mFrame.top;

        mVertices[10] = mFrame.right;
        mVertices[11] = mFrame.bottom;

        mVertices[14] = mFrame.left;
        mVertices[15] = mFrame.bottom;

        mNeedBufferUpdate = true;
    }

    private void updateVertices() {

        float halfWidth = getWidth()/2, halfHeight = getHeight()/2;

        mVertices[0] 	= -halfWidth;
        mVertices[1] 	= -halfHeight;

        mVertices[4] 	= +halfWidth;
        mVertices[5] 	= -halfHeight;

        mVertices[8] 	= +halfWidth;
        mVertices[9] 	= +halfHeight;

        mVertices[12]	= -halfWidth;
        mVertices[13]	= +halfHeight;

        mNeedBufferUpdate = true;
    }

    public float[] getVertices() {
        return mVertices.clone();
    }


    @Override
    public void draw() {
        super.draw();
        BasicGLScript script = BasicGLScript.get();

        mTexture.bind();

        script.setLighting(getColorM(), getColorA());

        script.uModel.setValueM4(getModelMatrix());

        if (mNeedBufferUpdate) {
            mVertexBuffer.position(0);
            mVertexBuffer.put(mVertices);
            mNeedBufferUpdate = false;
        }

        script.drawQuad(mVertexBuffer);
    }


    public boolean overlapsPoint(PointF p) {
        // this test assumes a square bounding box
        // where the position point is at its center
        PointF c=getPos();
        float hw=getWidth()/2, hh=getHeight()/2;

        return p.x >= c.x-hw && p.x <= c.x+hw &&
               p.y >= c.y-hh && p.y <= c.y+hh;
    }
}
