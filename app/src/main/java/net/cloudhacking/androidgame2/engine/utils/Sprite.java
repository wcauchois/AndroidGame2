package net.cloudhacking.androidgame2.engine.utils;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.foundation.TextureFrameSet;
import net.cloudhacking.androidgame2.engine.gl.Texture;

import java.nio.FloatBuffer;
import java.util.HashMap;

/**
 * Created by Andrew on 1/21/2015.
 */
public class Sprite extends Loggable {

    private Texture mTexture;
    private TextureFrameSet mFrames;

    private int mWidth;
    private int mHeight;

    private float[] mVertices;
    private HashMap<Integer, FloatBuffer> mBufferCache;


    public Sprite(SpriteAsset asset) {
        mTexture = AssetCache.getTexture(asset);
        mFrames = new TextureFrameSet(mTexture,
                asset.getFrameWidth(), asset.getFrameHeight()
        );

        mWidth = asset.getFrameWidth();
        mHeight = asset.getFrameHeight();

        mVertices = new float[16];
        initVertices();

        mBufferCache = new HashMap<Integer, FloatBuffer>();
    }

    public Texture getTexture() {
        return mTexture;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }


    private void initVertices() {

        float halfWidth = mWidth/2, halfHeight = mHeight/2;

        mVertices[0] 	= -halfWidth;
        mVertices[1] 	= -halfHeight;

        mVertices[4] 	= +halfWidth;
        mVertices[5] 	= -halfHeight;

        mVertices[8] 	= +halfWidth;
        mVertices[9] 	= +halfHeight;

        mVertices[12]	= -halfWidth;
        mVertices[13]	= +halfHeight;

    }


    private void setFrame(int frameIndex) {

        RectF frame = mFrames.getUVFrame(frameIndex);

        mVertices[2]  = frame.left;
        mVertices[3]  = frame.top;

        mVertices[6]  = frame.right;
        mVertices[7]  = frame.top;

        mVertices[10] = frame.right;
        mVertices[11] = frame.bottom;

        mVertices[14] = frame.left;
        mVertices[15] = frame.bottom;

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
