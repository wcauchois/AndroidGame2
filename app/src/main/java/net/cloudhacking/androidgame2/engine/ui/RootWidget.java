package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.InputManager;

/**
 * Created by wcauchois on 1/21/15.
 */
public class RootWidget extends Widget {

    public RootWidget(InputManager inputManager) {
        // set rect to be 100% width and 100% height
        super(new RectF(0, 0, Widget.getRootWidth(), Widget.getRootHeight()));
    }

}
