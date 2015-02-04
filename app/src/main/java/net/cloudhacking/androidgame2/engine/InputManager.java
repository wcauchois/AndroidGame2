package net.cloudhacking.androidgame2.engine;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by research on 2/2/15.
 */
public class InputManager
        extends Loggable
        implements View.OnTouchListener,
                   GestureDetector.OnGestureListener,
                   ScaleGestureDetector.OnScaleGestureListener
{

    private static final boolean LOG = false;

    private void logEvent(Event e) {
        if (e instanceof ClickEvent) {
            ClickEvent cl = (ClickEvent) e;
            d("dispatching ClickEvent: pos=" + cl.getPos() + ", type=" + cl.getType());
        } else if (e instanceof DragEvent) {
            DragEvent dr = (DragEvent) e;
            d("dispatching DragEvent: pos="+dr.getPos()+", delta="+dr.getDelta()+", type="+dr.getType());
        } else if (e instanceof ScaleEvent) {
            ScaleEvent sc = (ScaleEvent) e;
            d("dispatching ScaleEvent: focus"+sc.getPos()+", span="+sc.getSpan()+", type="+sc.getType());
        }
    }


    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;

    private List<Event> mQueuedEvents = new ArrayList<Event>();

    // TODO: have UIclick and SCENEclick, check UIclick first, then convert
    // TODO: coords and check SCENEclick.
    //public Signal<ClickEvent> clickScene = new Signal<ClickEvent>();
    //public Signal<ClickEvent> clickUI = new Signal<ClickEvent>();
    public Signal<ClickEvent> click = new Signal<ClickEvent>();
    public Signal<DragEvent> drag = new Signal<DragEvent>();
    public Signal<ScaleEvent> scale = new Signal<ScaleEvent>();

    public InputManager(Context context) {
        mScaleDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context, this);
        mDragging = false;
    }


    @Override
    public boolean onTouch(View unused, MotionEvent e) {
        mScaleDetector.onTouchEvent(e);
        mGestureDetector.onTouchEvent(e);
        additionalCheck(e);
        return true;
    }

    private void addEvent(Event e) {
        synchronized (mQueuedEvents) {
            mQueuedEvents.add(e);
        }
    }

    public void processEvents() {
        synchronized (mQueuedEvents) {
            for (Event e : mQueuedEvents) {
                e.doDispatch();
                if (LOG) logEvent(e);
            }
            mQueuedEvents.clear();
        }
    }


    /**********************************************************************************************/

    public static enum ClickEventType {
        DOWN,   // triggered when first touch is down (not second)
        UP,     // triggered when the first touch is lifted without
                //   a drag or scale event starting
        CANCEL  // triggered when a drag or scale event is started
    }

    public static enum DragEventType {
        START,
        UPDATE,
        END
    }

    public static enum ScaleEventType {
        START,
        UPDATE,
        END
    }


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


    public class ClickEvent extends PositionedEvent {
        private ClickEventType mType;

        private ClickEvent(PointF pos, ClickEventType type) {
            super(pos);
            mType = type;
        }

        public ClickEventType getType() {
            return mType;
        }

        @Override
        public void doDispatch() {
            // TODO: dispatch to UI, then scene
            click.dispatch(this);
        }
    }


    public class DragEvent extends PositionedEvent {
        private DragEventType mType;
        private PointF mOrigin;
        private Vec2 mDelta;

        public DragEvent(PointF origin, PointF pos, Vec2 delta, DragEventType type) {
            super(pos);
            mOrigin = origin;
            mDelta = delta;
            mType = type;
        }

        public PointF getOrigin() {
            return mOrigin;
        }

        public Vec2 getDelta() {
            return mDelta;
        }

        public DragEventType getType() {
            return mType;
        }

        @Override
        public void doDispatch() {
            drag.dispatch(this);
        }
    }


    public class ScaleEvent extends PositionedEvent {
        private ScaleEventType mType;
        private float mSpan;

        public ScaleEvent(PointF pos, float span, ScaleEventType type) {
            // [pos] is the distance halfway between the two touches
            // that compose the scaling event, and span is the distance
            // between them.
            super(pos);
            mSpan = span;
            mType = type;
        }

        public float getSpan() {
            return mSpan;
        }

        public ScaleEventType getType() {
            return mType;
        }

        @Override
        public void doDispatch() {
            scale.dispatch(this);
        }
    }


    /**********************************************************************************************/
    // handle click and drag events through GestureDetector

    private boolean mDragging;
    private PointF mDragOrigin;
    private PointF mCurrentDragPos;

    @Override
    public boolean onDown(MotionEvent e) {
        addEvent(new ClickEvent(new PointF(e.getX(), e.getY()),
                                ClickEventType.DOWN)
        );
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
        if (!mDragging) {
            mCurrentDragPos = new PointF(e1.getX(), e1.getY());
            mDragOrigin = mCurrentDragPos;
            addEvent(new ClickEvent(mCurrentDragPos, ClickEventType.CANCEL));
            addEvent(new DragEvent(mDragOrigin,
                                   mCurrentDragPos,
                                   new Vec2(),
                                   DragEventType.START)
            );
            mDragging = true;

        } else {
            mCurrentDragPos = new PointF(e2.getX(), e2.getY());
            addEvent(new DragEvent(mDragOrigin,
                                   mCurrentDragPos,
                                   new Vec2(dx, dy),
                                   DragEventType.UPDATE)
            );
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        addEvent( new ClickEvent(new PointF(e.getX(), e.getY()),
                                 ClickEventType.UP)
        );
        return true;
    }

    // in order to detect if a drag has ended, or other custom gestures
    private void additionalCheck(MotionEvent me) {
        final int action = me.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_UP:
                if (mDragging) {
                    addEvent(new DragEvent(mDragOrigin,
                                           mCurrentDragPos,
                                           new Vec2(),
                                           DragEventType.END)
                    );
                    mDragging = false;
                    mDragOrigin = null;
                }
                break;
        }
    }


    /**********************************************************************************************/
    // handle scale events through ScaleGestureDetector

    private void addScaleEvent(ScaleGestureDetector detector, ScaleEventType eventType) {
        addEvent(new ScaleEvent(
                new PointF(detector.getFocusX(), detector.getFocusY()),
                detector.getCurrentSpan(),
                eventType
        ));
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        addScaleEvent(detector, ScaleEventType.START);
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        addScaleEvent(detector, ScaleEventType.UPDATE);
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        addScaleEvent(detector, ScaleEventType.END);
    }

}
