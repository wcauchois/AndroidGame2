package net.cloudhacking.androidgame2.engine.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by wcauchois on 1/3/15.
 */
public class BufferUtils extends Loggable {

    private static final int FLOATS_PER_QUAD = 16;

    public static FloatBuffer makeQuadFloatBuffer() {
        return makeQuadFloatBuffer(1);
    }

    public static FloatBuffer makeQuadFloatBuffer(int quadCount) {
        return makeEmptyFloatBuffer(quadCount * FLOATS_PER_QUAD);
    }

    public static FloatBuffer makeFloatBuffer(float[] array) {
        FloatBuffer fb = makeEmptyFloatBuffer(array.length);
        fb.put(array).position(0);
        return fb;
    }

    public static FloatBuffer makeEmptyFloatBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size * (Float.SIZE/8) );
        bb.order(ByteOrder.nativeOrder());
        return bb.asFloatBuffer();
    }


    public static IntBuffer makeIntBuffer(int[] array) {
        IntBuffer ib = makeEmptyIntBuffer(array.length);
        ib.put(array).position(0);
        return ib;
    }

    public static IntBuffer makeEmptyIntBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size * (Integer.SIZE/8) );
        bb.order(ByteOrder.nativeOrder());
        return bb.asIntBuffer();
    }


    public static ShortBuffer makeShortBuffer(short[] array) {
        ShortBuffer sb = makeEmptyShortBuffer(array.length);
        sb.put(array).position(0);
        return sb;
    }

    public static ShortBuffer makeEmptyShortBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size * (Short.SIZE/2) );
        bb.order(ByteOrder.nativeOrder());
        return bb.asShortBuffer();
    }

}
