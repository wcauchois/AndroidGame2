package net.cloudhacking.androidgame2.engine.utils;

import android.graphics.Rect;

/**
 * Created by Andrew on 1/24/2015.
 */
public class NinePatchAsset extends Asset {

    private Rect mCenterPatch;

    public NinePatchAsset(String filename, Rect centerPatch) {
        super(filename);
        mCenterPatch = centerPatch;
    }

    public Rect getCenterPatch() {
        return mCenterPatch;
    }

}
