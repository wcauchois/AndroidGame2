package net.cloudhacking.androidgame2.engine.ui.widget;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.NinePatch;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.ui.UITouchHandler.WidgetController;
import net.cloudhacking.androidgame2.engine.utils.CommonUtils;
import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by wcauchois on 1/21/15.
 */
public abstract class Widget extends Group<Widget> {

    protected RectF mBounds;
    private boolean mTouchable;
    // TODO: private boolean mDraggable;
    private WidgetController mController;
    private WidgetBackground mBackground;
    private boolean mFitToBG;
    private boolean mNeedUpdate;

    private static RectF moveRect(float x, float y, RectF orig) {
        return new RectF(x, y, x+orig.width(), y+orig.height());
    }


    public Widget(RectF bounds, WidgetBackground bg, boolean fitWidgetToBackground) {
        mBounds = bounds;
        mBackground = bg;
        mFitToBG = fitWidgetToBackground;
        mNeedUpdate = true;
        mTouchable = false;
        mController = null;
    }

    public Widget(float x, float y, WidgetBackground bg) {
        this(moveRect(x, y, bg.getRect()), bg, true);
    }

    public Widget(RectF bounds, NinePatchAsset asset) {
        this(bounds, new NinePatch(asset), false);
    }

    public Widget(RectF bounds) {
        this(bounds, null, false);
    }


    public void setBounds(RectF bounds) {
        mBounds = bounds;
        mNeedUpdate = true;
    }

    public void setPos(float x, float y) {
        moveRect(x, y, mBounds);
        mNeedUpdate = true;
    }

    public void setPos(PointF pos) {
        setPos(pos.x, pos.y);
    }

    public void setWidth(float width) {
        mBounds.right = mBounds.left + width;
        mNeedUpdate = true;
    }

    public float getWidth() {
        return mBounds.width();
    }

    public void setHeight(float height) {
        mBounds.bottom = mBounds.top + height;
        mNeedUpdate = true;
    }

    public float getHeight() {
        return mBounds.height();
    }

    public RectF getBounds() {
        return mBounds;
    }

    public void setBackgroundImage(WidgetBackground bg) {
        mBackground = bg;
        mNeedUpdate = true;
    }

    public WidgetBackground getBackground() {
        return mBackground;
    }

    public void setTouchable(boolean bool) {
        mTouchable = bool;
    }

    public boolean isTouchable() {
        return mTouchable;
    }

    public void hide() {
        setVisibility(false);
        setInactive();
    }

    public void show() {
        setVisibility(true);
        setActive();
    }

    public void setScale(float scale) {
        setWidth(scale * mBounds.width());
        setHeight(scale * mBounds.height());
        mBackground.setScale(scale);
    }


    public boolean containsPt(PointF p) {
        return mBounds.contains(p.x, p.y);
    }


    //----------------------------------------------------------------------------------------------

    public Widget getWidgetOnTouch(PointF touch) {
        for (Widget w : mEntities) {
            if (w.isTouchable() && w.containsPt(touch)) {
                return w.getWidgetOnTouch(touch);
            }
        }
        return this;
    }

    public void setController(WidgetController controller) {
        mController = controller;
    }

    // called from UITouchHandler when this widget is selected
    public WidgetController getController() {
        return mController;
    }


    @Override
    public void update() {
        if (mNeedUpdate) {
            if (mBackground != null) {
                if (mFitToBG) {
                    RectF bgRect = mBackground.getRect();
                    mBounds = new RectF(mBounds.left,
                            mBounds.top,
                            mBounds.left+bgRect.width(),
                            mBounds.top+bgRect.height()
                    );
                    mBackground.setToRect(mBounds);
                } else {
                    mBackground.setToRect(mBounds);
                }
                mBackground.update();
            }
            mNeedUpdate = false;
        }
        super.update();
    }

    @Override
    public void draw(BasicGLScript gls) {
        if (mBackground != null) mBackground.draw(gls);
        super.draw(gls);
    }


}
