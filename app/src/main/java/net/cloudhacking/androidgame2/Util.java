package net.cloudhacking.androidgame2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by wcauchois on 1/3/15.
 */
public class Util {
    private static final String TAG = Util.class.getSimpleName();

    public static class BitmapInfo {
        private int mWidth, mHeight;

        public void setWidth(int width) {
            mWidth = width;
        }

        public void setHeight(int height) {
            mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }

    public static int loadTexture(Context context, int resourceId, BitmapInfo infoOut) {
        int[] textureHandles = new int[1];
        int textureHandle;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // No pre-scaling
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);

        if (infoOut != null) {
            infoOut.setWidth(bitmap.getWidth());
            infoOut.setHeight(bitmap.getHeight());
        }

        GLES20.glGenTextures(1, textureHandles, 0);
        textureHandle = textureHandles[0];
        Util.checkGlError("glGenTextures");

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);
        Util.checkGlError("loadTexture");

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        return textureHandle;
    }

    public static int loadTexture(Context context, int resourceId) {
        return loadTexture(context, resourceId, null);
    }

    public static String readTextFileFromRawResource(final Context context,
                                                     final int resourceId) {
        InputStream is = context.getResources().openRawResource(resourceId);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(isr);

        String nextLine;
        StringBuilder body = new StringBuilder();

        try {
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading text file", e);
            return null;
        }

        return body.toString();
    }

    public static FloatBuffer makeFloatBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(array).position(0);
        return fb;
    }

    public static IntBuffer makeIntBuffer(int[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib = bb.asIntBuffer();
        ib.put(array).position(0);
        return ib;
    }

    public static ShortBuffer makeShortBuffer(short[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer sb = bb.asShortBuffer();
        sb.put(array).position(0);
        return sb;
    }

    public static int loadShader(int type, String shaderCode) {
        int shaderHandle = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shaderHandle, shaderCode);
        GLES20.glCompileShader(shaderHandle);

        // Check for failure
        int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] != GLES20.GL_TRUE) {
            String msg = GLES20.glGetShaderInfoLog(shaderHandle);
            GLES20.glDeleteProgram(shaderHandle);
            Log.e(TAG, "glCompileShader: " + msg);
            throw new RuntimeException("glCompileShader failed");
        }

        return shaderHandle;
    }

    public static int createProgram(Context context,
                                    int vertexShaderResourceId,
                                    int fragmentShaderResourceId) {
        return Util.createProgram(
                Util.readTextFileFromRawResource(context, vertexShaderResourceId),
                Util.readTextFileFromRawResource(context, fragmentShaderResourceId)
        );
    }

    public static int createProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader =
                Util.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader =
                Util.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        int programHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(programHandle, vertexShader);
        GLES20.glAttachShader(programHandle, fragmentShader);
        GLES20.glLinkProgram(programHandle);

        // Check for failure
        int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] != GLES20.GL_TRUE) {
            String msg = GLES20.glGetProgramInfoLog(programHandle);
            GLES20.glDeleteProgram(programHandle);
            Log.e(TAG, "glLinkProgram: " + msg);
            throw new RuntimeException("glLinkProgram failed");
        }

        return programHandle;
    }

    public static void checkGlError(String msg) {
        int error, lastError = GLES20.GL_NO_ERROR;

        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, msg + ": glError " + error);
            lastError = error;
        }
        if (lastError != GLES20.GL_NO_ERROR) {
            throw new RuntimeException(msg + ": glError" + lastError);
        }
    }
}
