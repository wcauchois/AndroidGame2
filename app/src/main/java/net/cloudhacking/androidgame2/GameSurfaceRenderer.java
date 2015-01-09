package net.cloudhacking.androidgame2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;
import java.util.TreeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameSurfaceRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = GameSurfaceRenderer.class.getSimpleName();

    private GameState mGameState;
    private GameSurfaceView mSurfaceView;

    private SceneInfo mSceneInfo;
    private int mViewportWidth;
    private int mViewportHeight;
    private Camera mCamera;
    private final float[] mProjectionMatrix = new float[16];
    // The projection matrix times the camera matrix.
    private final float[] mProjectionCameraMatrix = new float[16];
    private InputManager mInputManager;
    private CameraController mCameraController;


    /* TODO: Not sure if this is the best way to do this, but I made it so when you construct
     *       a component or render layer it will add itself to a static collection that is part of
     *       its respective class.  This way we don't have to worry about always adding components
     *       to a collection in THIS class.
     *
     *       These collections get emptied when onSurfaceCreated() is called.
     */
    private List<Component> mComponents = Component.getComponents();
    private TreeSet<RenderLayer> mRenderLayers = RenderLayer.getsRenderLayers();


    public GameSurfaceRenderer(GameSurfaceView surfaceView, GameState gameState) {
        mSurfaceView = surfaceView;
        mGameState = gameState;
        mInputManager = new InputManager();
        mCamera = new Camera();
        mCameraController = new CameraController(mCamera, mInputManager);

        // Use this instance of SceneInfo for all render layers, since it contains all the info
        // about our viewport and provides access to the projection matrix.
        mSceneInfo = new SceneInfo() {
            public float[] getProjectionCamera() { return mProjectionCameraMatrix; }
            public int getViewportWidth()  { return mViewportWidth; }
            public int getViewportHeight() { return mViewportHeight; }
        };

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

        // TODO: the way we load resources might need to be reworked?  There is a weird lag, but I'm
        //       not sure if its because of logging or my phone or what.  Check dis tho;
        //          http://www.curious-creature.com/2008/12/18/avoid-memory-leaks-on-android/comment-page-1/
        //
        //       Also look at the logs at all the "... D/dalvikvm﹕ GC_FOR_ALLOC freed ...", not sure about it.

        /*
         * Allocate all components and render layers here before component resources are loaded.
         */
        mGameState.setUpGameLevel(mSceneInfo);

        // prepare resources for all components here
        // (mComponents points to static array list of components in Component)
        Context context = mSurfaceView.getContext();
        for (Component c : mComponents) {
            c.prepareResources(context);
        }

    }


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Sets up viewport so that it is centered on the screen and the aspect ratio of
        // the game arena is maintained.

        mGameState.updateArenaSize();  // do this before getting arena width/height

        float arenaRatio = ((float)mGameState.getArenaHeight()) / mGameState.getArenaWidth();
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

        Log.d(TAG, "onSurfaceChanged (width=" + width + ", height=" + height+")");
        Log.d(TAG, " --> xOffset=" + x + ", yOffset=" + y);
        Log.d(TAG, " --> viewPortWidth=" + viewWidth + ", viewPortHeight=" + viewHeight);

        GLES20.glViewport(x, y, viewWidth, viewHeight);
        mViewportWidth = viewWidth;
        mViewportHeight = viewWidth;

        Matrix.orthoM(mProjectionMatrix, 0, 0, mGameState.getArenaWidth(),
                mGameState.getArenaHeight(), 0, -1, 1);
    }


    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.multiplyMM(mProjectionCameraMatrix, 0, mProjectionMatrix, 0, mCamera.getMatrix(), 0);

        // do all updating in game state
        mGameState.update();

        // mRenderLayers points to static array list of render layers in RenderLayer
        for (RenderLayer r : mRenderLayers) {
            r.draw();
        }
    }


    public void onViewPause(ConditionVariable syncObj) {
        // Save game state here, use condition var to signal when done.
        syncObj.open();
    }

    public void handleTouchEvent(MotionEvent event) {
        mInputManager.handleTouchEvent(event);
    }
}
