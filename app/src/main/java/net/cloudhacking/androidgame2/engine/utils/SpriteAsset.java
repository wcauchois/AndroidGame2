package net.cloudhacking.androidgame2.engine.utils;

/**
 * Created by Andrew on 1/21/2015.
 */
public class SpriteAsset extends Asset {

    private int mFrameWidth;
    private int mFrameHeight;

    public SpriteAsset(String filename, int fw, int fh) {
        super(filename);
        mFrameWidth = fw;
        mFrameHeight = fh;
    }

    public int getFrameWidth() {
        return mFrameWidth;
    }

    public int getFrameHeight() {
        return mFrameHeight;
    }

}
