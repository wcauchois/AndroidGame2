package net.cloudhacking.androidgame2.engine.utils;

import android.util.Log;

/**
 * Created by Andrew on 1/15/2015.
 *
 * Provides access to the TAG variable
 */
public class Loggable {

    protected static final String TAG = "Utils";  // for static references

    // ex: Log.d(TAG, "debug message");
    protected String _TAG;

    public Loggable() {
        _TAG = this.getClass().getCanonicalName();
    }

    public void v(String msg) {
        Log.v(_TAG, msg);
    }

    public void d(String msg) {
        Log.d(_TAG, msg);
    }

    public void i(String msg) {
        Log.i(_TAG, msg);
    }

    public void w(String msg) {
        Log.w(_TAG, msg);
    }

    public void e(String msg) {
        Log.e(_TAG, msg);
    }
}