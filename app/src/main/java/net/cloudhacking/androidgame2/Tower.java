package net.cloudhacking.androidgame2;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by Andrew on 1/7/2015.
 *
 * TODO: Eventually this class should probably be abstract
 */
public /*abstract*/ class Tower extends AnimatedGridItem {
    private static final String TAG = Tower.class.getSimpleName();

    // TODO: if this class becomes abstract, this hashmap needs to be moved (I think? Needs to be unique to subclass of tower)
    /**
     * This maps a string handle that describes an animation to an animation sequence itself.
     * An animation sequence is a map from the current frame number of the animation sequence to
     * the corresponding tile index, i.e. tileIndex = animationSeq[frameNumber].
     */
    private static HashMap<String, int[]> sAnimationCache = new HashMap<String, int[]>();

    public static void addAnimationSeq(String handle, int[] animationSeq) {
        sAnimationCache.put(handle, animationSeq);
    }

    public void queueAnimation(String handle, long frameTime, boolean loop) {
        queueAnimationSequence(sAnimationCache.get(handle), frameTime, loop);
    }


    public Tower() {}


    @Override
    public void update() {
        updateAnimation();

        // increment rotation for debug
        setRotation((getRotation()+5) % 360);
    }

}
