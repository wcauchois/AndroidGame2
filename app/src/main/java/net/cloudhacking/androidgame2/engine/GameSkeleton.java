package net.cloudhacking.androidgame2.engine;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.LoggableActivity;
import net.cloudhacking.androidgame2.engine.utils.TextureCache;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Andrew on 1/17/2015.
 */
public abstract class GameSkeleton

        extends LoggableActivity
        implements GLSurfaceView.Renderer
{

    private static GameSkeleton sInstance;  // keep static instance of GameSkeleton
    private static boolean sInitGame = true;

    private Bundle mSavedInstanceState;
    private GLSurfaceView mView;
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

    abstract public void onGameInit(Bundle savedInstanceState);

    abstract public void onPauseGame();

    abstract public void onResumeGame();

    abstract public void onDestroyGame();

    //abstract public void onSaveGame(Bundle outState);



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mView = new GLSurfaceView( this );
        mView.setEGLContextClientVersion(2);
        mView.setEGLConfigChooser(false);
        mView.setRenderer(this);
        setContentView( mView );


        sInstance = this;
        mSavedInstanceState = savedInstanceState;

        mScene = null;

        TextureCache.setContext(this);
        GameTime.start();

        mInputManager = new InputManager(this);
        mView.setOnTouchListener(mInputManager);

        mCameraController = new CameraController(mInputManager);

        d("activity created");
    }


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // All GL state, including shader programs, must be re-generated here.

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glEnable(GL10.GL_SCISSOR_TEST);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        GLES20.glEnable(GLES20.GL_BLEND);  // enable alpha blending
        // For pre-multiplied alpha:
        // GLES20.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  // set alpha blending function

        TextureCache.reload();
        mGLScript = new BasicGLScript();

        d("surface created");
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        Camera.setView(width, height);
        mViewport = new Rect(0, 0, width, height);

        d("surface changed: width=" + width + ", height=" + height);
    }


    @Override
    public void onPause() {
        super.onPause();
        onPauseGame();
        mView.onPause();
        mGLScript.delete();

        d("game paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
        GameTime.start();
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
        sInstance = null;
        sInitGame = true;

        d("game destroyed");
    }


    @Override
    public void onDrawFrame(GL10 unused) {
        GameTime.tick();

        if (sInitGame) {

            onGameInit(mSavedInstanceState);
            mSavedInstanceState = null;

            sInitGame = false;

        } else if (mScene != null) {

            mInputManager.processEvents();
            mCameraController.update();
            mScene.update();

            mGLScript.clearLastCamera();
            GLES20.glScissor(0, 0, mViewport.width(), mViewport.height());
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            mScene.draw(mGLScript);
        }
    }

}
