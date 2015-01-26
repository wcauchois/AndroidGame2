package net.cloudhacking.androidgame2.engine.utils;

/**
 * Created by Andrew on 1/26/2015.
 */
public class FPSCounter extends Loggable {

    long mStart;
    int mFrames;

    public void start() {
        mStart = System.nanoTime();
        mFrames = 0;
    }

    public void logFrame() {
        mFrames++;

        if (System.nanoTime() - mStart > 1000000000) {
            d("fps: "+mFrames);
            start();
        }
    }

}
