package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

/**
 * Created by wcauchois on 1/21/15.
 */
public class RootWidget extends Widget {

    private int mRootWidth;
    private int mRootHeight;

    public RootWidget(int width, int height) {
        super( new RectF(0, 0, width, height), null );
        mRootWidth = width;
        mRootHeight = height;
    }

    public void setRootSize(int sw, int sh) {
        mRootWidth = sw;
        mRootHeight = sh;
        setBounds( new RectF(0, 0, sw, sh) );
    }

    public int getRootWidth() {
        return mRootWidth;
    }

    public int getRootHeight() {
        return mRootHeight;
    }

}
