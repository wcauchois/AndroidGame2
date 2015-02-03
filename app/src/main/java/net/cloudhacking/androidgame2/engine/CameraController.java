package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.Camera;
import net.cloudhacking.androidgame2.engine.ui.UICamera;
import net.cloudhacking.androidgame2.engine.utils.Loggable;

/**
 * Created by wcauchois on 1/8/15.
 */
public class CameraController extends Loggable implements Signal.Listener {

    private Camera mActiveCamera;
    private UICamera mUICamera;
    private boolean mDisabled;

    private float mRelativeScaleSpan;

    @Override
    public boolean onSignal(Object o) {
        if (o instanceof InputManager.DragEvent) {
            return handleDrag((InputManager.DragEvent)o);
        } else if (o instanceof InputManager.ScaleEvent) {
            return handleScale((InputManager.ScaleEvent)o);
        }
        return false;
    }

    private boolean handleDrag(InputManager.DragEvent e) {
        if (mDisabled) return false;

        switch (e.getType()) {
            case START:
                break;

            case UPDATE:
                mActiveCamera.incrementFocus(e.getDelta().negate());
                break;

            case END:
                break;
        }
        return true;
    }

    private boolean handleScale(InputManager.ScaleEvent e) {
        if (mDisabled) return false;

        switch (e.getType()) {
            case START:
                mRelativeScaleSpan = e.getSpan();
                break;

            case UPDATE:
                mActiveCamera.setRelativeZoom(e.getSpan() / mRelativeScaleSpan);
                break;

            case END:
                mActiveCamera.bindRelativeZoom();
                break;
        }

        return true;
    }

    /**********************************************************************************************/


    public CameraController() {
        mActiveCamera = new Camera();
        mUICamera = new UICamera();
        mDisabled = false;
    }

    public void setBoundaryRect(RectF boundaryRect) {
        mActiveCamera.setBoundaryRect(boundaryRect);
    }

    public Camera getActiveCamera() {
        return mActiveCamera;
    }

    public UICamera getUICamera() {
        return mUICamera;
    }


    public void update() {
        if (!mDisabled) mActiveCamera.update();
    }
}
