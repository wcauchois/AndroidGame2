package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/25/2015.
 */
public class TouchWidget extends Widget {

    public TouchWidget(RectF bounds, BindLocation loc) {
        super(bounds, loc);
    }

    public TouchWidget(RectF bounds) {
        super(bounds);
    }

    public TouchWidget() {
        super(new RectF(0,0,1,1), BindLocation.NULL);
    }


    public boolean handleClick(PointF touch, RectF parentBounds) {

        if (mBounds.contains(touch.x, touch.y)) {

            // touch point relative to the bounding box for this widget
            PointF relPt =
                    new PointF( (touch.x - mBounds.left)*(parentBounds.width()/mBounds.width()),
                                (touch.y - mBounds.top)*(parentBounds.height()/mBounds.width())
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
