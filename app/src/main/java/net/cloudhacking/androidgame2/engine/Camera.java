package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class Camera extends Loggable {

    private PointF mPosition;
    private Vec2 mTempOffset;
    private float mWidth;
    private float mHeight;

    private PointF mFocus;
    private Renderable mTarget;

    private float mZoom;
    private float mLastZoom;

    private float[] mMatrix;


    public Camera(PointF pos, float width, float height, float zoom) {
        mPosition = pos;
        mTempOffset = new Vec2();
        mWidth = width;
        mHeight = height;
        mZoom = zoom;
        mLastZoom = zoom;
        mFocus = new PointF();
        mTarget = null;
        mMatrix = new float[16];
        MatrixUtils.setIdentity(mMatrix);
    }


    public static Camera createFullscreen(float zoom) {
        RectF mapRect = GameSkeleton.getInstance().getScene().getMapRect();

        float w = mapRect.width()/zoom;
        float h = mapRect.height()/zoom;

        return new Camera(new PointF(w/2, h/2), w, h, 1);
    }


    public void focusOn(PointF focus) {

    }

    public void focusOn(Renderable target) {

    }


    public void setPosition(PointF newPosition) {
        mPosition = newPosition.copy();
    }

    public synchronized void incrementPosition(Vec2 inc) {
        mPosition.move(inc);
    }

    public PointF getPosition() {
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
        incrementPosition(mTempOffset);
        mTempOffset = new Vec2();
    }


    public float[] getMatrix() {
        return mMatrix;
    }

    private void updateMatrix() {
        // This matrix transforms game coordinate vertices into the GL [-1,1]X[-1,1] screen space;
        // ***need to multiply y-related matrix entries by -1 in order to preserve orientation for
        //    some reason.  wat r matrices?
        float invW = CameraController.getInverseGameWidth();
        float invH = CameraController.getInverseGameHeight();

        mMatrix[0]  =       mZoom * 2 * invW;
        mMatrix[5]  = -1 * (mZoom * 2 * invH);
        mMatrix[12] =       -1 + (mPosition.x + mTempOffset.x) * 2 * invW;
        mMatrix[13] = -1 * (-1 + (mPosition.y + mTempOffset.y) * 2 * invH);
    }


    public void update() {

        if (mTarget != null) focusOn(mTarget);

        // for UI camera we won't need to update the matrix
        updateMatrix();
    }
}
