package net.cloudhacking.androidgame2.engine.utils;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

import java.util.HashMap;

/**
 * Created by Andrew on 1/16/2015.
 */
public class TextureFrameSet extends Loggable {

    /**
     * This class is intended to use with a sprite sheet or tile set.  It will automatically
     * generate and cache UV coordinates for all the frames on the texture given the frame size,
     * spacing, and offset.  There only really needs to be one instance of this per texture.
     *
     * See TileMap and Sprite for usage of this.
     */

    private static final RectF FULL = new RectF( 0, 0, 1, 1 );

    private int mTextureWidth;
    private int mTextureHeight;

    private int mFrameWidth;
    private int mFrameHeight;

    private int mColumns;
    private int mRows;

    private int mMaxFrameIndex;

    private HashMap<Integer, RectF> mUVFrames = new HashMap<Integer, RectF>();


    public TextureFrameSet(Texture texture, int frameWidth) {
        this(texture, frameWidth, texture.getHeight());
    }

    public TextureFrameSet(Texture texture, int frameWidth, int frameHeight) {
        this(texture, frameWidth, frameHeight, 0, 0, 0, 0);
    }

    public TextureFrameSet(Texture texture, int frameWidth, int frameHeight,
                                            int hSpacing,   int vSpacing,
                                            int hOffset,    int vOffset)
    {

        mTextureWidth = texture.getWidth();
        mTextureHeight = texture.getHeight();

        if (frameWidth == -1) frameWidth = mTextureWidth;
        if (frameHeight == -1) frameHeight = mTextureHeight;

        RectF rect;
        float du = (float)frameWidth/mTextureWidth;
        float dv = (float)frameHeight/mTextureHeight;

        int rows = mTextureHeight/frameHeight;
        int cols = mTextureWidth/frameWidth;

        int ho = hOffset,  vo = vOffset;

        for (int iy=0; iy<rows; iy++) {
            for (int ix=0; ix<cols; ix++) {
                rect = new RectF(ho+ix*du, vo+iy*dv, ho+(ix+1)*du, vo+(iy+1)*dv);
                ho += hSpacing;
                vo += vSpacing;
                mUVFrames.put(iy*cols+ix, rect);
            }
        }
        mUVFrames.put(-1, FULL);

        mFrameWidth = frameWidth;
        mFrameHeight = frameHeight;
        mColumns = cols;
        mRows = rows;
    }

    public TextureFrameSet(Texture texture, SpriteAsset a) {
        this(texture, a.getFrameWidth(), a.getFrameHeight(),
                      a.getHSpacing(),   a.getVSpacing(),
                      a.getHOffset(),    a.getVOffset()
        );
    }


    public void setMaxFrameIndex(int maxFrameIdx) {
        mMaxFrameIndex = maxFrameIdx;
    }

    public int getMaxFrameIndex() {
        return mMaxFrameIndex;
    }

    public int getFrameWidth() {    // in texture pixels
        return mFrameWidth;
    }

    public int getFrameHeight() {   // in texture pixels
        return mFrameHeight;
    }

    public int getFrameColumns() {
        return mColumns;
    }

    public int getFrameRows() {
        return mRows;
    }

    public RectF getUVFrame(int index) {
        return mUVFrames.get(index);
    }

    public RectF getUVFrame(int ix, int iy) {
        return mUVFrames.get(iy*mColumns + ix);
    }



}
