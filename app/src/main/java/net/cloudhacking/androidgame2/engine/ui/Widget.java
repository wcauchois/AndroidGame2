package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.foundation.Group;
import net.cloudhacking.androidgame2.engine.foundation.Image;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/21/15.
 */
public abstract class Widget extends Group<Widget> {
    protected RectF mBounds;
    private Image mWidgetBackgroundImage;

    public RectF getBounds() {
        return mBounds;
    }

    public Vec2 getTopLeft() {
        return new Vec2(mBounds.left, mBounds.top);
    }

    public Widget(RectF bounds) {
        mBounds = bounds;
        mWidgetBackgroundImage = null;
    }

    public void onClick(Vec2 pos) {
        /*for (Widget child : mEntities) {
            if (pos.containedBy(child.getBounds())) {
                child.onClick(pos.subtract(child.getTopLeft()));
            }
        }*/
    }


    // below added by Andrew

    public void setBounds(RectF bounds) {
        mBounds = bounds;
        if (mWidgetBackgroundImage != null) mWidgetBackgroundImage.setToRect(bounds);
    }

    public void setBackgroundImage(Image image) {
        mWidgetBackgroundImage = image;
        mWidgetBackgroundImage.setToRect(mBounds);
    }

    public void setBackgroundImage(Asset imgAsset) {
        setBackgroundImage( new Image(imgAsset) );
    }


    @Override
    public void update() {
        if (mWidgetBackgroundImage != null) mWidgetBackgroundImage.update();
        super.update();
    }

    @Override
    public void draw(BasicGLScript gls) {
        if (mWidgetBackgroundImage != null) mWidgetBackgroundImage.draw(gls);
        super.draw(gls);
    }
}
