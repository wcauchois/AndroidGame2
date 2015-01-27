package net.cloudhacking.androidgame2.engine.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.QuadDrawer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;
import net.cloudhacking.androidgame2.engine.utils.TextureFrameSet;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Created by Andrew on 1/21/2015.
 */
public class Sprite extends Loggable {

    /**
     * This class represents an animated sprite with all its frames laid out on a texture
     * (A.K.A. a sprite sheet). This class is not meant to be extended, but rather gotten from
     * the AssetCache and then used to call drawSpriteFrame(gls, frameIndex).  This is because
     * we only really need one instance of Sprite for each sprite sheet, since the vertex buffers
     * for the frames can be shared.  The vertex buffer for each frame is also cached so that if
     * we have 100 sprites using a single sprite sheet, with several of each drawing the
     * same frame, they can each just use the same vertex buffer.
     *
     * See Animated for the basic implementation of this.
     */

    private Texture mTexture;
    private TextureFrameSet mFrames;

    private int mWidth;
    private int mHeight;

    private float[] mVertices;
    private HashMap<Integer, FloatBuffer> mBufferCache;


    public Sprite(SpriteAsset asset) {
        mTexture = AssetCache.getInstance().getTexture(asset);
        mFrames = new TextureFrameSet(mTexture, asset);

        mWidth = asset.getFrameWidth();
        mHeight = asset.getFrameHeight();

        mVertices = new float[16];
        initVertices();

        mBufferCache = new HashMap<Integer, FloatBuffer>();

        d("successfully created new sprite sheet from " + asset.getFileName()
                + ", cols=" + mFrames.getFrameColumns() + ", rows=" + mFrames.getFrameRows());
    }

    public Texture getTexture() {
        return mTexture;
    }

    public TextureFrameSet getFrames() {
        return mFrames;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public int getFrameColumns() {
        return mFrames.getFrameColumns();
    }

    public int getFrameRows() {
        return mFrames.getFrameRows();
    }

    public int getMaxFrameIndex() {
        return mFrames.getMaxFrameIndex();
    }

    public void setMaxFrameIndex(int maxFrameIndex) {
        mFrames.setMaxFrameIndex(maxFrameIndex);
    }


    private void initVertices() {
        float hw = mWidth/2, hh = mHeight/2;
        QuadDrawer.fillVertices(mVertices, -hw, -hh, hw, hh);
    }


    private void setFrame(int frameIndex) {
        RectF frame = mFrames.getUVFrame(frameIndex);
        QuadDrawer.fillUVCoords(mVertices, frame);
    }


    /**
     * Draw the given frame off of the sprite sheet.  Note that this must be called
     * at the end of the caller's draw function because this function will not set the
     * model matrix, lighting, or other uniforms.
     *
     * @param gls  GLScript instance
     * @param frameIndex  Index of frame on sprite sheet to draw
     */
    public void drawSpriteFrame(BasicGLScript gls, int frameIndex) {
        FloatBuffer vbo;

        if (!mBufferCache.containsKey(frameIndex)) {
            setFrame(frameIndex);
            vbo = BufferUtils.makeFloatBuffer(mVertices);
            mBufferCache.put(frameIndex, vbo);

        } else {
            vbo = mBufferCache.get(frameIndex);
        }

        mTexture.bind();
        gls.drawQuad(vbo);
    }

}
