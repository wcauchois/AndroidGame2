package net.cloudhacking.androidgame2;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wcauchois on 12/31/14.
 */
public class GameLoop implements Runnable {
    private static final String TAG = GameLoop.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;

    private AtomicBoolean running = new AtomicBoolean(false);
    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public GameLoop(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Starting game loop");
        boolean firstSim = true;
        double lastSim = 0.0;
        while(running.get()) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (running.get()) {
                        this.gamePanel.onDraw(canvas);

                        double currentTime = System.currentTimeMillis();
                        double timeDelta = firstSim ? 0.0 : (currentTime - lastSim);
                        lastSim = currentTime;
                        firstSim = false;
                        this.gamePanel.simulate(timeDelta);
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
