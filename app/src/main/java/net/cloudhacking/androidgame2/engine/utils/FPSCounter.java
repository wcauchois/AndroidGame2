package net.cloudhacking.androidgame2.engine.utils;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.ui.Widget;

/**
 * Created by Andrew on 1/26/2015.
 */
public class FPSCounter extends Widget {

    private long mStart;
    private int mFrames;

    private TextRenderer.TextProps mProps;

    public FPSCounter(BindLocation loc) {
        mStart = System.nanoTime();
        mFrames = 0;
        mProps = new TextRenderer.TextProps();

        Image bg = genCounter(99);
        float w = bg.getWidth(), h = bg.getHeight();

        setBindLocation(loc);
        setBounds( new RectF(0, 0, .16f, 1) );
        setBackgroundImage(bg);
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
