package net.cloudhacking.androidgame2.engine.old;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import net.cloudhacking.androidgame2.engine.utils.Loggable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Andrew on 1/18/2015.
 */
public class OldUtils extends Loggable {

    /**
     * These aren't really used in the refactored code but the load text file from resource
     * function might be useful later on.
     */

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
            Log.e(Loggable.TAG, "glCompileShader: " + msg);
            throw new RuntimeException("glCompileShader failed");
        }

        return shaderHandle;
    }

    public static int createProgram(Context context,
                                    int vertexShaderResourceId,
                                    int fragmentShaderResourceId) {
        return createProgram(
                readTextFileFromRawResource(context, vertexShaderResourceId),
                readTextFileFromRawResource(context, fragmentShaderResourceId)
        );
    }

    public static int createProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShader =
                loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader =
                loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

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
            Log.e(Loggable.TAG, "glLinkProgram: " + msg);
            throw new RuntimeException("glLinkProgram failed");
        }

        return programHandle;
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
            Log.e(Loggable.TAG, "Error reading text file", e);
            return null;
        }

        return body.toString();
    }
}
