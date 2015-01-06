package net.cloudhacking.androidgame2;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Right now this just represents some subsystem that needs to initialize resources
 * when the GameSurfaceView is resumed.
 *
 * Created by wcauchois on 1/4/15.
 */
public abstract class Component {

    // All components
    public static List<Component> sComponents = new ArrayList<Component>();


    protected boolean mResourcesPrepared=false;

    // ANYTHING that extends Component should call super() or else prepareResources will never be called.
    // Or not?  Just learned that if the super constructer has no-args it will automatically be called,
    // cool.
    public Component() {
        sComponents.add(this);
    }


    public void prepareResources(Context context) {}  // set mResourcesPrepared=true;

    public void checkResourcesPrepared(String TAG) {
        if (!mResourcesPrepared) {
            Log.e(TAG, "resources not prepared");
            throw new RuntimeException(TAG + ": resources not prepared");
        }
    }
}
