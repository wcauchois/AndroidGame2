package net.cloudhacking.androidgame2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class CameraController {
    private Camera mCamera;
    private SceneInfo mSceneInfo;
    private CameraScrollListener mScrollListener;
    private CameraZoomListener mZoomListener;


    private class CameraScrollListener implements InputManager.DragListener {
        private InputManager.Pointer mPointer;
        private Vec2 mCameraOrigin;

        public void onStart(InputManager.Pointer pointer) {
            mCameraOrigin = mCamera.getPosition();
            mPointer = pointer;
        }

        public void onEnd(InputManager.Pointer pointer) {
            mPointer = null;
        }

        public void update() {
            // TODO(wcauchois): If we implement zooming this will have to take that into account.
            if (mPointer != null) {
                mCamera.setPosition( mCameraOrigin.add(mPointer.getDelta().scale(1 / mSceneInfo.getSceneScale())) );
            }
        }
    }


    private class CameraZoomListener implements InputManager.MultiTouchListener {
        public void onStart(InputManager.Pointer[] pointers) {

        }

        public void onEnd(InputManager.Pointer[] pointers) {

        }

        public void update() {

        };
    }


    public CameraController(Camera camera, InputManager inputManager, SceneInfo sceneInfo) {
        mCamera = camera;
        mSceneInfo = sceneInfo;
        mScrollListener = new CameraScrollListener();
        inputManager.addDragListener(mScrollListener);
        mZoomListener = new CameraZoomListener();
        inputManager.addMultiTouchListener(mZoomListener);
    }

    public void update() {
        mScrollListener.update();
        mZoomListener.update();
    }
}
