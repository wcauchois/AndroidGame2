package net.cloudhacking.androidgame2.engine.element;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 1/20/2015.
 */
public class Animated extends Renderable {

    /**
     * This is the basic animated game object class.  It draws itself by first setting the
     * model matrix and other shader uniforms, then passing the current frame index to the
     * associated Sprite (see Sprite).
     *
     * An animated object is controlled by an implementation of the Animation interface.  An
     * animation can be queued or forced to start, and can also optionally be looped.  If there
     * is no animation or the current animation runs out and there is no queued animation, then
     * the NULL_ANIMATION will be set, which will just always render sprite frame zero.
     *
     * The Animation interface is versatile if it is an inner class to an extension of Animated,
     * because then it has access to all the movement variables from Renderable and so it can then
     * animate pretty much anything.  See PokemonFactory for a wacky implementation of this,
     * or AnimationSequence below for a simple implementation.
     */


    abstract public static class Animation {
        public void start() {}
        public void reset() {}
        public void update() {}
        abstract public boolean isAnimating();
        abstract public int getCurrentFrameIndex();
    }


    public static final Animation NULL_ANIMATION = new Animation() {
        @Override
        public boolean isAnimating() {
            return false;
        }
        @Override
        public int getCurrentFrameIndex() {
            return 0;
        }
    };


    // queues animation that constantly returns the same frame number
    public void setPermanentSpriteFrame(final int index) {
        queueAnimation(new Animated.Animation() {
            @Override
            public boolean isAnimating() {
                return true;
            }
            @Override
            public int getCurrentFrameIndex() {
                return index;
            }
        }, true, true);
    }


    //----------------------------------------------------------------------------------------------

    private Sprite mSprite;
    private Animation mAnimation;
    private Animation mNextAnimation;
    private boolean mLooping;


    public Animated(SpriteAsset asset) {
        this( AssetCache.getInstance().getSprite(asset) );
    }

    public Animated(Sprite sprite) {
        super(0, 0, 0, 0);
        mSprite = sprite;
        setWidth(mSprite.getWidth());
        setHeight(mSprite.getHeight());

        mAnimation = NULL_ANIMATION;
    }

    public Sprite getSprite() {
        return mSprite;
    }


    public void queueAnimation(Animation animation, boolean loop, boolean force) {
        if (force) {
            mAnimation = animation;
            mAnimation.start();
        } else {
            mNextAnimation = animation;
        }
        mLooping = loop;
    }

    public boolean isAnimating() {
        return mAnimation.isAnimating();
    }


    @Override
    public void update() {

        if (mAnimation.isAnimating()) {
            mAnimation.update();

        } else if (mNextAnimation != null) {
            mAnimation = mNextAnimation;
            mNextAnimation = null;
            mAnimation.start();

        } else if (mLooping) {
            mAnimation.reset();
            mAnimation.start();

        } else {
            mAnimation = NULL_ANIMATION;
        }

        super.update();
    }

    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);
        mSprite.drawSpriteFrame(gls, mAnimation.getCurrentFrameIndex());
    }



    //----------------------------------------------------------------------------------------------
    // simple implementation of Animation

    public static class AnimationSequence extends Animation {

        private int[] mAnimationSeq;
        private int mDefaultFrameIndex;
        private int mCurrentFrame;
        private float mFrameSwitchTime;
        private float mThreshold;
        private boolean mCurrentlyAnimating;

        public AnimationSequence(int[] seq, int defaultIndex, float fps) {
            mAnimationSeq = seq;
            mDefaultFrameIndex = defaultIndex;
            mCurrentFrame = defaultIndex;
            mFrameSwitchTime = 1/fps;
            mThreshold = 0;

            mCurrentlyAnimating = false;
        }

        @Override
        public void start() {
            mCurrentlyAnimating = true;
            mCurrentFrame = 0;
            mThreshold = 0;
        }

        @Override
        public void reset() {
            mCurrentFrame = mDefaultFrameIndex;
            mCurrentlyAnimating = false;
        }

        @Override
        public boolean isAnimating() {
            return mCurrentlyAnimating;
        }

        @Override
        public void update() {
            if (!mCurrentlyAnimating) {
                return;
            }
            mThreshold += GameTime.getFrameDelta();

            if (mThreshold >= mFrameSwitchTime) {
                mCurrentFrame = (mCurrentFrame+1) % mAnimationSeq.length;
                if (mCurrentFrame == 0) {
                    mCurrentlyAnimating = false;
                    return;
                }
                mThreshold -= mFrameSwitchTime;
            }
        }

        @Override
        public int getCurrentFrameIndex() {
            if (!mCurrentlyAnimating) {
                return mDefaultFrameIndex;
            }
            return mAnimationSeq[mCurrentFrame];
        }
    }

}
