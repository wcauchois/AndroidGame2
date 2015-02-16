package net.cloudhacking.androidgame2.engine.gl;

import android.opengl.GLES20;

import java.util.concurrent.Callable;

/**
 * Created by Andrew on 1/23/2015.
 */
public class PreRenderTexture extends Texture {

    /**
     * A texture that is not loaded from a bitmap but created by rendering
     * to a gl texture.  In this case a reloader must be defined to re-render
     * the texture when the gl context is resumed.  If this is added to the
     * asset cache, reload() will automatically be called when the context is
     * resumed.  PreRenderable objects are automatically cached when
     * getPreRendered() is called.
     */

    private static int ID = 0;
    public static void reset() {
        ID = 0;
    }

    private Callable<PreRenderTexture> mReloader;
    private int mID;


    public PreRenderTexture(int w, int h) {
        this(w, h, null);
    }

    public PreRenderTexture(int w, int h, Callable<PreRenderTexture> reloader) {
        super(w, h);
        mReloader = reloader;
        mID = ID++;

        // let GL know the size of the texture and the data types
        this.bind();
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA,
                w, h, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
    }

    public int getId() {
        return mID;
    }

    public void setReloader(Callable<PreRenderTexture> reloader) {
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
