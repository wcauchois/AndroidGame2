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
