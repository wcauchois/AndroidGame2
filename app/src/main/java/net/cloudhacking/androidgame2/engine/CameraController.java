package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.Signal;

/**
 * Created by wcauchois on 1/8/15.
 */
public class CameraController extends Loggable {

    private Camera mActiveCamera;
    private Camera mUICamera;
    private boolean mDisabled;

    private float mRelativeScaleSpan;

    /**********************************************************************************************/

    private static class UICamera extends Camera {
        public UICamera() {
            super();
        }
        @Override
        public void update() {
            focusOn(Camera.getViewportCenter());
            super.update();
        }
    }


    public CameraController(InputManager inputManager) {
        mActiveCamera = new Camera();
        mUICamera = new UICamera();
        mDisabled = false;

        inputManager.drag.connect(new Signal.Listener<InputManager.DragEvent>() {
            public boolean onSignal(InputManager.DragEvent e) {
                if (mDisabled) return false;

                mActiveCamera.incrementFocus(e.getDelta());
                return true;
            }
        });

        inputManager.startScale.connect(new Signal.Listener<InputManager.ScaleEvent>() {
            public boolean onSignal(InputManager.ScaleEvent e) {
                if (mDisabled) return false;

                mRelativeScaleSpan = e.getSpan();
                return true;
            }
        });

        inputManager.scale.connect(new Signal.Listener<InputManager.ScaleEvent>() {
            public boolean onSignal(InputManager.ScaleEvent e) {
                if (mDisabled) return false;

                mActiveCamera.setRelativeZoom(e.getSpan() / mRelativeScaleSpan);
                return true;
            }
        });

        inputManager.endScale.connect(new Signal.Listener<InputManager.ScaleEvent>() {
            public boolean onSignal(InputManager.ScaleEvent e) {
                if (mDisabled) return false;

                mActiveCamera.bindRelativeZoom();
                return true;
            }
        });
    }

    public void setBoundaryRect(RectF boundaryRect) {
        mActiveCamera.setBoundaryRect(boundaryRect);
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
