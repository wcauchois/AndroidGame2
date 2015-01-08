package net.cloudhacking.androidgame2;

/**
 * Created by Andrew on 1/8/2015.
 */
public abstract class AnimatedGridItem extends LevelGrid.GridItem {

    private int mDefaultTileIndex;
    private int mCurrentFrame;
    private int[] mCurrentAnimationSeq;
    private boolean mCurrentlyAnimating;
    private boolean mLoopAnimation;
    private long mFrameTime;

    private long mSysTimeLast = -1;
    private long mSysTimeNow;
    private long mSysTimeSinceSwitch = 0;


    public AnimatedGridItem() {
        mDefaultTileIndex = 0;
        mCurrentFrame = 0;
        mCurrentAnimationSeq = new int[] {0};
        mCurrentlyAnimating = false;
        mLoopAnimation = false;
    }

    public int getDefaultTileIndex() {
        return mDefaultTileIndex;
    }

    public int getCurrentFrame() {
        return mCurrentFrame;
    }

    public boolean isCurrentlyAnimating() {
        return mCurrentlyAnimating;
    }

    public boolean checkLoopAnimation() {
        return mLoopAnimation;
    }

    public void setDefaultTileIndex(int defaultTileIndex) {
        mDefaultTileIndex = defaultTileIndex;
    }

    public void setCurrentlyAnimating(boolean bool) {
        mCurrentlyAnimating = bool;
    }

    public void setLoopAnimation(boolean bool) {
        mLoopAnimation = bool;
    }

    /**
     * Queue an animation.
     *
     * @param animationSeq maps animation frame index number to tile indexes on tile set,
     *                     i.e. tileIndex = animationSeq[frameNumber]
     * @param frameTime time between frame switch (in millis)
     * @param loop boolean which determines whether or not animation will loop
     */
    public void queueAnimationSequence(int[] animationSeq, long frameTime, Boolean loop) {
        mCurrentAnimationSeq = animationSeq;
        mFrameTime = frameTime;
        mLoopAnimation = loop;
        mCurrentlyAnimating = true;
        mSysTimeLast = System.currentTimeMillis();
        mSysTimeSinceSwitch = 0;
    }


    public void updateAnimation() {

        if (!mCurrentlyAnimating) {
            setTileIndex(mDefaultTileIndex);
            return;
        }

        // increment total time passed since last frame switch (or call to queueAnimationSequence)
        mSysTimeNow = System.currentTimeMillis();
        mSysTimeSinceSwitch += (mSysTimeNow - mSysTimeLast);
        mSysTimeLast = mSysTimeNow;

        if (mSysTimeSinceSwitch>=mFrameTime) {
            // if total time passed is greater than frame time threshold, increment frame index.
            mCurrentFrame = (mCurrentFrame+1) % mCurrentAnimationSeq.length;
            if ((mCurrentFrame==0) && (!mLoopAnimation)) {
                // if frame index returns to 0 and animation is not looped, reset to default state
                mCurrentlyAnimating = false;
                setTileIndex(mDefaultTileIndex);
                return;
            }
            setTileIndex(mCurrentAnimationSeq[mCurrentFrame]);
            mSysTimeSinceSwitch -= mFrameTime;
        }
    }
}
