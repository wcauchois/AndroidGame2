package net.cloudhacking.androidgame2;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Andrew on 1/13/2015.
 */
public class TDGame extends Activity implements GLSurfaceView.Renderer, View.OnTouchListener {
    private static final String TAG = "TDGame";

    private GLSurfaceView mView;

    private SceneInfo mSceneInfo;
    private int mViewportWidth, mViewportHeight;

    private GameState mGameState;
    private InputManager mInputManager;

    private Camera mCamera;
    private CameraController mCameraController;

    private List<Component> mComponents = Component.getComponents();
    private TreeSet<RenderLayer> mRenderLayers = RenderLayer.getsRenderLayers();

    protected ArrayList<MotionEvent> mTouchEvents = new ArrayList<MotionEvent>(); // Accumulated touch events


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Use this instance of SceneInfo for all render layers, since it contains all the info
        // about our viewport and provides access to the projection matrix.
        mSceneInfo = new SceneInfo() {
            private final float[] mProjectionMatrix = new float[16];
            private final float[] mProjectionCameraMatrix = new float[16];  // The projection matrix times the camera matrix.
            private float mSceneScale = 1f;

            public float getSceneScale() { return mSceneScale; }
            public float[] getProjection() { return mProjectionMatrix; }
            public float[] getProjectionCamera() { return mProjectionCameraMatrix; }
            public int getViewportWidth()  { return mViewportWidth; }
            public int getViewportHeight() { return mViewportHeight; }

            public void update() {
                Matrix.multiplyMM(mProjectionCameraMatrix, 0, mProjectionMatrix, 0, mCamera.getMatrix(), 0);
            }

            public void reset() {
                mSceneScale = mGameState.getArenaWidth() / mViewportWidth;  // should == mGameState.getArenaHeight() / mViewportHeight ... although this should be temporary.
                Matrix.orthoM(mProjectionMatrix, 0, 0, mGameState.getArenaWidth(),
                        mGameState.getArenaHeight(), 0, -1, 1);
            }
        };

        mInputManager = new InputManager();

        mGameState = new GameState();
        mGameState.setUpGameLevel(mSceneInfo);

        mCamera = new Camera();
        mCameraController = new CameraController(mCamera, mInputManager, mSceneInfo);

        mView = new GLSurfaceView( this );
        mView.setEGLContextClientVersion( 2 );
        mView.setEGLConfigChooser( false );
        mView.setRenderer( this );
        mView.setOnTouchListener( this );
        setContentView( mView );
    }

    @Override
    public void onPause() {
        super.onPause();

        // pause game state here

        mView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // resume game state here

        mView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // destroy game state here
    }


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // All GL state, including shader programs, must be re-generated here.

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);  // enable alpha blending
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        // clear static collections
        Component.clearComponents();
        RenderLayer.clearRenderLayers();
        LevelGrid.clearGridItems();

        TextureUtils.reset();

        // Allocate all components and render layers here before component resources are loaded.
        mGameState.setUpGameLevel(mSceneInfo);

        // prepare resources for all components
        for (Component c : mComponents) {
            c.prepareResources(this);
        }

    }


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Sets up viewport so that it is centered on the screen and the aspect ratio of
        // the game arena is maintained.

        mGameState.updateArenaSize();  // do this before getting arena width/height

        float arenaRatio = mGameState.getArenaHeight() / mGameState.getArenaWidth();
        int x, y, viewWidth, viewHeight;

        if (height > (int) (width * arenaRatio)) {
            // limited by narrow width; restrict height
            viewWidth = width;
            viewHeight = (int) (width * arenaRatio);
        } else {
            // limited by short height; restrict width
            viewHeight = height;
            viewWidth = (int) (height / arenaRatio);
        }
        x = (width - viewWidth) / 2;
        y = (height - viewHeight) / 2;

        /*Log.d(TAG, "onSurfaceChanged (width=" + width + ", height=" + height+")");
        Log.d(TAG, " --> xOffset=" + x + ", yOffset=" + y);
        Log.d(TAG, " --> viewPortWidth=" + viewWidth + ", viewPortHeight=" + viewHeight);*/

        GLES20.glViewport(x, y, viewWidth, viewHeight);
        mViewportWidth = viewWidth;
        mViewportHeight = viewWidth;

        mSceneInfo.reset();
    }


    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        update();
    }


    @Override
    public boolean onTouch(View unused, MotionEvent event) {
        synchronized (mTouchEvents) {
            mTouchEvents.add(MotionEvent.obtain(event));
        }
        return true;
    }


    private void update() {

        synchronized (mTouchEvents) {  // TODO: why does this need to be synchronized?
            mInputManager.handleTouchEvents(mTouchEvents);
            mTouchEvents.clear();
        }

        // update state
        mGameState.update();
        mSceneInfo.update();
        mCameraController.update();

        // draw
        for (RenderLayer r : mRenderLayers) {
            r.draw();
        }

    }
}
