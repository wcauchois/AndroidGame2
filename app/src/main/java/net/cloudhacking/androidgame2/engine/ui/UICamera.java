package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.gl.Camera;

/**
* Created by Andrew on 1/23/2015.
*/
public class UICamera extends Camera {

    public UICamera() {
        super();
    }

    public void onSurfaceChange(int width, int height) {
        focusOn( (float)width/2, (float)height/2 );
        // TODO: should implement some way to probably check ui size after surface change
        super.update();
    }

}
