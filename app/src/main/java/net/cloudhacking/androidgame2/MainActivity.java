package net.cloudhacking.androidgame2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private GameSurfaceView mSurfaceView;
    private GameState mGameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Should initialize data that depends on android resources here.
        mGameState = new GameState();
        mSurfaceView = new GameSurfaceView(this, mGameState);
        setContentView(mSurfaceView);
        Log.d(TAG, "Surface view added");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "MainActivity pausing");
        super.onPause();
        mSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "MainActivity resuming");
        super.onResume();
        mSurfaceView.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "MainActivity destroying");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "MainActivity stopping");
        super.onStop();
    }
}
