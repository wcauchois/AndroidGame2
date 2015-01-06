package net.cloudhacking.androidgame2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.ConditionVariable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameSurfaceRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = GameSurfaceRenderer.class.getSimpleName();

    private GameState mGameState;
    private GameSurfaceView mSurfaceView;
    private Context mContext;

    private SceneInfo mSceneInfo;
    private int mViewportWidth;
    private int mViewportHeight;
    private final float[] mProjectionMatrix = new float[16];

    private GameLevel mLevel;

    /* TODO: Not sure if this is the best way to do this, but I made it so when you construct
     *       a component or render layer it will add itself to a static arraylist.
     */
    // mComponents -> Component.sComponents
    // mRenderLayers -> RenderLayer.sRenderLayers


    public GameSurfaceRenderer(GameSurfaceView surfaceView, Context context, GameState gameState) {
        mSurfaceView = surfaceView;
        mGameState = gameState;
        mContext = context;

        // Use this instance of SceneInfo for all render layers.
        mSceneInfo = new SceneInfo() {
            public float[] getProjection() { return mProjectionMatrix; }
            public int getViewportWidth()  { return mViewportWidth; }
            public int getViewportHeight() { return mViewportHeight; }
        };

        mLevel = new GameLevel(mSceneInfo);
    }


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // All GL state, including shader programs, must be re-generated here.

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);

        for (Component comp : Component.sComponents) {
            comp.prepareResources(mContext);
        }

        // Set arena size to size of GameLevel (must be done after GameLevel resources
        // are prepared).  Projection matrix is based on the arena size, so this ensures that
        // the game level will be nice and centered on the screen.
        //
        // However in the future, aspect ratio of the screen should not matter, so we need to make
        // sure that we have some sort of a camera that can handle any aspect ratio.
        mGameState.setArenaSize(mLevel.getLevelSize());

        Log.d(TAG, "Components: "+Component.sComponents);
        Log.d(TAG, "Render Layers: "+RenderLayer.sRenderLayers);
    }


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Sets up viewport so that it is centered on the screen and the aspect ratio of
        // the game arena is maintained.

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

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

        mLevel.update();

        for (RenderLayer r : RenderLayer.sRenderLayers) {
            r.draw();
        }

        GLES20.glDisable(GLES20.GL_BLEND);
    }


    public void onViewPause(ConditionVariable syncObj) {
        // Save game state here, use condition var to signal when done.
        syncObj.open();
    }
}
