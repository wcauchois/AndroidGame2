package net.cloudhacking.androidgame2.engine;

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
    private boolean mDisabled;


    @Override
    public void onStartDrag(PointF start) {
        if (mDisabled) return;
    }

    @Override
    public void onDrag(PointF cur, Vec2 dv) {
        if (mDisabled) return;

        mActiveCamera.incrementFocus(dv);
    }


    private float mRelativeScaleSpan;

    @Override
    public void onStartScale(PointF focus, float span) {
        if (mDisabled) return;

        mRelativeScaleSpan = span;
    }

    @Override
    public void onScale(PointF focus, float span) {
        if (mDisabled) return;

        mActiveCamera.setRelativeZoom(span / mRelativeScaleSpan);
    }

    @Override
    public void onEndScale(PointF focus, float span) {
        if (mDisabled) return;

        mActiveCamera.bindRelativeZoom();
    }


    /**********************************************************************************************/

    public CameraController() {
        mActiveCamera = new Camera();
        mUICamera = new Camera();
        mDisabled = false;
    }


    public Camera getActiveCamera() {
        return mActiveCamera;
    }

    public Camera getUICamera() {
        return mUICamera;
    }


    public void update() {
        if (!mDisabled) mActiveCamera.update();
    }
}
