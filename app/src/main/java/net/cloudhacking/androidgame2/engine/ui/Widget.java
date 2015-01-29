package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.Signal;

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

    // margin around bound elements
    private static int sMargin = 8 /* pixels */ ;
    public static void setMargin(int margin) {
        sMargin = margin;
    }

    // determines widget scaling
    public static enum ScaleType {

        FIXED,  // keep bounds constant relative to root widget size

        FIT_FIXED_RATIO,  // scale to fit parent widget but retain aspect ratio of bounds

        FIT_FREE  // scale to fit parent widget and scale background image to given bounds

    }


    // connect listeners to this for widget touch events
    public static Signal<TouchWidget> widgetSelector = new Signal<TouchWidget>();


    /**********************************************************************************************/

    protected RectF mBounds;
    private WidgetBackground mBackground;
    private boolean mNeedUpdate;
    private BindLocation mBindLocation;
    private ScaleType mScaleType;

    private float mAspectRatio;
    private float mRelativeW;
    private float mRelativeH;


    // widget size based on background image size
    public Widget(WidgetBackground bg, BindLocation loc, ScaleType stype) {
        mBackground = bg;
        mBindLocation = loc;

        RectF bgRect = bg.getRect();
        mRelativeW = bgRect.width() / sRootWidth;
        mRelativeH = bgRect.height() / sRootHeight;
        mAspectRatio = ( bgRect.width() / bgRect.height() ) * ( sRootWidth / sRootHeight );
        mBounds = new RectF(0, 0, mRelativeW, mRelativeH);

        if (stype == ScaleType.FIT_FREE) {
            d("scaling type cannot be FIT_FREE for this constructor; setting to FIT_FIXED_RATIO.");
            mScaleType = ScaleType.FIT_FIXED_RATIO;
        } else {
            mScaleType = stype;
        }

        mNeedUpdate = true;
    }

    // background image size based on widget size
    public Widget(WidgetBackground bg, BindLocation loc, RectF bounds) {
        mBackground = bg;
        mBindLocation = loc;
        mBounds = bounds;
        mScaleType = ScaleType.FIT_FREE;
        mAspectRatio = 0;
        mRelativeW = 0;
        mRelativeH = 0;
        mNeedUpdate = true;
    }

    // create widget whose bounds are raw pixel coordinates
    public Widget(RectF bounds) {
        // assumes boundary values in [0,1] range
        mBounds = bounds;
        mBackground = null;
        mBindLocation = BindLocation.NULL;
        mScaleType = ScaleType.FIXED;
        mRelativeW = bounds.width();
        mRelativeH = bounds.height();
        mAspectRatio = mRelativeW / mRelativeH;
        cancelUpdate();
    }

    public void cancelUpdate() {
        mNeedUpdate = false;
    }

    public void setBackgroundImage(WidgetBackground bg) {
        mBackground = bg;
        RectF bgRect = bg.getRect();
        mRelativeW = bgRect.width() / sRootWidth;
        mRelativeH = bgRect.height() / sRootHeight;
        mAspectRatio = ( bgRect.width() / bgRect.height() ) * ( sRootWidth / sRootHeight );

        mNeedUpdate = true;
    }

    public RectF getBounds() {
        return mBounds;
    }

    public PointF getTopLeft() {
        RectF abs = getAbsoluteBounds();
        return new PointF(abs.left, abs.top);
    }

    public void setBounds(RectF bounds) {
        mBounds = bounds;
        mNeedUpdate = true;
    }

    public void setWidth(float width) {
        mBounds.right = width;
        mNeedUpdate = true;
    }

    public void setHeight(float height) {
        mBounds.bottom = height;
        mNeedUpdate = true;
    }

    public BindLocation getBindLocation() {
        return mBindLocation;
    }

    public void setBindLocation(BindLocation loc) {
        mBindLocation = loc;
        mNeedUpdate = true;
    }

    public ScaleType getScaleType() {
        return mScaleType;
    }

    public void setScaleType(ScaleType stype) {
        mScaleType = stype;
        mNeedUpdate = true;
    }

    public void scaleToWidget(Widget w) {
        RectF r = w.getAbsoluteBounds();
        float rt = (r.right + 2 * sMargin) / sRootWidth;
        float bt = (r.bottom + 2 * sMargin) / sRootHeight;
        setBounds( new RectF(0, 0, rt, bt) );
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

    public void updateBounds() {

        RectF parentAbsBounds = ((Widget)getParent()).getAbsoluteBounds();
        float absW = parentAbsBounds.width();
        float absH = parentAbsBounds.height();

        float w, h;  // width and height should be in the [0,1] range

        float mw = sMargin / absW;
        float mh = sMargin / absH;

        float absWadjust = absW - 2 * sMargin;
        float absHadjust = absH - 2 * sMargin;

        switch(mScaleType) {
            case FIXED:
                w = (mRelativeW * sRootWidth) / absW;
                h = (mRelativeH * sRootHeight) / absH;
                break;

            case FIT_FIXED_RATIO:
                float absRatio = absW/absH;

                // too wide
                if (mAspectRatio > absRatio) {
                    w = (absWadjust / absW);
                    h = (absRatio / mAspectRatio) * (absWadjust / absW);

                // too tall
                } else {
                    w = (mAspectRatio / absRatio) * (absHadjust / absH) ;
                    h = (absHadjust / absH);
                }
                if (mBindLocation == BindLocation.NULL) {
                    d("bind location cannot be NULL for scale type FIT_FIXED_RATIO; setting to CENTER.");
                    mBindLocation = BindLocation.CENTER;
                }
                break;

            case FIT_FREE:
                w = mBounds.width();
                h = mBounds.height();
                w = ( w > (1-2*mw) ? (1-2*mw) : w ) < 0 ? 0 : w;
                h = ( h > (1-2*mh) ? (1-2*mh) : h ) < 0 ? 0 : h;
                break;

            default:
                w = (1-2*mw);
                h = (1-2*mh);
        }


        switch (mBindLocation) {
            case TOP_LEFT:
                mBounds = new RectF(mw, mh, w+mw, h+mh);
                break;

            case TOP_RIGHT:
                mBounds = new RectF(1-w-mw, mh, 1-mw, h+mh);
                break;

            case BOTTOM_LEFT:
                mBounds = new RectF(mw, 1-h-mh, w+mw, 1-mh);
                break;

            case BOTTOM_RIGHT:
                mBounds = new RectF(1-w-mw, 1-h-mh, 1-mw, 1-mh);
                break;

            case CENTER_LEFT:
                mBounds = new RectF(mw, .5f-h/2, w+mw, .5f+h/2);
                break;

            case CENTER_TOP:
                mBounds = new RectF(.5f-w/2, mh, .5f+w/2, h+mh);
                break;

            case CENTER_RIGHT:
                mBounds = new RectF(1-w-mw, .5f-h/2, 1-mw, .5f+h/2);
                break;

            case CENTER_BOTTOM:
                mBounds = new RectF(.5f-w/2, 1-h-mh, .5f+w/2, 1-mh);
                break;

            case CENTER:
                mBounds = new RectF(.5f-w/2, .5f-h/2, .5f+w/2, .5f+h/2);
                break;

            case NULL:
                break;
        }

        // update background image
        if (mBackground != null) {
            mBackground.setToRect(getAbsoluteBounds());
            mBackground.update();
        }
    }



    @Override
    public void update() {
        if (mNeedUpdate) {
            updateBounds();
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
