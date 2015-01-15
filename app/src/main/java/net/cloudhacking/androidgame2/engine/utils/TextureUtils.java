package net.cloudhacking.androidgame2.engine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;


/**
 * Created by Andrew on 1/7/2015.
 */
public class TextureUtils extends Loggable {

    public static final int GL_MAX_TEXTURE_UNITS = GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS;

    // this corresponds to GL_TEXTURE0, GL_TEXTURE1, etc.  Start with GL_TEXTURE0
    // where 0 <= currentOpenTextureUnit < GL_MAX_TEXTURE_UNITS
    public static int currentOpenTextureUnit=0;

    public static void reset() {
        currentOpenTextureUnit = 0;
    }


    public static class TextureInfo {
        private int mBitmapWidth, mBitmapHeight;
        private int mGLTextureUnit, mGLTextureHandle;

        public void setBitmapWidth(int width) {
            mBitmapWidth = width;
        }

        public void setBitmapHeight(int height) {
            mBitmapHeight = height;
        }

        public void setGLTextureUnit(int glTextureUnit) {
            mGLTextureUnit = glTextureUnit;
        }

        public void setGLTextureHandle(int glTextureHandle) {
            mGLTextureHandle = glTextureHandle;
        }

        public int getBitmapWidth() {
            return mBitmapWidth;
        }

        public int getBitmapHeight() {
            return mBitmapHeight;
        }

        public int getGLTextureUnit() {
            return mGLTextureUnit;
        }

        public int getGLTextureHandle() {
            return mGLTextureHandle;
        }
    }


    public static TextureInfo loadTexture(Context context, int resourceId) {
        int[] textureHandles = new int[1];
        int textureHandle;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // No pre-scaling
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        GLES20.glGenTextures(1, textureHandles, 0);
        textureHandle = textureHandles[0];
        RenderUtils.checkGlError("glGenTextures");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + currentOpenTextureUnit);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_NEAREST);
        RenderUtils.checkGlError("loadTexture");

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        TextureInfo textureInfo = new TextureInfo();
        textureInfo.setBitmapWidth(bitmap.getWidth());
        textureInfo.setBitmapHeight(bitmap.getHeight());
        textureInfo.setGLTextureUnit(currentOpenTextureUnit);
        textureInfo.setGLTextureHandle(textureHandle);

        currentOpenTextureUnit++;
        if (currentOpenTextureUnit>=GL_MAX_TEXTURE_UNITS) {
            Log.e(TAG, "Max allowed textureInfo units reached!");
        }

        bitmap.recycle();

        return textureInfo;
    }

}
