package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.engine.foundation.Renderable;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.foundation.Sprite;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

import java.util.HashMap;

/**
 * Created by Andrew on 1/20/2015.
 */
public class AnimatedSprite extends Renderable {

    /**
     * This cache can be used to bind animations to strings so that multiple objects
     * have access to the same animation.
     */
    public static class AnimationCache {
        /**
         * This maps a handle that describes an animation to an animation object itself. The
         * handle's prefix is the name of the class, so different classes can have animations
         * stored in this cache with the same handle.
         */
        private static HashMap<String, Animation> sCache = new HashMap<String, Animation>();

        public static Animation get(String formalHandle) {
            return sCache.get(formalHandle);
        }

        /**
         * Add an animation sequence to the animation cache.
         *
         * @param cls  Class to which this animation will be attached
         * @param handle  String describing this animation (used for lookup)
         * @param anim  Animation object
         * @param <T>  Class extending AnimatedSprite
         */
        public static <T extends AnimatedSprite> void addAnimation(Class<T> cls,
                                                                   String handle,
                                                                   Animation anim) {
            String formalHandle = cls.getSimpleName() + "__" + handle;
            sCache.put(formalHandle, anim);
        }

        public static void addAnimation(String handle, Animation anim) {
            sCache.put(handle, anim);
        }
    }


    /**********************************************************************************************/

    public static interface Animation {
        public void start();
        public void reset();
        public void update();
        public boolean isAnimating();
        public int getCurrentFrameIndex();
    }


    public static final Animation DEFAULT_ANIMATION = new Animation() {
        public void start() {}
        public void reset() {}
        public void update() {}

        @Override
        public boolean isAnimating() { return true; }

        @Override
        public int getCurrentFrameIndex() { return 0; }
    };


    public class AnimationSequence implements Animation {

        private int[] mAnimationSeq;
        private int mDefaultFrameIndex;
        private int mCurrentFrame;
        private float mFrameSwitchTime;
        private float mThreshold;
        private boolean mCurrentlyAnimating;

        public AnimationSequence(int[] seq, int defaultIndex, float fps) {
            mAnimationSeq = seq;
            mDefaultFrameIndex = defaultIndex;
            mCurrentFrame = 0;
            mFrameSwitchTime = 1/fps;
            mThreshold = 0;

            mCurrentlyAnimating = false;
        }

        @Override
        public void start() {
            mCurrentlyAnimating = true;
            mThreshold = 0;
        }

        @Override
        public void reset() {
            mThreshold = 0;
            mCurrentFrame = 0;
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


    /**********************************************************************************************/

    private String mHandlePrefix;

    private Sprite mSprite;
    private Animation mAnimation;
    private Animation mNextAnimation;
    private boolean mLooping;


    public AnimatedSprite(SpriteAsset asset) {
        super(0, 0, 0, 0);
        mSprite = AssetCache.getSprite(asset);
        setWidth(mSprite.getWidth());
        setHeight(mSprite.getHeight());

        mAnimation = DEFAULT_ANIMATION;

        mHandlePrefix = this.getClass().getSimpleName() + "__";
    }


    public void addAnimation(String handle, Animation animation) {
        AnimationCache.addAnimation(this.getClass(), handle, animation);
    }

    public void queueAnimation(String handle, boolean loop, boolean force) {
        if (force) {
            mAnimation = AnimationCache.get(mHandlePrefix + handle);
        } else {
            mNextAnimation = AnimationCache.get(mHandlePrefix + handle);
        }
        mLooping = loop;
    }

    public void queueAnimation(Animation animation, boolean loop, boolean force) {
        if (force) {
            mAnimation = animation;
        } else {
            mNextAnimation = animation;
        }
        mLooping = loop;
    }


    @Override
    public void update() {
        super.update();

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
            mAnimation = DEFAULT_ANIMATION;
        }

    }

    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);

        // set other uniforms before drawing sprite
        gls.setLighting(getColorM(), getColorA());
        gls.uModel.setValueM4(getModelMatrix());

        mSprite.drawSpriteFrame(gls, mAnimation.getCurrentFrameIndex());
    }

}
