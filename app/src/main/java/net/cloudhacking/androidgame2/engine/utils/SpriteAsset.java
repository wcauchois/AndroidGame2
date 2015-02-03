package net.cloudhacking.androidgame2.engine.utils;

import net.cloudhacking.androidgame2.engine.element.Sprite;

/**
 * Created by Andrew on 1/21/2015.
 */
public class SpriteAsset extends Asset {

    private int mFrameWidth;
    private int mFrameHeight;
    private int mHSpacing;
    private int mVSpacing;
    private int mHOffset;
    private int mVOFfset;

    public SpriteAsset(String filename) {
        this(filename, -1, -1, 0, 0, 0, 0);
    }

    public SpriteAsset(String filename, int fw, int fh) {
        this(filename, fw, fh, 0, 0, 0, 0);
    }

    public SpriteAsset(String filename, int fw, int fh, int hs, int vs, int ho, int vo) {
        super(filename);
        mFrameWidth = fw;
        mFrameHeight = fh;
        mHSpacing = hs;
        mVSpacing = vs;
        mHOffset = ho;
        mVOFfset = vo;
    }

    public int getFrameWidth() {
        return mFrameWidth;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }

    public int getHSpacing() {
        return mHSpacing;
    }

    public int getVSpacing() {
        return mVSpacing;
    }

    public int getHOffset() {
        return mHOffset;
    }

    public int getVOffset() {
        return mVOFfset;
    }

}
