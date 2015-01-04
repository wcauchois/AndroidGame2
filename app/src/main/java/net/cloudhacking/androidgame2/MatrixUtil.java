package net.cloudhacking.androidgame2;

/**
 * More utilities for dealing with 4x4 column-major matrices. Most functions *mutate*
 * the input, and return it (for chaining or whatever).
 *
 * Created by wcauchois on 1/4/15.
 */
public class MatrixUtil {
    public static float[] setTranslation(float[] matrix, float x, float y) {
        matrix[12] = x;
        matrix[13] = y;
        return matrix;
    }

    public static float getXTranslation(float[] matrix) {
        return matrix[12];
    }

    public static float getYTranslation(float[] matrix) {
        return matrix[13];
    }

    public static float[] setScale(float[] matrix, float sx, float sy) {
        matrix[0] = sx;
        matrix[5] = sy;
        return matrix;
    }

    public static float getXScale(float[] matrix) {
        return matrix[0];
    }

    public static float getYScale(float[] matrix) {
        return matrix[5];
    }
}
