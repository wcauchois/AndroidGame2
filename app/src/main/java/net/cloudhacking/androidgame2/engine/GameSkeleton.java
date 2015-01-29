package net.cloudhacking.androidgame2.engine;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.Camera;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.LoggableActivity;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Andrew on 1/17/2015.
 */
public abstract class GameSkeleton

        extends LoggableActivity
        implements GLSurfaceView.Renderer
{
    /**
     * This is the basic game renderer loop.
     *
     * Once the scene is set, using setScene(scene), it will automatically be created and
     * the render loop will be started.
     */

    private static GameSkeleton sInstance;  // keep static instance of GameSkeleton
    private static boolean sInitGame;

    private Bundle mSavedInstanceState;
    private GLSurfaceView mView;
    private float mScreenDensity;
    private Rect mViewport;
    private BasicGLScript mGLScript;
    private Scene mScene;
    private InputManager mInputManager;
    private CameraController mCameraController;


    public static GameSkeleton getInstance() {
        return sInstance;
    }

    public BasicGLScript getGLScript() {
        return mGLScript;
    }

    public Rect getViewport() {
        return mViewport;
    }

    public float getScreenDensity() {
        return mScreenDensity;
    }

    public Scene getScene() {
        return mScene;
    }

    public void setScene(Scene scene) {
        mScene = scene;
    }

    public InputManager getInputManager() {
        return mInputManager;
    }

    public CameraController getCameraController() {
        return mCameraController;
    }

    abstract public Scene onInitGame(Bundle savedInstanceState);

    abstract public void onPauseGame();

    abstract public void onResumeGame();

    abstract public void onDestroyGame();

    abstract public void onSaveGame(Bundle outState);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics m = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(m);
        mScreenDensity = m.density;

        mView = new GLSurfaceView(this);
        mView.setEGLContextClientVersion(2);
        mView.setEGLConfigChooser(false);
        mView.setRenderer(this);
        setContentView(mView);

        mGLScript = null;

        sInstance = this;
        sInitGame = true;
        mSavedInstanceState = savedInstanceState;

        mScene = null;

        mInputManager = new InputManager(this);
        mView.setOnTouchListener(mInputManager);

        mCameraController = new CameraController(mInputManager);

        d("activity created");
    }


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // All GL state, including shader programs, must be re-generated here.

        GLES20.glClearColor(0, 0, 0, 1);
        GLES20.glEnable(GL10.GL_SCISSOR_TEST);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        GLES20.glEnable(GLES20.GL_BLEND);  // enable alpha blending
        // For pre-multiplied alpha:
        // GLES20.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  // set alpha blending function

        mGLScript = new BasicGLScript();
        mGLScript.use();
        AssetCache.getInstance().reloadTextures();
        TextRenderer.getInstance().reloadTextures();

        d("surface created");
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Camera.setView(width, height);

        // update UI camera matrix for new viewport
        mCameraController.getUICamera().onSurfaceChange(width, height);

        mViewport = new Rect(0, 0, width, height);

        d("surface changed: width=" + width + ", height=" + height);
    }


    @Override
    public void onPause() {
        super.onPause();
        onPauseGame();
        //if (mGLScript != null) mGLScript.delete();
        mView.onPause();

        d("game paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
        GameTime.reset();
        onResumeGame();

        d("game resumed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroyGame();
        if (mScene != null) {
            mScene.destroy();
            mScene = null;
        }
        AssetCache.clearInstance();
        TextRenderer.clearInstance();
        sInstance = null;

        d("game destroyed");
    }


    @Override
    public void onDrawFrame(GL10 unused) {
        GameTime.tick();

        if (sInitGame) {
            d("initializing game");

            mScene = onInitGame(mSavedInstanceState);
            mSavedInstanceState = null;

            sInitGame = false;
        }

        if (mScene != null) {

            if (!mScene.isCreated()) mScene.start();

            mInputManager.processEvents();
            mCameraController.update();
            mScene.update();

            mGLScript.clearLastCamera();
            GLES20.glScissor(0, 0, mViewport.width(), mViewport.height());
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            mScene.draw(mGLScript);
        }
    }


    // TODO: scene switching functionality

}
