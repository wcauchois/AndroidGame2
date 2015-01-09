package net.cloudhacking.androidgame2;

import android.util.Log;

/**
 * Created by wcauchois on 1/8/15.
 */
public class CameraController {
    private Camera mCamera;
    private Vector2 mStartDragCameraPos;

    private class InputListeners implements InputManager.StartDragListener,
            InputManager.EndDragListener,
            InputManager.DragListener {
        public void onStartDrag() {
            mStartDragCameraPos = mCamera.getPosition();
        }

        public void onEndDrag() {
        }

        public void onDrag(Vector2 currentPos, Vector2 posDelta) {
            Log.d("FOO", "got drag delta " + posDelta);
            // TODO(wcauchois): If we implement zooming this will have to take that into account.
            Vector2 newCameraPos = mStartDragCameraPos.add(posDelta.negate());
            mCamera.setPosition(newCameraPos);
        }
    }

    public CameraController(Camera camera, InputManager inputManager) {
        mCamera = camera;

        InputListeners listeners = new InputListeners();
        inputManager.addStartDragListener(listeners);
        inputManager.addEndDragListener(listeners);
        inputManager.addDragListener(listeners);
    }
}
