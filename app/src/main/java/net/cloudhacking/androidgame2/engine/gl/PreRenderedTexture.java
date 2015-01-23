package net.cloudhacking.androidgame2.engine.gl;

import java.util.concurrent.Callable;

/**
 * Created by Andrew on 1/23/2015.
 */
public class PreRenderedTexture extends Texture {

    private Callable<PreRenderedTexture> mReloader;


    public PreRenderedTexture(int w, int h) {
        this(w, h, null);
    }

    public PreRenderedTexture(int w, int h, Callable<PreRenderedTexture> reloader) {
        super(w, h);
        mReloader = reloader;
    }

    public void setReloader(Callable<PreRenderedTexture> reloader) {
        mReloader = reloader;
    }


    @Override
    public void reload() {
        try {
            PreRenderedTexture reloaded = mReloader.call();
            mHandle = reloaded.getHandle();
        } catch (Exception e) {
            d("failed to reload texture");
        }
    }


}
