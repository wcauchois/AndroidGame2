package net.cloudhacking.androidgame2;

/**
 * Created by wcauchois on 1/8/15.
 */
public class CameraController {
    private Camera mCamera;
    private SceneInfo mSceneInfo;
    private CameraScrollListener mListener;

    private class CameraScrollListener implements InputManager.DragListener {
        private InputManager.Pointer mPointer;
        private Vec2 mCameraOrigin;

        public void onStartDrag(InputManager.Pointer pointer) {
            mCameraOrigin = mCamera.getPosition();
            mPointer = pointer;
        }

        public void onEndDrag(InputManager.Pointer pointer) {
            mPointer = null;
        }

        public void update() {
            // TODO(wcauchois): If we implement zooming this will have to take that into account.
            if (mPointer != null) {
                mCamera.setPosition( mCameraOrigin.add( mPointer.getDelta().scale( 1/mSceneInfo.getSceneScale() ) ) );
            }
        }
    }

    public CameraController(Camera camera, InputManager inputManager, SceneInfo sceneInfo) {
        mCamera = camera;
        mSceneInfo = sceneInfo;
        mListener = new CameraScrollListener();
        inputManager.addDragListener(mListener);
    }

    public void update() {
        mListener.update();
    }
}
