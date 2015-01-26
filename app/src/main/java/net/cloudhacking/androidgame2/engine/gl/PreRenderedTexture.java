package net.cloudhacking.androidgame2.engine.gl;

import java.util.concurrent.Callable;

/**
 * Created by Andrew on 1/23/2015.
 */
public class PreRenderedTexture extends Texture {

    /**
     * A texture that is not loaded from a bitmap but created by rendering
     * to a gl texture.  In this case a reloader must be defined to re-render
     * the texture when the gl context is resumed.  If this is added to the
     * asset cache, reload() will automatically be called when the context is
     * resumed.  PreRenderable objects are automatically cached when
     * getPreRendered() is called.
     */

    private static int ID = 0;
    public static void resetID() {
        ID = 0;
    }

    private Callable<PreRenderedTexture> mReloader;
    private int mID;


    public PreRenderedTexture(int w, int h) {
        this(w, h, null);
    }

    public PreRenderedTexture(int w, int h, Callable<PreRenderedTexture> reloader) {
        super(w, h);
        mReloader = reloader;
        mID = ID++;
    }

    public int getId() {
        return mID;
    }

    public void setReloader(Callable<PreRenderedTexture> reloader) {
        mReloader = reloader;
    }


    @Override
    public void reload() {
        if (mReloader == null) return;
        try {
            mHandle = mReloader.call().getHandle();
        } catch (Exception e) {
            d("failed to reload texture, ID: "+mID);
            e.printStackTrace();
        }
    }


}
