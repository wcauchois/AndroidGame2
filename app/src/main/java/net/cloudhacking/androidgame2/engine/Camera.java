package net.cloudhacking.androidgame2.engine;

import android.graphics.Rect;
import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class Camera extends Loggable {

    private static Camera sMainCamera;

    public static Camera getMainCamera() {
        return sMainCamera;
    }
    public static void setMainCamera(Camera camera) {
        sMainCamera = camera;
    }

    private static float sInvGameWidth;
    private static float sInvGameHeight;

    public static void reset(Camera newCamera) {
        Rect viewport = GameSkeleton.getInstance().getViewport();

        sInvGameWidth  = 1f/viewport.width();
        sInvGameHeight = 1f/viewport.height();

        sMainCamera = newCamera;
    }

    public static Camera createFullscreen(float zoom) {
        RectF mapRect = GameSkeleton.getInstance().getScene().getMapRect();

        float w = mapRect.width()/zoom;
        float h = mapRect.height()/zoom;

        return new Camera(new PointF(w/2, h/2), w, h, 1);
    }


    private PointF mPosition;
    private Vec2 mTempOffset;
    private float mWidth;
    private float mHeight;

    private float mZoom;
    private float mLastZoom;

    private float[] mMatrix;


    public Camera() {
        this(new PointF(), 1, 1, 1);
    }

    public Camera(PointF pos, float width, float height, float zoom) {
        mPosition = pos;
        mTempOffset = new Vec2();
        mWidth = width;
        mHeight = height;
        mZoom = zoom;
        mLastZoom = zoom;
        mMatrix = new float[16];
        MatrixUtils.setIdentity(mMatrix);
    }


    public void setPosition(PointF newPosition) {
        mPosition = newPosition.copy();
    }

    public void incPosition(Vec2 inc) {
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
        mMatrix[0]  = mZoom * 2 * sInvGameWidth;
        mMatrix[5]  = mZoom * 2 * sInvGameHeight;
        mMatrix[12] = -1 + mPosition.x * 2 * sInvGameWidth;
        mMatrix[13] = -1 + mPosition.y * 2 * sInvGameHeight;
    }
}
