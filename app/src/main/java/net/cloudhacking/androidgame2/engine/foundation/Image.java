package net.cloudhacking.androidgame2.engine.foundation;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;

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
        mTexture = AssetCache.getTexture(asset);
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

        setWidth(frame.width() * mTexture.getWidth());
        setHeight(frame.height() * mTexture.getHeight());

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
    public void draw(BasicGLScript gls) {
        super.draw(gls);

        mTexture.bind();

        gls.setLighting(getColorM(), getColorA());

        gls.uModel.setValueM4(getModelMatrix());

        if (mNeedBufferUpdate) {
            mVertexBuffer.position(0);
            mVertexBuffer.put(mVertices);
            mNeedBufferUpdate = false;
        }

        gls.drawQuad(mVertexBuffer);
    }


    public boolean overlapsPoint(PointF p) {
        // This test assumes a square bounding box where the position point
        // is at its center, which is the case with this class.
        PointF c=getPos();
        float hsw = getScaledWidth()/2, hsh = getScaledHeight()/2;

        return p.x >= c.x-hsw && p.x <= c.x+hsw &&
               p.y >= c.y-hsh && p.y <= c.y+hsh;
    }
}
