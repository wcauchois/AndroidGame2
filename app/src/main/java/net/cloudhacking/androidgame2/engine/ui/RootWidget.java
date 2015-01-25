package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Signal;

/**
 * Created by wcauchois on 1/21/15.
 */
public class RootWidget extends Widget {

    public RootWidget(InputManager inputManager) {
        // set rect to be 100% width and 100% height
        super(new RectF(0, 0, Widget.getRootWidth(), Widget.getRootHeight()));

        inputManager.click.connect(new Signal.Listener<InputManager.ClickEvent>() {
            @Override
            public boolean onSignal(InputManager.ClickEvent clickEvent) {
                return handleSignal(clickEvent);
            }
        });
    }

    public boolean handleSignal(InputManager.ClickEvent event) {
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
