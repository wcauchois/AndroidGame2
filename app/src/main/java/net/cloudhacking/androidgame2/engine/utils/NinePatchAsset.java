package net.cloudhacking.androidgame2.engine.utils;

import android.graphics.Rect;

/**
 * Created by Andrew on 1/24/2015.
 */
public class NinePatchAsset extends Asset {

    private Rect mCenterPatch;

    public NinePatchAsset(String filename, int cleft, int ctop, int cright, int cbottom) {
        super(filename);
        mCenterPatch = new Rect(cleft, ctop, cright, cbottom);
    }

    public Rect getCenterPatch() {
        return mCenterPatch;
    }

}
