package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.Texture;

import java.util.HashMap;

/**
 * Created by Andrew on 1/16/2015.
 */
public class TextureFrameSet {

    private static final RectF FULL = new RectF( 0, 0, 1, 1 );

    private int mTextureWidth;
    private int mTextureHeight;

    private int mFrameWidth;
    private int mFrameHeight;

    private int mColumns;
    private int mRows;

    private HashMap<Integer, RectF> mUVFrames = new HashMap<Integer, RectF>();


    public TextureFrameSet(Texture texture, int frameWidth) {
        this(texture, frameWidth, texture.getHeight());
    }

    public TextureFrameSet(Texture texture, int frameWidth, int frameHeight) {

        mTextureWidth = texture.getWidth();
        mTextureHeight = texture.getHeight();

        RectF rect;
        float du = frameWidth/mTextureWidth;
        float dv = frameHeight/mTextureHeight;

        int rows = mTextureWidth/frameWidth;
        int cols = mTextureHeight/frameHeight;

        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                rect = new RectF(j*du, i*dv, (j+1)*du, (i+1)*dv);
                mUVFrames.put(i*cols+j, rect);
            }
        }
        mUVFrames.put(-1, FULL);

        mFrameWidth = frameWidth;
        mFrameHeight = frameHeight;
        mColumns = cols;
        mRows = rows;
    }


    public int getFrameWidth() {    // in texture pixels
        return mFrameWidth;
    }

    public int getFrameHeight() {   // in texture pixels
        return mFrameHeight;
    }

    public RectF getUVFrame(int index) {
        return mUVFrames.get(index);
    }

    public RectF getUVFrame(int ix, int iy) {
        return mUVFrames.get(iy*mColumns + ix);
    }



}
