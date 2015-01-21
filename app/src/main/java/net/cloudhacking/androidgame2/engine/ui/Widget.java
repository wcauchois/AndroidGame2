package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.foundation.Group;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/21/15.
 */
public abstract class Widget extends Group<Widget> {
    protected RectF mBounds;

    public RectF getBounds() {
        return mBounds;
    }

    public Vec2 getTopLeft() {
        return new Vec2(mBounds.left, mBounds.top);
    }

    public Widget(RectF bounds) {
        mBounds = bounds;
    }

    public void onClick(Vec2 pos) {
        /*for (Widget child : mEntities) {
            if (pos.containedBy(child.getBounds())) {
                child.onClick(pos.subtract(child.getTopLeft()));
            }
        }*/
    }
}
