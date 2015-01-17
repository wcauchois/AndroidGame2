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

    private HashMap<Integer, RectF> mFrames = new HashMap<Integer, RectF>();


    public TextureFrameSet(Texture texture, int cols) {
        this(texture, cols, 1);
    }

    public TextureFrameSet(Texture texture, int cols, int rows) {
        mTextureWidth = texture.getWidth();
        mTextureHeight = texture.getHeight();

        float du = (float)mTextureWidth / cols;
        float dv = (float)mTextureHeight / rows;

        RectF rect;
        for (int i=0; i<rows; i++) {
            for (int j=0; j<cols; j++) {
                rect = new RectF(j*du, i*dv, (j+1)*du, (i+1)*dv);
                mFrames.put((i*cols + j), rect);
            }
        }

        mFrameWidth = (int)du;
        mFrameHeight = (int)dv;

        mColumns = cols;
        mRows = rows;
    }


    public int getFrameWidth() {
        return mFrameWidth;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }

    public RectF getFrame(int index) {
        return mFrames.get(index);
    }

    public RectF getFrame(int i, int j) {
        return mFrames.get(i*mColumns + j);
    }



}
