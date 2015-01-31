package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.Signal;

/**
 * Created by wcauchois on 1/21/15.
 */
public class RootWidget extends Widget implements Signal.Listener<InputManager.ClickEvent> {

    public RootWidget() {
        // set rect to be 100% width and 100% height
        super(new RectF(0, 0, Widget.getRootWidth(), Widget.getRootHeight()));
    }

    @Override
    public boolean onSignal(InputManager.ClickEvent event) {
        PointF pt = event.getPos();
        PointF relPt = new PointF( pt.x/Widget.getRootWidth(), pt.y/Widget.getRootHeight() );

        for (Widget w : mEntities) {
            if (w instanceof TouchWidget) {
                if (((TouchWidget)w).handleClick(relPt, new RectF(0, 0, 1, 1))) {
                    return true;
                }
            }
        }
        return false;
    }

}
