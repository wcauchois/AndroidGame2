package net.cloudhacking.androidgame2;

import android.content.Context;
import android.opengl.GLES20;
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
public class RenderUtils {
    private static final String TAG = RenderUtils.class.getSimpleName();

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
        return RenderUtils.createProgram(
                RenderUtils.readTextFileFromRawResource(context, vertexShaderResourceId),
                RenderUtils.readTextFileFromRawResource(context, fragmentShaderResourceId)
        );
    }

    public static int createProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader =
                RenderUtils.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader =
                RenderUtils.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

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
