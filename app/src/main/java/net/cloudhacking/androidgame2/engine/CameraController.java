package net.cloudhacking.androidgame2.engine;

import android.graphics.Rect;

import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class CameraController

        extends Loggable
        implements InputManager.DragListener,
                   InputManager.ScaleListener
{

    private Camera mActiveCamera;
    private Camera mUICamera;


    @Override
    public void onStartDrag(PointF start) {
        if (mActiveCamera == null) return;
    }

    @Override
    public void onDrag(PointF cur, Vec2 dv) {
        if (mActiveCamera == null) return;

        mActiveCamera.incrementPosition(dv.negate());
    }


    private float mRelativeScaleSpan;

    @Override
    public void onStartScale(PointF focus, float span) {
        if (mActiveCamera == null) return;

        mRelativeScaleSpan = span;
    }

    @Override
    public void onScale(PointF focus, float span) {
        if (mActiveCamera == null) return;

        mActiveCamera.setRelativeZoom( span/mRelativeScaleSpan );

        // TODO: make it so the pinch zoom centers on the focus point.  Figure out the math bro
    }

    @Override
    public void onEndScale(PointF focus, float span) {
        if (mActiveCamera == null) return;

        mActiveCamera.bindLastZoom();
    }


    /**********************************************************************************************/

    public CameraController() {
        mActiveCamera = null;
        // init UI camera
    }


    public Camera getActiveCamera() {
        return mActiveCamera;
    }

    public void reset(Camera newCamera) {
        Rect viewport = GameSkeleton.getInstance().getViewport();

        Camera.setInverseGameWidth( 1f/viewport.width() );
        Camera.setInverseGameHeight( 1f/viewport.height() );

        mActiveCamera = newCamera;
    }

    public void update() {
        if (mActiveCamera != null) mActiveCamera.update();
    }
}
