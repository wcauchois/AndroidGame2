package net.cloudhacking.androidgame2.engine.utils;

import android.app.Activity;
import android.util.Log;

/**
 * Created by Andrew on 1/17/2015.
 */
public class LoggableActivity extends Activity {

    protected static final String TAG = "MainActivity";  // for static references

    
    protected String _TAG;

    public LoggableActivity() {
        super();
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

}
