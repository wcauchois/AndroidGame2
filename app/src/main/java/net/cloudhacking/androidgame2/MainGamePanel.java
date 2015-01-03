package net.cloudhacking.androidgame2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

/**
 * Created by wcauchois on 12/31/14.
 */
public class MainGamePanel  extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = MainGamePanel.class.getSimpleName();

    private Thread thread;
    private GameLoop gameLoop;
    private GameServices services;

    public MainGamePanel(Context context) {
        super(context);
        getHolder().addCallback(this);

        gameLoop = new GameLoop(getHolder(), this);

        services = new GameServices(
                new GameObjectManager(),
                new ResourceManager(getResources()),
                new DisplayMetricsService(getContext())
        );

        setFocusable(true);
    }

    private void startGameLoop() {
        if (thread != null) {
            throw new RuntimeException("Game loop already started!");
        }
        thread = new Thread(gameLoop);
        gameLoop.setRunning(true);
        thread.start();
    }

    private void stopGameLoop() {
        gameLoop.setRunning(false);
        boolean retry = true;
        while(retry) {
            try {
                thread.join();
                thread = null;
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        services.getManager().draw(canvas);
    }

    private void gameInit() {
        services.getManager().addObject(new TurretCreator(services));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        new AsyncTask<Void, Void, Boolean>() {
            protected Boolean doInBackground(Void... params) {
                try {
                    MainGamePanel.this.services.getResourceManager().initialize();
                    return true;
                } catch(Exception e) {
                    Log.e(TAG, "Failed to initialize", e);
                    return false;
                }
            }

            protected void onPostExecute(Boolean result) {
                if (result) {
                    gameInit();
                    startGameLoop();
                } else {
                    Toast.makeText(MainGamePanel.this.getContext(),
                            "FAILED to initialize", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Destroying surface...");
        stopGameLoop();
    }

    public void simulate(double timeDelta) {
        services.getManager().simulate(timeDelta);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
        }
        services.getManager().handleTouchEvent(event);
        return true;
    }
}
