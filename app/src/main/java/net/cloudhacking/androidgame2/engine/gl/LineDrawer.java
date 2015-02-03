package net.cloudhacking.androidgame2.engine.gl;

import android.opengl.GLES20;

import net.cloudhacking.androidgame2.engine.utils.Loggable;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/3/15.
 */
public class LineDrawer extends Loggable {

    public static void drawLines(int offset, int vertexCount) {
        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, offset, vertexCount);
    }

    public static void drawConvexFilled(int offset, int vertexCount) {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, offset, vertexCount);
    }

}
