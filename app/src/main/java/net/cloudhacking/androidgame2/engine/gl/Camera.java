package net.cloudhacking.androidgame2.engine.gl;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class Camera extends Loggable {

    private static float sScrollSpeed;

    public static void setScrollSpeed(float s) {
        sScrollSpeed = s;
    }


    private static float sViewWidth;
    private static float sViewHeight;
    private static float sDoubleInvViewWidth;
    private static float sDoubleInvViewHeight;
    private static PointF sViewCenter;

    public static void setView(float vw, float vh) {
        sViewWidth = vw;
        sViewHeight = vh;
        sDoubleInvViewWidth = 2/vw;
        sDoubleInvViewHeight = 2/vh;
        sViewCenter = new PointF(vw/2, vh/2);
    }

    public static PointF getViewportCenter() {
        return sViewCenter;
    }


    private PointF mFocus;
    private Renderable mTarget;

    private float mZoom;
    private float mLastZoom;

    private float[] mMatrix;


    /**
     * Camera will not be able to pan outside this rectangle if it is defined.
     *
     * Note: the left and top values of the rect should both be zero, i.e. the top-left vertex
     *       of the rect should be (0,0).
     */
    private RectF mBoundaryRect;
    private float mMinZoom;
    private boolean mCheckBounds;

    public void setBoundaryRect(RectF br) {
        if (br == null) {
            mBoundaryRect = null;
            mCheckBounds = false;
            mMinZoom = 0f;
            return;
        }
        mBoundaryRect = br;
        mCheckBounds = true;
        mFocus = new PointF(br.centerX(), br.centerY());
        mMinZoom = Math.max(sViewWidth/br.right, sViewHeight/br.bottom);
        setZoom( mMinZoom );
    }

    public void setBoundary(int sceneX, int sceneY) {
        setBoundaryRect( new RectF(0, 0, sceneX, sceneY) );
    }



    /**
     * Creates a new camera object.  Typically we will only really need a game level
     * camera (mActiveCamera), and a UI camera (mUICamera).  The camera's center point
     * will be the [focus].  If [mTarget] is not null then the camera will automatically
     * focus on the center of the target.
     *
     * @param focus focal point of camera
     * @param zoom zoom level
     */
    public Camera(PointF focus, float zoom) {
        mFocus = focus;
        mZoom = zoom;
        mLastZoom = zoom;
        mTarget = null;
        mBoundaryRect = null;
        mCheckBounds = false;
        mMinZoom = 0f;
        mMatrix = new float[16];
        MatrixUtils.setIdentity(mMatrix);
    }

    public Camera() {
        this(new PointF(), 1f);
    }


    public float[] getMatrix() {
        return mMatrix;
    }


    private void enforceBounds() {
        if (mCheckBounds) {
            float hwz, hhz, lb, tb, rb, bb;

            hwz = sViewWidth / (2 * mZoom);     // half of the zoomed width
            hhz = sViewHeight / (2 * mZoom);    // half of the zoomed height

            lb = mFocus.x;
            tb = mFocus.y;
            rb = mBoundaryRect.right - mFocus.x;
            bb = mBoundaryRect.bottom - mFocus.y;

            if (lb < hwz) { mFocus.moveX( hwz-lb ); }
            if (rb < hwz) { mFocus.moveX( rb-hwz ); }
            if (tb < hhz) { mFocus.moveY( hhz-tb ); }
            if (bb < hhz) { mFocus.moveY( bb-hhz ); }
        }
    }


    public void setTarget(Renderable target) {
        mTarget = target;
    }

    public void focusOn(Renderable target) {
        focusOn(target.getPos());
    }

    public void focusOn(PointF focus) {
        focusOn(focus.x, focus.y);
    }

    public void focusOn(float x, float y) {
        mFocus.set(x, y);
        enforceBounds();
    }

    public void incrementFocus(Vec2 inc) {
        mFocus.move( inc.scale( sScrollSpeed / mZoom ) );
        enforceBounds();
    }


    public void setZoom(float zoom) {
        mZoom = zoom;
        mLastZoom = zoom;
    }

    public void setRelativeZoom(float zoom) {
        mZoom = Math.max( zoom*mLastZoom, mMinZoom );
        enforceBounds();
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
        Vec2 adjust = sViewCenter.vecTowards(touchPoint).scale( 1 / mZoom );

        return mFocus.add(adjust);
    }


    private void updateMatrix() {
        /*
         * This matrix transforms game coordinate vertices into the GL [-1,1]X[-1,1] screen space.
         * The center of this camera will be [mFocus] (which is in scene space).  The zoom is
         * multiplied by the inverse of the view width/height in order to correct for
         * the screen's aspect ratio.
         */
        mMatrix[0]  = +mZoom * sDoubleInvViewWidth;
        mMatrix[5]  = -mZoom * sDoubleInvViewHeight;  // negative to preserve vertical orientation
        mMatrix[12] = -mFocus.x * mMatrix[0];
        mMatrix[13] = -mFocus.y * mMatrix[5];
    }

    public void update() {
        if (mTarget != null) focusOn(mTarget);
        updateMatrix();
    }
}
