package net.cloudhacking.androidgame2.engine.ui.widget;

import net.cloudhacking.androidgame2.engine.element.Animated;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by research on 2/12/15.
 */
public class AnimatedButton extends Button {

    private int mDefaultFrame, mDepressedFrame;

    public AnimatedButton(float x, float y, SpriteAsset asset,
                          int defaultFrame, int depressedFrame)
    {
        super(x, y, new Animated(asset));
        ((Animated)getBackground()).setPermanentSpriteFrame(defaultFrame);
        mDefaultFrame = defaultFrame;
        mDepressedFrame = depressedFrame;
    }

    @Override
    public void onTouchDown() {
        super.onTouchDown();
        ((Animated)getBackground()).setPermanentSpriteFrame(mDepressedFrame);
    }

    @Override
    public void onTouchUp() {
        super.onTouchUp();
        ((Animated)getBackground()).setPermanentSpriteFrame(mDefaultFrame);
    }

}
