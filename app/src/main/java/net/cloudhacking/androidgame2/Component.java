package net.cloudhacking.androidgame2;

import android.content.Context;

/**
 * Right now this just represents some subsystem that needs to initialize resources
 * when the GameSurfaceView is resumed.
 *
 * Created by wcauchois on 1/4/15.
 */
public abstract class Component {
    public void prepareResources(Context context) {}
}
