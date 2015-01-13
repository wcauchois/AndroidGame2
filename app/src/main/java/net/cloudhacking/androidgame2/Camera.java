package net.cloudhacking.androidgame2;

import android.opengl.Matrix;

/**
 * Created by wcauchois on 1/8/15.
 */
public class Camera {
    private float[] mMatrix = new float[16];

    public Camera() {
        Matrix.setIdentityM(mMatrix, 0);
    }

    public void setPosition(Vec2 newPosition) {
        mMatrix[12] = -newPosition.getX();
        mMatrix[13] = -newPosition.getY();
    }

    public Vec2 getPosition() {
        return new Vec2(-mMatrix[12], -mMatrix[13]);
    }

    // Return a transformation matrix corresponding to this camera.
    public float[] getMatrix() {
        return mMatrix;
    }
}
