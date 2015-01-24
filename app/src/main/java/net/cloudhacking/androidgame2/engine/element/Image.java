package net.cloudhacking.androidgame2.engine.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.QuadDrawer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.ui.WidgetBackground;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 1/18/2015.
 */
public class Image extends Renderable implements WidgetBackground {

    /**
     * This class represents a static texture. It's size will be the same size as the bitmap of the
     * associated texture.  You can still move it around the scene using setPos(), setScale(),
     * setVelocity(), etc, however the vertices and UV coordinates won't change, only the
     * model matrix.  This way we can recycle the vertex buffer! Yey.
     *
     * If you reset the frame, though, the vertex buffer will be regenerated.
     *
     * Note: the point defined by setPos() is meant to be the center point of the image.
     */

    private Texture mTexture = null;
    private RectF mFrame;

    private float[] mVertices;
    private FloatBuffer mVertexBuffer;

    private boolean mNeedBufferUpdate;


    public Image() {
        super(0, 0, 0, 0);
        mVertices = new float[16];
        mVertexBuffer = BufferUtils.makeQuadFloatBuffer();
        mNeedBufferUpdate = false;
    }

    public Image(Texture tex) {
        this();
        setTexture(tex);
    }

    public Image(Asset asset) {
        this();
        setTexture(asset);
    }

    public Image(Asset asset, int x, int y, int w, int h) {
        this(asset);
        setFrame(x, y, w, h);
    }


    /**
     * Move and scale this image to the size of the given rectangle
     */
    public void setToRect(RectF r) {
        float w = r.width(), h = r.height();

        setPos(new PointF(r.left + w / 2, r.top + h / 2));
        setWidth(w);
        setHeight(h);
    }

    // don't fuck with actual pixel width
    private void setActualWidth(float width) {
        super.setWidth(width);
    }

    private void setActualHeight(float height) {
        super.setHeight(height);
    }

    @Override
    public void setWidth(float width) {
        setScaleX( width/getWidth() );
    }

    @Override
    public void setHeight(float height) {
        setScaleY( height/getHeight() );
    }

    public void moveToOrigin() {
        setPos( getScaledWidth()/2 , getScaledHeight()/2 );
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

        setActualWidth(frame.width() * mTexture.getWidth());
        setActualHeight(frame.height() * mTexture.getHeight());

        updateFrame();
        updateVertices();
    }

    public RectF getFrame() {
        return new RectF(mFrame);
    }


    private void updateFrame() {  // UV coordinates
        QuadDrawer.fillUVCoords(mVertices, mFrame);
        mNeedBufferUpdate = true;
    }

    private void updateVertices() {  // scene coordinates, centered on position

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

        // only regenerate the vertex buffer if vertices have been changed
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
