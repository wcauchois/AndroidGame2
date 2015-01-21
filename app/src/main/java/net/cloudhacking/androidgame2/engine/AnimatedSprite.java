package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.engine.foundation.Image;

/**
 * Created by Andrew on 1/20/2015.
 */
public class AnimatedSprite extends Image {

    // Old animated sprite class.  Keeping for reference.


    /*
    private int mDefaultTileIndex;
    private int mCurrentFrame;
    private int[] mCurrentAnimationSeq;
    private boolean mCurrentlyAnimating;
    private boolean mLoopAnimation;
    private long mFrameTime;

    private long mSysTimeLast = -1;
    private long mSysTimeNow;
    private long mSysTimeSinceSwitch = 0;

    private String mHandlePrefix;


    public static class AnimationCache {
        *
         * This maps a handle that describes an animation to an animation sequence itself. The
         * handle's prefix is the name of the class, so different classes can have animations with
         * the same handle.
         *
         * An animation sequence is a map from the current frame number of the animation sequence to
         * the corresponding tile index on the sprite sheet, i.e.:
         *      tileIndex = animationSeq[frameNumber];

        private static HashMap<String, int[]> sCache = new HashMap<String, int[]>();

        public static int[] get(String formalHandle) {
            return sCache.get(formalHandle);
        }


        *
         * Add an animation sequence to the animation cache.
         *
         * @param cls  Class to which this animation will be attached
         * @param handle  String describing this animation (used for lookup)
         * @param animationSeq Animation sequence
         * @param <T>  Class extending AnimatedGridItem

        public static <T extends AnimatedGridItem> void addAnimation(Class<T> cls,
                                                                     String handle,
                                                                     int[] animationSeq) {
            String formalHandle = cls.getSimpleName() + "__" + handle;
            sCache.put(formalHandle, animationSeq);
        }
    }



    public AnimatedGridItem() {
        mDefaultTileIndex = 0;
        mCurrentFrame = 0;
        mCurrentAnimationSeq = new int[] {0};
        mCurrentlyAnimating = false;
        mLoopAnimation = false;

        mHandlePrefix = this.getClass().getSimpleName() + "__";
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



    *
     * Queue an animation. TODO: Force animation?
     *
     * @param animationSeq maps animation frame index number to tile indexes on tile set,
     *                     i.e. tileIndex = animationSeq[frameNumber]
     * @param frameTime time between frame switch (in millis)
     * @param loop boolean which determines whether or not animation will loop

    public void queueAnimationSequence(int[] animationSeq, long frameTime, Boolean loop) {
        mCurrentAnimationSeq = animationSeq;
        setTileIndex(mCurrentAnimationSeq[0]);
        mFrameTime = frameTime;
        mLoopAnimation = loop;
        mCurrentlyAnimating = true;
        mSysTimeLast = System.currentTimeMillis();
        mSysTimeSinceSwitch = 0;
    }

    public void queueAnimation(String handle, long frameTime, boolean loop) {
        queueAnimationSequence(AnimationCache.get(mHandlePrefix + handle), frameTime, loop);
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
    */


}
