package net.cloudhacking.androidgame2.engine.gl;

import android.graphics.RectF;
import android.opengl.GLES20;

import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.Loggable;

import java.nio.ShortBuffer;

/**
 * Created by Andrew on 1/15/2015.
 */
public class QuadDrawer extends Loggable {

    /**
     * This class is a helper for drawing quads using GLES20.  drawQuad() and drawQuadSet(n) both
     * assume that all relevant uniforms and vertex buffers have been set already.  Since the index
     * ShortBuffer will be the same for all quads, it is cached here.
     *
     *
     * [0]--[1]
     *  |    |
     * [3]--[2]
     */
    private static final short[] QUAD_VERTEX_ORDER = new short[] {0, 1, 2, 0, 2, 3};

    private static final int SIZE = QUAD_VERTEX_ORDER.length;  // number of vertices per quad

    private static ShortBuffer sCachedBuffer;  // vertex index buffer to use for drawing quads
    private static int sCachedBufferSize;  // current number of sets of quad indices in cached buffer


    public static ShortBuffer getIndexBuffer(int quadCount) {
        /**
         * If the requested size is greater than the size of the currently cached buffer,
         * generate a new one.  Otherwise, return the cached buffer.
         */
        if (quadCount > sCachedBufferSize) {

            sCachedBufferSize = quadCount;
            short[] values = new short[quadCount * SIZE];
            int pos = 0;
            int lim = quadCount * 4;
            int inc = 4;

            for (int ofs=0; ofs<lim; ofs+=inc) {
                values[pos++] = (short)(ofs + QUAD_VERTEX_ORDER[0]);
                values[pos++] = (short)(ofs + QUAD_VERTEX_ORDER[1]);
                values[pos++] = (short)(ofs + QUAD_VERTEX_ORDER[2]);
                values[pos++] = (short)(ofs + QUAD_VERTEX_ORDER[3]);
                values[pos++] = (short)(ofs + QUAD_VERTEX_ORDER[4]);
                values[pos++] = (short)(ofs + QUAD_VERTEX_ORDER[5]);
            }

            sCachedBuffer = BufferUtils.makeShortBuffer(values);
        }

        return sCachedBuffer;
    }

    // draw a single quad
    public static void drawQuad() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, SIZE,
                GLES20.GL_UNSIGNED_SHORT, getIndexBuffer(1));
    }

    // draw multiple quads
    public static void drawQuadSet(int quadCount) {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, SIZE * quadCount,
                GLES20.GL_UNSIGNED_SHORT, getIndexBuffer(quadCount));
    }


    public static void fillVertices(float[] v, RectF r) {
        fillVertices(v, r.left, r.top, r.right, r.bottom);
    }

    public static void fillVertices(float[] v, float lt, float tp, float rt, float bm) {

        v[0] 	= lt;
        v[1] 	= tp;

        v[4] 	= rt;
        v[5] 	= tp;

        v[8] 	= rt;
        v[9] 	= bm;

        v[12]	= lt;
        v[13]	= bm;

    }

    public static void fillUVCoords(float[] v, RectF r) {
        fillUVCoords(v, r.left, r.top, r.right, r.bottom);
    }

    public static void fillUVCoords(float[] v, float lt, float tp, float rt, float bm) {

        v[2]  = lt;
        v[3]  = tp;

        v[6]  = rt;
        v[7]  = tp;

        v[10] = rt;
        v[11] = bm;

        v[14] = lt;
        v[15] = bm;

    }
}
