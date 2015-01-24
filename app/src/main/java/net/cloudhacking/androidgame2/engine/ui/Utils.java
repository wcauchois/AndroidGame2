package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.Camera;
import net.cloudhacking.androidgame2.engine.utils.Loggable;

/**
 * Created by Andrew on 1/23/2015.
 */
public class Utils extends Loggable {

    public static final float UI_SCALE = 1f;  // currently not used in code


    public static enum BindLocation {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER_LEFT,
        CENTER_TOP,
        CENTER_RIGHT,
        CENTER_BOTTOM,
        CENTER,
        NULL  // don't bind
    }


    public static class UICamera extends Camera {
        public UICamera() {
            super();
        }

        public void onSurfaceChange(int width, int height) {
            focusOn(Camera.getViewportCenter());
            Widget.setRootSize(width, height);
            super.update();
        }
    }


}
