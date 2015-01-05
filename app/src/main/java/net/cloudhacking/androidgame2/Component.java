package net.cloudhacking.androidgame2;

import android.content.Context;

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

    // ANYTHING that extends Component should call super() or else prepareResources will never be called.
    public Component() {
        sComponents.add(this);
    }


    public void prepareResources(Context context) {}
}
