package net.cloudhacking.androidgame2.engine;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import net.cloudhacking.androidgame2.TestScene;
import net.cloudhacking.androidgame2.engine.gl.GLScript;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.LoggableActivity;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.TextureCache;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Andrew on 1/17/2015.
 */
public abstract class GameSkeleton extends LoggableActivity implements GLSurfaceView.Renderer, View.OnTouchListener {

    private GLSurfaceView mView;

    // keep static instance of GameSkeleton
    private static GameSkeleton sInstance;
    public static GameSkeleton getInstance() {
        return sInstance;
    }

    // get viewport rectangle
    private Rect mViewport;
    public Rect getViewport() {
        return mViewport;
    }

    // get instance of current scene
    private Scene mScene = null;
    private Class<? extends Scene> mSceneClass;
    public Scene getScene() {
        return mScene;
    }
    public <T extends Scene> void setSceneClass(Class<T> cls) {
        mSceneClass = cls;
    }

    // get input manager instance
    private InputManager mInputManager;
    public InputManager getInputManager() {
        return mInputManager;
    }

    // accumulated touch events
    private ArrayList<MotionEvent> mTouchEvents = new ArrayList<MotionEvent>();
    public ArrayList<MotionEvent> getTouchEvents() {
        return mTouchEvents;
    }

    // camera and camera controller
    private Camera mCamera;
    private CameraController mCameraController;


    private Bundle mSavedInstanceState;
    private boolean mInit = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mView = new GLSurfaceView( this );
        mView.setEGLContextClientVersion( 2 );
        mView.setEGLConfigChooser( false );
        mView.setRenderer( this );
        mView.setOnTouchListener( this );
        setContentView( mView );


        // init game assets

        sInstance = this;
        TextureCache.setContext(sInstance);

        GameTime.start();

        mInputManager = new InputManager();

        //mCameraController = new CameraController(mCamera, mInputManager);

        mSavedInstanceState = savedInstanceState;
    }

    abstract public void onCreateGame(Bundle savedInstanceState);



    @Override
    public void onPause() {
        super.onPause();

        onPauseGame();

        mView.onPause();
        GLScript.reset();
    }

    abstract public void onPauseGame();



    @Override
    public void onResume() {
        super.onResume();
        mView.onResume();
        GameTime.reset();

        onResumeGame();
    }

    abstract public void onResumeGame();



    @Override
    public void onDestroy() {
        super.onDestroy();

        onDestroyGame();
    }

    abstract public void onDestroyGame();



    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // All GL state, including shader programs, must be re-generated here.

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        GLES20.glEnable(GLES20.GL_BLEND);  // enable alpha blending
        GLES20.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);  // set alpha blending function

        GLES20.glEnable(GL10.GL_SCISSOR_TEST);  // enable scissor test

        TextureCache.reload();
        GLScript.use(BasicGLScript.class);
    }



    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {

        GLES20.glViewport(0, 0, width, height);
        mViewport = new Rect(0, 0, width, height);

        //mCameraController.reset();
    }


    @Override
    public void onDrawFrame(GL10 unused) {
        GameTime.tick();

        step();

        // Check if there is already an instance of this program, create one if there isn't.
        // The new instance is available through BasicGLScript.get(), we don't really need to do
        // this here though because we're only using one program right now, so we just need to
        // call it in onSurfaceCreated();
        //GLScript.use(BasicGLScript.class);
        BasicGLScript.get().uCamera.setValueM4(Camera.getMainCamera().getMatrix());

        GLES20.glScissor(0, 0, mViewport.width(), mViewport.height());
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        draw();
    }



    @Override
    public boolean onTouch(View unused, MotionEvent event) {
        synchronized (mTouchEvents) {
            mTouchEvents.add(MotionEvent.obtain(event));
        }
        return true;
    }



    private void step() {

        if (!mInit) {
            onCreateGame(mSavedInstanceState);
            mInit = true;
        }

        if (mScene == null) {
            try {
                mScene = mSceneClass.newInstance();
                mScene.create();
                mCamera = new Camera(new PointF(), mScene.getMapWidth(), mScene.getMapHeight(), 1);
                Camera.reset(mCamera);
            } catch(Exception e) {
                e("error creating new instance of scene: " + mSceneClass.getCanonicalName());
                e.printStackTrace();
            }
        }

        // TODO: why does this need to be synchronized?
        synchronized (mTouchEvents) {
            mInputManager.handleTouchEvents(mTouchEvents);
        }

        //mCameraController.update();
        mCamera.updateMatrix();
        mScene.update();
    }


    private void draw() {
        mScene.draw();
    }

}
