package net.cloudhacking.androidgame2.engine.utils;

import android.util.Log;

/**
 * Created by Andrew on 1/15/2015.
 *
 * Extend this in order to get access to the TAG variable
 *
 */
public class Loggable {

    // ex: Log.d(TAG, "debug message");
    protected String TAG;

    public Loggable() {
        TAG = this.getClass().getSimpleName();
    }

}
