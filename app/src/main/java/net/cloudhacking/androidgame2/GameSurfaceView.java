package net.cloudhacking.androidgame2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by wcauchois on 1/3/15.
 */
public class GameSurfaceView extends GLSurfaceView {
    private static final String TAG = GameSurfaceView.class.getSimpleName();

    private GameSurfaceRenderer mRenderer;
    private final ConditionVariable mSyncObj = new ConditionVariable();


    public GameSurfaceView(Context context, GameState gameState) {
        super(context);

        setEGLContextClientVersion(2);

        mRenderer = new GameSurfaceRenderer(this, gameState);
        setRenderer(mRenderer);
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "Asking renderer to pause");
        mSyncObj.close();
        queueEvent(new Runnable() {
            @Override public void run() {
                mRenderer.onViewPause(mSyncObj);
            }
        });
        mSyncObj.block();
        Log.d(TAG, "Renderer pause complete");
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable() {
            @Override public void run() {
                mRenderer.handleTouchEvent(event);
            }
        });
        return true;
    }
}
