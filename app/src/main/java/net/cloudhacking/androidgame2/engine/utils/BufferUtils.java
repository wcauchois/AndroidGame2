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

    public static FloatBuffer makeFloatBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * (Float.SIZE/8) );
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(array).position(0);
        return fb;
    }

    public static FloatBuffer makeEmptyFloatBuffer() {
        return makeEmptyFloatBuffer(1);
    }

    public static FloatBuffer makeEmptyFloatBuffer(int quadCount) {
        ByteBuffer bb = ByteBuffer.allocateDirect(quadCount * FLOATS_PER_QUAD * (Float.SIZE/8) );
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        return fb;
    }


    public static IntBuffer makeIntBuffer(int[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * (Integer.SIZE/8) );
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib = bb.asIntBuffer();
        ib.put(array).position(0);
        return ib;
    }

    public static ShortBuffer makeShortBuffer(short[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * (Short.SIZE/2) );
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer sb = bb.asShortBuffer();
        sb.put(array).position(0);
        return sb;
    }

}
