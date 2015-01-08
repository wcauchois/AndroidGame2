package net.cloudhacking.androidgame2;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/7/2015.
 */
public class Tower extends LevelGrid.GridItem {
    private static final String TAG = Tower.class.getSimpleName();

    // animation vars
    private final long ANIMATION_FREQUENCY=(long)(0.75*1e9);  // in nanoseconds
    private long sysTimeLastNsec=-1, sysTimeNowNsec;
    private long threshold=0;

    private int[] mAnimationSequence = new int[] {0, 1};  // alternate between first and second tile
    private int mCurrentFrame = 0;

    public Tower() {
        mPosX = 0;
        mPosY = 0;
        mRotation = 0.0f;
        mScaleX = 1.0f;
        mScaleY = 1.0f;
        mTileIndex = mAnimationSequence[mCurrentFrame];
    };


    @Override
    public void update() {

        // on first frame...
        if (sysTimeLastNsec == -1) {
            sysTimeLastNsec = System.nanoTime();
            return;
        }

        // increment total time passed
        sysTimeNowNsec = System.nanoTime();
        threshold += (sysTimeNowNsec - sysTimeLastNsec);
        sysTimeLastNsec = sysTimeNowNsec;

        // if total time passed is greater than threshold, increment frame index.
        if (threshold>=ANIMATION_FREQUENCY) {
            mCurrentFrame = (mCurrentFrame+1) % mAnimationSequence.length;
            mTileIndex = mAnimationSequence[mCurrentFrame];
            threshold -= ANIMATION_FREQUENCY;
        }

        // increment rotation for debug
        mRotation = (mRotation+5) % 360;
    }

}
