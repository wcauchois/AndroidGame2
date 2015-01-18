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
import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.LoggableActivity;
import net.cloudhacking.androidgame2.engine.utils.TextureCache;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Andrew on 1/17/2015.
 */
public abstract class GameSkeleton extends LoggableActivity implements GLSurfaceView.Renderer, View.OnTouchListener {

    private GLSurfaceView mView;

    /**
     * Keep static instance of GameSkeleton
     */
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
    private Scene mScene;
    public Scene getScene() {
        return mScene;
    }

    // get GLScript instance for drawing
    private GLScript mGLScript;
    public GLScript getGLScript() {
        return mGLScript;
    }

    // get input manager instance
    private InputManager mInputManager;
    public InputManager getInputManager() {
        return mInputManager;
    }


    private Camera mCamera;
    private CameraController mCameraController;


    protected ArrayList<MotionEvent> mTouchEvents = new ArrayList<MotionEvent>(); // Accumulated touch events

    private static final float GAME_TIME_SCALE = 0.0001f;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        sInstance = this;
        TextureCache.setContext(sInstance);

        GameTime.setTimeScale(GAME_TIME_SCALE);
        GameTime.start();


        mInputManager = new InputManager();

        //mCamera = new Camera();
        //mCameraController = new CameraController(mCamera, mInputManager);
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
        GameTime.reset();

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

        TextureCache.reload();
        mGLScript = new BasicGLScript();

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

        GLES20.glScissor(0, 0, mViewport.width(), mViewport.height());
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT );
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
        // TODO: why does this need to be synchronized?
        synchronized (mTouchEvents) {
            mInputManager.handleTouchEvents(mTouchEvents);
        }

        //mCameraController.update();
        mScene.update();
    }


    private void draw() {
        mScene.draw();
    }

}
