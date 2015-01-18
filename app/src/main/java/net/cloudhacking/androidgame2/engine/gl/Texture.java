package net.cloudhacking.androidgame2.engine.gl;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLUtils;

/**
 * Created by Andrew on 1/16/2015.
 */
public class Texture {

    /**
     * Alias for different texture filter modes (that we need to use)
     */
    public static enum FilterType {
        NEAREST     (GLES20.GL_NEAREST),
        LINEAR      (GLES20.GL_LINEAR);

        private int mValue;
        private FilterType(int value) { mValue = value; }
        public int getValue() { return mValue; }
    }

    /**
     * Alias for different texture wrap types
     */
    public static enum WrapType {
        REPEAT      (GLES20.GL_REPEAT),
        MIRROR      (GLES20.GL_MIRRORED_REPEAT),
        CLAMP       (GLES20.GL_CLAMP_TO_EDGE);

        private int mValue;
        private WrapType(int value) { mValue = value; }
        public int getValue() { return mValue; }
    }

    public static void activate(int glTexUnit) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + glTexUnit);
    }

    /**
     * Texture options
     */
    public static class TextureOptions {
        public FilterType minMode = FilterType.NEAREST;
        public FilterType magMode = FilterType.NEAREST;
        public WrapType wrapModeS = WrapType.CLAMP;
        public WrapType wrapModeT = WrapType.CLAMP;

        public boolean equals(TextureOptions other) {
            return minMode   == other.minMode   &&
                   magMode   == other.magMode   &&
                   wrapModeS == other.wrapModeS &&
                   wrapModeT == other.wrapModeT;
        }
    }


    private int mHandle;
    private boolean mPreMultiplied=false;  // whether or not alpha value has been premultiplied (I think...)

    private int mWidth;
    private int mHeight;

    private FilterType mMinMode;
    private FilterType mMagMode;

    private WrapType mWrapS;    // wrap horizontal
    private WrapType mWrapT;    // wrap vertical

    private Bitmap mBitmap;


    public Texture() {
        mHandle = genNewHandle();
        bind();
    }

    public Texture(Bitmap bmp) {
        this(bmp, FilterType.NEAREST, WrapType.CLAMP);
    }

    public Texture(Bitmap bmp, TextureOptions opts) {
        this(bmp, opts.minMode, opts.magMode, opts.wrapModeS, opts.wrapModeT);
    }

    public Texture(Bitmap bmp, FilterType fType, WrapType wType) {
        this(bmp, fType, fType, wType, wType);
    }

    public Texture(Bitmap bmp,
                   FilterType minMode, FilterType maxMode,
                   WrapType wrapS, WrapType wrapT) {

        mHandle = genNewHandle();
        setBitmap(bmp);
        setFilter(minMode, maxMode);
        setWrap(wrapS, wrapT);
    }


    private int genNewHandle() {
        int[] handles = new int[1];
        GLES20.glGenTextures(1, handles, 0);
        return handles[0];
    }

    public int getHandle() {
        return mHandle;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public RectF getRect() {
        return new RectF(0, 0, mWidth, mHeight);
    }


    public void bind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mHandle);
    }

    public void setFilter(FilterType minMode, FilterType magMode) {
        bind();
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                minMode.getValue());
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                magMode.getValue());

        mMinMode = minMode;
        mMagMode = magMode;
    }

    public void setWrap(WrapType wrapS, WrapType wrapT) {
        bind();
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, wrapS.getValue());
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, wrapT.getValue());

        mWrapS = wrapS;
        mWrapT = wrapT;
    }

    public void setBitmap(Bitmap bmp) {
        bind();
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        mPreMultiplied = true;
        mBitmap = bmp;
        mWidth = bmp.getWidth();
        mHeight = bmp.getHeight();
    }

    public void delete() {
        int[] handles = {mHandle};
        GLES20.glDeleteTextures(1, handles, 0);

        mBitmap.recycle();
        mBitmap = null;
    }

    public void reload() {
        // ensure bitmap is reloaded with same filtering and wrapping parameters.
        mHandle = new Texture(mBitmap, mMinMode, mMagMode, mWrapS, mWrapT).getHandle();
    }

    public RectF getUVRect(float left, float top, float right, float bottom) {
        return new RectF(left/mWidth, top/mHeight, right/mWidth, bottom/mHeight);
    }


    public TextureOptions getOptions() {
        TextureOptions opts = new TextureOptions();
        opts.minMode = mMinMode;
        opts.magMode = mMagMode;
        opts.wrapModeS = mWrapS;
        opts.wrapModeT = mWrapT;
        return opts;
    }
}
