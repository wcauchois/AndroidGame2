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

    // for root widget
    public Widget(RectF bounds) {
        // assumes boundary values in [0,1] range
        mBounds = bounds;
        mBackground = null;
        mBindLocation = BindLocation.NULL;
        mScaleType = ScaleType.FIXED;
        mRelativeW = bounds.width();
        mRelativeH = bounds.height();
        mAspectRatio = mRelativeW / mRelativeH;
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
        // This function assumes that bounding rect coords are between 0 and 1, unless
        // the scale type is set to fixed width, in which case the bounds will be set to
        // the size of the image in screen space in the constructor.

        RectF parentAbsBounds = ((Widget)getParent()).getAbsoluteBounds();
        float absW = parentAbsBounds.width();
        float absH = parentAbsBounds.height();

        float w, h;
        switch(mScaleType) {
            case FIXED:
                w = mRelativeW / absW;
                h = mRelativeH / absH;
                mBounds = new RectF(0, 0, w, h);
                break;

            case FIT_FIXED_RATIO:
                float absRatio = absW/absH;

                // too wide
                if (mAspectRatio > absRatio) {
                    w = 1;
                    h = absRatio / mAspectRatio;

                // too tall
                } else {
                    w = mAspectRatio / absRatio;
                    h = 1;
                }
                if (mBindLocation == BindLocation.NULL) {
                    d("bind location cannot be NULL for scale type FIT_FIXED_RATIO; setting to CENTER.");
                    mBindLocation = BindLocation.CENTER;
                }
                mBounds = new RectF(0, 0, w, h);
                break;

            case FIT_FREE:
                w = mBounds.width();
                h = mBounds.height();
                w = ( w > 1 ? 1 : w ) < 0 ? 0 : w;
                h = ( h > 1 ? 1 : h ) < 0 ? 0 : h;
                mBounds = new RectF(0, 0, w, h);
                break;

            default:
                w = 1;
                h = 1;
        }

        w = mBounds.width();
        h = mBounds.height();
        switch (mBindLocation) {
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
