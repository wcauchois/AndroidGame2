package net.cloudhacking.androidgame2.engine.utils;

import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.ui.Widget;

/**
 * Created by Andrew on 1/26/2015.
 */
public class FPSCounter extends Widget {

    private long mStart;
    private int mFrames;

    private static TextRenderer.TextProps mProps = new TextRenderer.TextProps();

    public FPSCounter(float x, float y) {
        super(x, y, new Image(TextRenderer.getInstance().getTexture("FPS: 00", mProps)) );
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
