package net.cloudhacking.androidgame2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class CameraController {
    private Camera mCamera;
    private Vec2 mStartDragCameraPos;

    private class InputListeners implements InputManager.StartDragListener,
            InputManager.EndDragListener,
            InputManager.DragListener {
        public void onStartDrag() {
            mStartDragCameraPos = mCamera.getPosition();
        }

        public void onEndDrag() {
        }

        public void onDrag(Vec2 currentPos, Vec2 posDelta) {
            // TODO(wcauchois): If we implement zooming this will have to take that into account.
            Vec2 newCameraPos = mStartDragCameraPos.add(posDelta.negate());
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
