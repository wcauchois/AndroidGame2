package net.cloudhacking.androidgame2.engine.utils;

import android.util.Log;

/**
 * Created by Andrew on 1/15/2015.
 *
 * Provides access to the TAG variable
 */
public class Loggable {

    // ex: Log.d(TAG, "message");
    protected static final String TAG = "Utils";  // for static references


    protected String _TAG;

    public Loggable() {
        _TAG = this.getClass().getSimpleName();
    }

    protected void v(String msg) {
        Log.v(_TAG, msg);
    }

    protected void d(String msg) {
        Log.d(_TAG, msg);
    }

    protected void i(String msg) {
        Log.i(_TAG, msg);
    }

    protected void w(String msg) {
        Log.w(_TAG, msg);
    }

    protected void e(String msg) {
        Log.e(_TAG, msg);
    }

    protected void e(String msg, Throwable t) {
        Log.e(_TAG, msg, t);
    }
}
