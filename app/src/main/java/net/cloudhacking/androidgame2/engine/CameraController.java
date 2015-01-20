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

    public CameraController(InputManager inputManager) {
        mActiveCamera = new Camera();
        mUICamera = new Camera();
        mUICamera.update();  // only need to update once in order to generate matrix
        mDisabled = false;

        inputManager.startDrag.connect(new Signal.Listener<InputManager.StartDragEvent>() {
            public void onSignal(InputManager.StartDragEvent e) {
                // TODO(wcauchois): Copied this from andrew's code, doesn't seem to do anything?
                if (mDisabled) return;
            }
        });

        inputManager.drag.connect(new Signal.Listener<InputManager.DragEvent>() {
            public void onSignal(InputManager.DragEvent e) {
                if (mDisabled) return;

                mActiveCamera.incrementFocus(e.getDelta());
            }
        });

        inputManager.startScale.connect(new Signal.Listener<InputManager.ScaleEvent>() {
            public void onSignal(InputManager.ScaleEvent e) {
                if (mDisabled) return;

                mRelativeScaleSpan = e.getSpan();
            }
        });

        inputManager.scale.connect(new Signal.Listener<InputManager.ScaleEvent>() {
            public void onSignal(InputManager.ScaleEvent e) {
                if (mDisabled) return;

                mActiveCamera.setRelativeZoom(e.getSpan() / mRelativeScaleSpan);
            }
        });

        inputManager.endScale.connect(new Signal.Listener<InputManager.ScaleEvent>() {
            public void onSignal(InputManager.ScaleEvent e) {
                if (mDisabled) return;

                mActiveCamera.bindRelativeZoom();
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
