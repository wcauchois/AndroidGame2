package net.cloudhacking.androidgame2;

import android.util.Log;

/**
 * Created by wcauchois on 1/8/15.
 */
public class CameraController {
    private Camera mCamera;
    private SceneInfo mSceneInfo;
    private Vec2 mAspectRatioVec;

    private class CameraScrollListener implements InputManager.DragListener {
        private InputManager.Pointer mPointer = null;
        private Vec2 mCameraOriginalPos;

        public void onStart(InputManager.Pointer pointer) {
            mCameraOriginalPos = mCamera.getPosition();
            mPointer = pointer;
        }

        public void onEnd(InputManager.Pointer pointer) {
            mPointer = null;
        }

        public void update() {
            // TODO(wcauchois): If we implement zooming this will have to take that into account.
            if (mPointer != null) {
                mCamera.incPosition(mPointer.getDelta().scale( 1/mSceneInfo.getSceneScale() ));
            }
        }
    }


    private class CameraZoomListener implements InputManager.MultiTouchListener {
        private InputManager.MultiTouch mMultiTouch = null;
        private float mDistOriginal;

        public void onStart(InputManager.MultiTouch multiTouch) {
            mMultiTouch = multiTouch;
            // original distance between the two fingers
            mDistOriginal = mMultiTouch.getDistBtwn();
        }

        public void onEnd(InputManager.MultiTouch multiTouch) {
            mMultiTouch = null;
            mCamera.bindLastZoom();
            mCamera.bindTempOffset();
        }

        public void update() {
            if (mMultiTouch != null) {
                float dst = mMultiTouch.getDistBtwn();
                float zoom =  dst / mDistOriginal;
                mCamera.setRelativeZoom(zoom);
                mCamera.setTempOffset( mAspectRatioVec.negate().scale( (dst - mDistOriginal)/2 ) );
                // this doesn't work exactly as intended.  It seems to only
            }
        }
    }


    public CameraController(Camera camera, InputManager inputManager, SceneInfo sceneInfo) {
        mCamera = camera;
        mSceneInfo = sceneInfo;

        inputManager.addDragListener( new CameraScrollListener() );
        inputManager.addMultiTouchListener( new CameraZoomListener() );
    }

    public void reset() {
        mCamera.updateMatrix();
        mAspectRatioVec = new Vec2(mSceneInfo.getViewportWidth(),
                mSceneInfo.getViewportHeight()).normalize();
    }

    public void update() {
        mCamera.updateMatrix();
    }
}
