package net.cloudhacking.androidgame2.engine;

import android.opengl.Matrix;

import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class Camera extends Loggable {
    private float[] mMatrix;
    private float mZoom;
    private float mLastZoom;
    private Vec2 mPosition;
    private Vec2 mTempOffset;

    public Camera() {
        mMatrix = new float[16];
        Matrix.setIdentityM(mMatrix, 0);
        mZoom = 1f;
        mLastZoom = 1f;
        mPosition = new Vec2();
        mTempOffset = new Vec2();
    }

    public void setPosition(Vec2 newPosition) {
        mPosition = newPosition.copy();

    }

    public void incPosition(Vec2 inc) {
        setPosition(getPosition().add(inc));
    }

    public Vec2 getPosition() {
        return mPosition.copy();
    }


    public void setZoom(float zoom) {
        mZoom = zoom;
    }

    public void bindLastZoom() {
        mLastZoom = mZoom;
    }

    public void setRelativeZoom(float zoom) {
        mZoom = zoom * mLastZoom;
    }

    public void zoom(float zoom) {
        mZoom *= zoom;
    }

    public float getZoom() {
        return mZoom;
    }

    public float getLastZoom() {
        return mLastZoom;
    }

    public void setTempOffset(Vec2 offset) {
        mTempOffset = offset;
    }

    public Vec2 getTempOffset() {
        return mTempOffset;
    }

    public void bindTempOffset() {
        incPosition(mTempOffset);
        mTempOffset = new Vec2();
    }

    // Return a transformation matrix corresponding to this camera.
    public float[] getMatrix() {
        return mMatrix;
    }

    public void updateMatrix() {
        mMatrix[0] = mMatrix[5] = mZoom;
        mMatrix[12] = mPosition.getX() + mTempOffset.getX();
        mMatrix[13] = mPosition.getY() + mTempOffset.getY();
    }
}
