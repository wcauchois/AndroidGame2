package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/25/2015.
 */
public class TouchWidget extends Widget {

    public TouchWidget(WidgetBackground bg, RectF bounds, BindLocation loc) {
        super(bg, loc, bounds);
    }

    public TouchWidget(RectF bounds, BindLocation loc) {
        super(null, loc, bounds);
    }

    public TouchWidget(RectF bounds) {
        super(null, BindLocation.NULL, bounds);
    }


    public boolean handleClick(PointF touch, RectF parentBounds) {

        if (mBounds.contains(touch.x, touch.y)) {
            onClick();

            // touch point relative to the bounding box for this widget
            PointF relPt =
                    new PointF( (touch.x - mBounds.left)*(parentBounds.width()/mBounds.width()),
                                (touch.y - mBounds.top)*(parentBounds.height()/mBounds.height())
            );

            TouchWidget t;
            for (Widget w : mEntities) {

                if (w instanceof TouchWidget) {
                    t = (TouchWidget) w;

                    if (t.handleClick(relPt, mBounds)) {
                        t.onClick();
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void onClick() {}

}
