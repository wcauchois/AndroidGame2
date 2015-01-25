package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by wcauchois on 1/21/15.
 */
public abstract class Widget extends Group<Widget> {

    /**
     * This represents a general UI object.  The coordinate system is set up so that a
     * widgets bounding rect size is a percentage of its parent's bounding rect size.  This way
     * we don't have to worry about screen size and aspect ratio etc.  The root widget's bounds
     * would typically be the size of the screen.
     *
     * There is also and automatic binding system for different screen locations, see ui.Utils.
     */

    // use these in order to scale all children widgets properly
    private static int sRootWidth;
    private static int sRootHeight;

    public static void setRootSize(int sw, int sh) {
        sRootWidth = sw;
        sRootHeight = sh;
    }

    public static int getRootWidth() {
        return sRootWidth;
    }

    public static int getRootHeight() {
        return sRootHeight;
    }

    // bind widget to location within parent's bounding box
    public static enum BindLocation {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER_LEFT,
        CENTER_TOP,
        CENTER_RIGHT,
        CENTER_BOTTOM,
        CENTER,
        NULL  // don't bind
    }


    /**********************************************************************************************/

    protected RectF mBounds;
    private WidgetBackground mBackground;
    private boolean mNeedBGUpdate;
    private BindLocation mBindLocation;

    public Widget(RectF bounds, BindLocation loc) {
        mBackground = null;
        mNeedBGUpdate = false;
        mBindLocation = loc;
        setBounds(bounds);
    }

    public Widget(RectF bounds) {
        this(bounds, BindLocation.NULL);
    }

    public Widget() {
        this( new RectF(0, 0, 1, 1) );
    }


    public RectF getBounds() {
        return mBounds;
    }

    public void setBounds(RectF bounds) {
        mBounds = bounds;
        setBindLocation(mBindLocation);
        mNeedBGUpdate = true;
    }

    public PointF getTopLeft() {
        return new PointF(mBounds.left, mBounds.top);
    }

    public RectF getAbsoluteBounds() {
        // get bounds for this widget relative to the root widget
        if (this instanceof RootWidget) {
            return mBounds;

        } else if (getParent() instanceof Widget) {
            RectF parentBounds = ((Widget) getParent()).getAbsoluteBounds();

            float width = parentBounds.width() * mBounds.width();
            float height = parentBounds.height() * mBounds.height();
            float left = parentBounds.left + (mBounds.left*parentBounds.width());
            float top = parentBounds.top  + (mBounds.top*parentBounds.height());

            return new RectF(left, top, left + width, top + height);

        } else {
            return null;
        }
    }


    public BindLocation getBindLocation() {
        return mBindLocation;
    }

    public void setBindLocation(BindLocation loc) {
        // this function assumes that bounding rect coords are between 0 and 1
        mBindLocation = loc;
        float w = mBounds.width();
        float h = mBounds.height();

        switch (loc) {
            case TOP_LEFT:
                mBounds = new RectF(0, 0, w, h);
                break;

            case TOP_RIGHT:
                mBounds = new RectF(1 - w, 0, 1, h);
                break;

            case BOTTOM_LEFT:
                mBounds = new RectF(0, 1 - h, w, 1);
                break;

            case BOTTOM_RIGHT:
                mBounds = new RectF(1 - w, 1 - h, 1, 1);
                break;

            case CENTER_LEFT:
                mBounds = new RectF(0, .5f - h/2, w, .5f + h/2);
                break;

            case CENTER_TOP:
                mBounds = new RectF(.5f - w/2, 0, .5f + w/2, h);
                break;

            case CENTER_RIGHT:
                mBounds = new RectF(1 - w, .5f - h/2, 1, .5f + h/2);
                break;

            case CENTER_BOTTOM:
                mBounds = new RectF(.5f - w/2, 1 - h, .5f + w/2, 1);
                break;

            case CENTER:
                mBounds = new RectF(.5f - w/2, .5f - h/2, .5f + w/2, .5f + h/2);
                break;

            case NULL:
                break;
        }
    }

    public void setBackgroundImage(WidgetBackground bg) {
        mBackground = bg;
        mNeedBGUpdate = true;
    }




    // use Signal here?
    public void onClick(Vec2 pos) {
        /*for (Widget child : mEntities) {
            if (pos.containedBy(child.getBounds())) {
                child.onClick(pos.subtract(child.getTopLeft()));
            }
        }*/
    }



    @Override
    public void update() {
        if (mBackground != null && mNeedBGUpdate)
        {
            mBackground.setToRect(getAbsoluteBounds());
            mBackground.update();
            mNeedBGUpdate = false;
        }
        super.update();
    }

    @Override
    public void draw(BasicGLScript gls) {
        if (mBackground != null) mBackground.draw(gls);
        super.draw(gls);
    }


}
