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

    private static float sViewWidth;
    private static float sViewHeight;
    private static float sInvViewWidth;
    private static float sInvViewHeight;
    private static PointF sViewCenter;

    public static void setView(float vw, float vh) {
        sViewWidth = vw;
        sViewHeight = vh;
        sInvViewWidth = 1/vw;
        sInvViewHeight = 1/vh;
        sViewCenter = new PointF(vw/2, vh/2);
    }


    private PointF mFocus;
    private Renderable mTarget;

    private float mZoom;
    private float mLastZoom;

    private float[] mMatrix;

    public Camera() {
        this(new PointF(), 1f);
    }

    /**
     * Creates a new camera object.  Typically we will only really need a game level
     * camera (mActiveCamera), and a UI camera (mUICamera).  The camera's center point
     * will be the [focus].  If [mTarget] is not null then the camera will automatically
     * focus on the center of the target.
     *
     * @param focus focal point of camera
     * @param zoom zoom>1 := zoom in, zoom<1 := zoom out
     */
    public Camera(PointF focus, float zoom) {
        mFocus = focus;
        mZoom = zoom;
        mLastZoom = zoom;
        mTarget = null;
        mMatrix = new float[16];
        MatrixUtils.setIdentity(mMatrix);
    }


    public float[] getMatrix() {
        return mMatrix;
    }


    public void setTarget(Renderable target) {
        mTarget = target;
    }

    public void focusOn(Renderable target) {
        focusOn(target.getCenter());
    }

    public void focusOn(PointF focus) {
        focusOn(focus.x, focus.y);
    }

    public void focusOn(float x, float y) {
        mFocus.set(x, y);
    }

    public synchronized void incrementFocus(Vec2 inc) {
        mFocus.move(inc.scale(1/mZoom));
    }


    public void setZoom(float zoom) {
        mZoom = zoom;
    }

    public void setRelativeZoom(float zoom) {
        mZoom = zoom * mLastZoom;
    }

    public void bindRelativeZoom() {
        mLastZoom = mZoom;
    }

    public float getZoom() {
        return mZoom;
    }


    public PointF sceneToCamera(PointF scenePoint) {
        Vec2 adjust = mFocus.vecTowards(scenePoint).scale(mZoom);

        return sViewCenter.add(adjust);
    }

    public PointF cameraToScene(PointF touchPoint) {
        Vec2 adjust = sViewCenter.vecTowards(touchPoint).scale(1/mZoom);

        return mFocus.add(adjust);
    }


    private void updateMatrix() {
        // This matrix transforms game coordinate vertices into the GL [-1,1]X[-1,1] screen space;
        // The center of this camera will be Vec2(0,0) + mFocus .  The zoom is multiplied by
        // the inverse of the view width/height in order to correct for the screen's aspect ratio.

        mMatrix[0]  = +mZoom * 2 * sInvViewWidth;
        mMatrix[5]  = -mZoom * 2 * sInvViewHeight;  // negative to preserve vertical orientation
        mMatrix[12] = -mFocus.x * mMatrix[0];
        mMatrix[13] = -mFocus.y * mMatrix[5];
    }

    public void update() {
        if (mTarget != null) focusOn(mTarget);
        updateMatrix();
    }
}
