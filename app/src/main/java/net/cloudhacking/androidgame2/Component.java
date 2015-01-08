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

    /*
     * This is a static collection which contains all the components.
     */
    public static List<Component> sComponents = new ArrayList<Component>();

    public static List<Component> getComponents() {
        return sComponents;
    }

    public static void clearComponents() {
        sComponents.clear();
    }


    protected boolean mResourcesPrepared=false;

    // Make sure this constructor is always called so that the component is added to sComponents,
    // although im pretty that constructors with no args are always called in Java?
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
