package net.cloudhacking.androidgame2.engine.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by wcauchois on 1/8/15.
 *
 * TODO: Idk much about synchronization, but I think some of this might need to be synchronized?
 */
public class InputManager

        extends Loggable
        implements View.OnTouchListener,
                   GestureDetector.OnGestureListener,
                   ScaleGestureDetector.OnScaleGestureListener
{

    private static final boolean INPUT_DEBUG = false;
    private static final boolean TRIGGER_DEBUG = false;


    /**
     * Touch listener callback
     */
    @Override
    public boolean onTouch(View unused, MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }


    /**
     * Gesture listener callbacks
     */
    private boolean mScrolling = false;

    @Override
    public boolean onDown(MotionEvent e) {
        if (INPUT_DEBUG) d("onDown called");

        mScrolling = false;
        triggerClickDown( new PointF(e.getX(), e.getY()) );

        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
        if (INPUT_DEBUG) d("onFling called");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (INPUT_DEBUG) d("onLongPress called");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
        if (INPUT_DEBUG) d("onScroll called");

        if (!mScrolling) {
            triggerClickCancel();
            triggerStartDrag( new PointF(e1.getX(), e1.getY()) );
            mScrolling = true;
        } else {
            triggerDrag( new PointF(e2.getX(), e2.getY()), new Vec2(dx, dy) );
        }

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        if (INPUT_DEBUG) d("onShowPress called");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (INPUT_DEBUG) d("onSingleTapUp called");

        triggerClickUp( new PointF(e.getX(), e.getY()) );

        return true;
    }


    /**
     * Scale listener callbacks
     */
    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        if (INPUT_DEBUG) d("onScaleBegin called");

        triggerStartScale( new PointF(detector.getFocusX(), detector.getFocusY()),
                detector.getCurrentSpan() );

        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (INPUT_DEBUG) d("onScale called");

        triggerScale( new PointF(detector.getFocusX(), detector.getFocusY()),
                detector.getCurrentSpan() );

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (INPUT_DEBUG) d("onScaleEnd called");

        triggerEndScale( new PointF(detector.getFocusX(), detector.getFocusY()),
                detector.getCurrentSpan() );

    }


    /**********************************************************************************************/

    public static interface ClickListener {
        public void onClickDown(PointF down);
        public void onClickUp(PointF up);
        public void onClickCancel();
    }

    public static interface DragListener {
        public void onStartDrag(PointF start);
        public void onDrag(PointF cur, Vec2 dv);
    }

    public static interface ScaleListener {
        public void onStartScale(PointF focus, float span);
        public void onScale(PointF focus, float span);
        public void onEndScale(PointF focus, float span);
    }


    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    ArrayList<ClickListener> mClickListeners = new ArrayList<ClickListener>();
    ArrayList<DragListener>  mDragListeners  = new ArrayList<DragListener>();
    ArrayList<ScaleListener> mScaleListeners = new ArrayList<ScaleListener>();

    public InputManager(Context context) {
        mGestureDetector = new GestureDetector(context, this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
    }


    public void addClickListener(ClickListener listener) {
        mClickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        mClickListeners.remove(listener);
    }

    public void addDragListener(DragListener listener) {
        mDragListeners.add(listener);
    }

    public void removeDragListener(DragListener listener) {
        mDragListeners.remove(listener);
    }

    public void addScaleListener(ScaleListener listener) {
        mScaleListeners.add(listener);
    }

    public void removeScaleListener(ScaleListener listener) {
        mScaleListeners.remove(listener);
    }


    private void triggerClickDown(PointF down) {
        if (TRIGGER_DEBUG) d("onClick triggered : down="+down);
        for (ClickListener l : mClickListeners) {
            l.onClickDown(down);
        }
    }

    private void triggerClickUp(PointF up) {
        if (TRIGGER_DEBUG) d("onClick triggered : up="+up);
        for (ClickListener l : mClickListeners) {
            l.onClickUp(up);
        }
    }

    private void triggerClickCancel() {
        if (TRIGGER_DEBUG) d("onClick triggered");
        for (ClickListener l : mClickListeners) {
            l.onClickCancel();
        }
    }

    private void triggerStartDrag(PointF start) {
        if (TRIGGER_DEBUG) d("onStartDrag triggered : start="+start);
            for (DragListener l : mDragListeners) {
            l.onStartDrag(start);
        }
    }

    private void triggerDrag(PointF cur, Vec2 dv) {
        if (TRIGGER_DEBUG) d("onDrag triggered : cur="+cur+", dv="+dv);
            for (DragListener l : mDragListeners) {
            l.onDrag(cur, dv);
        }
    }

    private void triggerStartScale(PointF focus, float span) {
        if (TRIGGER_DEBUG) d("onStartScale triggered : focus="+focus+", span="+span);
            for (ScaleListener l : mScaleListeners) {
            l.onStartScale(focus, span);
        }
    }

    private void triggerScale(PointF focus, float span) {
        if (TRIGGER_DEBUG) d("onScale triggered : focus="+focus+", span="+span);
            for (ScaleListener l : mScaleListeners) {
            l.onScale(focus, span);
        }
    }

    private void triggerEndScale(PointF focus, float span) {
        if (TRIGGER_DEBUG) d("onEndScale triggered : focus="+focus+", span="+span);
        for (ScaleListener l : mScaleListeners) {
            l.onEndScale(focus, span);
        }
    }

}
