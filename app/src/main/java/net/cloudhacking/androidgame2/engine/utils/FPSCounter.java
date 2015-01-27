package net.cloudhacking.androidgame2.engine.utils;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.ui.element.Label;

/**
 * Created by Andrew on 1/26/2015.
 */
public class FPSCounter extends Label {

    private long mStart;
    private int mFrames;

    private static TextRenderer.TextProps mProps = new TextRenderer.TextProps();
    private static final Texture DEFAULT
            = TextRenderer.getInstance().getTexture("FPS: 00", mProps);

    public FPSCounter(BindLocation loc) {
        super(DEFAULT, loc);
        mStart = System.nanoTime();
        mFrames = 0;
    }

    private Image genCounter(int fps) {
        Texture text = TextRenderer.getInstance().getTexture("FPS: "+fps, mProps);
        return new Image(text);
    }

    public void reset() {
        mStart = System.nanoTime();
        mFrames = 0;
    }

    @Override
    public void update() {
        mFrames++;
        if (System.nanoTime() - mStart > 1000000000) {
            setBackgroundImage(genCounter(mFrames));
            reset();
        }
        super.update();
    }

}
