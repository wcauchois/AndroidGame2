package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by wcauchois on 1/21/15.
 */
public abstract class Widget extends Group<Widget> {

    protected RectF mBounds;
    private WidgetBackground mBackground;
    private boolean mFitToBG;
    private boolean mNeedUpdate;


    public Widget(RectF bounds, WidgetBackground bg) {
        mBounds = bounds;
        mBackground = bg;
        mFitToBG = false;
        mNeedUpdate = true;
    }

    public Widget(float x, float y, WidgetBackground bg) {
        RectF bgRect = bg.getRect();
        mBounds = new RectF(x, y, x+bgRect.width(), y+bgRect.height());
        mBackground = bg;
        mFitToBG = true;
        mNeedUpdate = true;
    }


    protected void onClick() {}

    public void setBounds(RectF bounds) {
        mBounds = bounds;
        mNeedUpdate = true;
    }

    public void setPos(float x, float y) {
        mBounds = new RectF(x, y, x+mBounds.width(), y+mBounds.height());
        mNeedUpdate = true;
    }

    public void setWidth(float width) {
        mBounds.right = mBounds.left + width;
        mNeedUpdate = true;
    }

    public void setHeight(float height) {
        mBounds.bottom = mBounds.top + height;
        mNeedUpdate = true;
    }

    public RectF getBounds() {
        return mBounds;
    }

    public void setBackgroundImage(WidgetBackground bg) {
        mBackground = bg;
        mNeedUpdate = true;
    }


    public boolean containsPt(PointF p) {
        return mBounds.contains(p.x, p.y);
    }

    public boolean handleClick(InputManager.ClickEvent e) {
        if ( this.containsPt(e.getPos()) ) {
            onClick();
            for (Widget w : mEntities) if (w.handleClick(e)) return true;
            return true;
        }
        return false;
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
