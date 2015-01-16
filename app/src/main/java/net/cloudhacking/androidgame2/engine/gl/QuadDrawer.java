package net.cloudhacking.androidgame2.engine.gl;

import android.opengl.GLES20;

import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.Utils;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Andrew on 1/15/2015.
 */
public class QuadDrawer extends Loggable {

    private static final short[] QUAD_VERTICES = new short[] {0, 1, 2, 0, 2, 3};

    private static final int SIZE = QUAD_VERTICES.length;  // number of vertices per quad

    private static ShortBuffer mCachedBuffer;    // use this when drawing sets of quads
    private static int mCachedBufferSize;        // number of sets of quad indices in cached buffer


    public static ShortBuffer getIndexBuffer(int quadCount) {
        /**
         * If the requested size is greater than the size of the currently cached buffer,
         * generate a new one.  Otherwise, return the cached buffer.
         */
        if (quadCount > mCachedBufferSize) {

            mCachedBufferSize = quadCount;
            short[] values = new short[quadCount * SIZE];
            int pos = 0;
            int lim = quadCount * 4;
            int inc = 4;

            for (int ofs=0; ofs<lim; ofs+=inc) {
                values[pos++] = (short)(ofs+QUAD_VERTICES[0]);
                values[pos++] = (short)(ofs+QUAD_VERTICES[1]);
                values[pos++] = (short)(ofs+QUAD_VERTICES[2]);
                values[pos++] = (short)(ofs+QUAD_VERTICES[3]);
                values[pos++] = (short)(ofs+QUAD_VERTICES[4]);
                values[pos++] = (short)(ofs+QUAD_VERTICES[5]);
            }

            mCachedBuffer = Utils.makeShortBuffer(values);
        }

        return mCachedBuffer;
    }


    public static void drawQuad() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, SIZE,
                GLES20.GL_UNSIGNED_SHORT, getIndexBuffer(1));
    }

    public static void drawQuadSet(int quadCount) {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, SIZE * quadCount,
                GLES20.GL_UNSIGNED_SHORT, getIndexBuffer(quadCount));
    }
}
