package net.cloudhacking.androidgame2.engine.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcauchois on 1/8/15.
 */
public class InputManager

        extends Loggable
        implements View.OnTouchListener,
                   GestureDetector.OnGestureListener,
                   ScaleGestureDetector.OnScaleGestureListener
{

    /**
     * This class is the interface between android's touch screen and our Signal triggers.
     */

    public Signal<ClickEvent> clickUp = new Signal<ClickEvent>();
    public Signal<StartDragEvent> startDrag = new Signal<StartDragEvent>();
    public Signal<DragEvent> drag = new Signal<DragEvent>();
    public Signal<ScaleEvent> startScale = new Signal<ScaleEvent>();
    public Signal<ScaleEvent> scale = new Signal<ScaleEvent>();
    public Signal<ScaleEvent> endScale = new Signal<ScaleEvent>();

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

        addEvent(new ClickEvent(new PointF(e.getX(), e.getY()), ClickEventType.CLICK_DOWN));

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

        addEvent(new AnyDragEvent(
                new PointF(e1.getX(), e1.getY()),
                new PointF(e2.getX(), e2.getY()),
                new Vec2(dx, dy)
        ));

        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        if (INPUT_DEBUG) d("onShowPress called");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (INPUT_DEBUG) d("onSingleTapUp called");

        addEvent(new ClickEvent(new PointF(e.getX(), e.getY()), ClickEventType.CLICK_UP));

        return true;
    }


    /**
     * Scale listener callbacks
     */
    private void addScaleEvent(ScaleGestureDetector detector, ScaleEventType eventType) {
        addEvent(new ScaleEvent(
                new PointF(detector.getFocusX(), detector.getFocusY()),
                detector.getCurrentSpan(),
                eventType
        ));
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        if (INPUT_DEBUG) d("onScaleBegin called");

        addScaleEvent(detector, ScaleEventType.START_SCALE);

        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if (INPUT_DEBUG) d("onScale called");

        addScaleEvent(detector, ScaleEventType.SCALE);

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        if (INPUT_DEBUG) d("onScaleEnd called");

        addScaleEvent(detector, ScaleEventType.END_SCALE);
    }


    /**********************************************************************************************/

    public abstract class Event {
        protected abstract void doDispatch();
    }

    public abstract class PositionedEvent extends Event {
        private PointF mPos;

        public PointF getPos() {
            return mPos;
        }

        private PositionedEvent(PointF pos) {
            mPos = pos;
        }
    }


    /**
     * Drag Events
     */
    public class AnyDragEvent extends Event {
        protected PointF mPos1, mPos2;
        protected Vec2 mDelta;

        @Override
        protected void doDispatch() {
            if (!mScrolling) {
                startDrag.dispatch(new StartDragEvent(this));
                mScrolling = true;
            } else {
                drag.dispatch(new DragEvent(this));
            }
        }

        private AnyDragEvent(PointF pos1, PointF pos2, Vec2 delta) {
            mPos1 = pos1;
            mPos2 = pos2;
            mDelta = delta;
        }

        private AnyDragEvent(AnyDragEvent proto) {
            mPos1 = proto.mPos1;
            mPos2 = proto.mPos2;
            mDelta = proto.mDelta;
        }
    }

    public class StartDragEvent extends AnyDragEvent {
        private StartDragEvent(AnyDragEvent proto) {
            super(proto);
        }

        public PointF getPos() {
            return mPos1;
        }
    }

    public class DragEvent extends AnyDragEvent {
        private DragEvent(AnyDragEvent proto) {
            super(proto);
        }

        public PointF getPos() {
            return mPos2;
        }

        public Vec2 getDelta() {
            return mDelta;
        }
    }


    /**
     * Scale Event
     */
    public class ScaleEvent extends Event {
        private PointF mFocus;
        private float mSpan;
        private ScaleEventType mEventType;

        public PointF getFocus() {
            return mFocus;
        }

        public float getSpan() {
            return mSpan;
        }

        private ScaleEvent(PointF focus, float span, ScaleEventType eventType) {
            mFocus = focus;
            mSpan = span;
            mEventType = eventType;
        }

        @Override
        protected void doDispatch() {
            switch (mEventType) {
                case START_SCALE:
                    startScale.dispatch(this);
                    break;
                case SCALE:
                    scale.dispatch(this);
                    break;
                case END_SCALE:
                    endScale.dispatch(this);
                    break;
            }
        }
    }

    private enum ScaleEventType {
        START_SCALE, SCALE, END_SCALE
    }


    /**
     * Click Event
     */
    public class ClickEvent extends PositionedEvent {
        private ClickEventType mEventType;

        protected ClickEvent(PointF pos, ClickEventType eventType) {
            super(pos);
            mEventType = eventType;
        }

        @Override
        protected void doDispatch() {
            switch (mEventType) {
                case CLICK_UP:
                    clickUp.dispatch(this);
                    break;
                case CLICK_DOWN:
                    // Currently no click down signal.
                    mScrolling = false;
                    break;
            }
        }
    }

    private enum ClickEventType {
        CLICK_UP, CLICK_DOWN
    }


    /**********************************************************************************************/

    private List<Event> mQueuedEvents = new ArrayList<Event>();

    private void addEvent(Event e) {
        synchronized (mQueuedEvents) {
            mQueuedEvents.add(e);
        }
    }

    public void processEvents() {
        synchronized (mQueuedEvents) {
            for (Event e : mQueuedEvents) {
                e.doDispatch();
            }
            mQueuedEvents.clear();
        }
    }

    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    public InputManager(Context context) {
        mGestureDetector = new GestureDetector(context, this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
    }
}
