package net.cloudhacking.androidgame2.engine;

import android.view.MotionEvent;
import android.view.View;

import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by research on 2/2/15.
 */
public class InputManager extends Loggable implements View.OnTouchListener {

    private static final boolean LOG = true;

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


    private List<Event> mQueuedEvents = new ArrayList<Event>();

    public Signal<ClickEvent> click = new Signal<ClickEvent>();
    public Signal<DragEvent> drag = new Signal<DragEvent>();
    public Signal<ScaleEvent> scale = new Signal<ScaleEvent>();


    @Override
    public boolean onTouch(View unused, MotionEvent mEvent) {
        processEvent(mEvent);
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
            click.dispatch(this);
        }
    }


    public class DragEvent extends PositionedEvent {
        private DragEventType mType;
        private Vec2 mDelta;

        public DragEvent(PointF pos, Vec2 delta, DragEventType type) {
            super(pos);
            mDelta = delta;
            mType = type;
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

    private static final Pointer INVALID_POINTER = null;

    private static final float DRAG_START_THRESHOLD = 0.1f;

    private Pointer mActivePointer1;  // first touch; for click and drag
    private Pointer mActivePointer2;  // second touch; for scaling
    private boolean mDragging;
    private boolean mScaling;

    public InputManager() {
        mActivePointer1 = INVALID_POINTER;
        mActivePointer2 = INVALID_POINTER;
        mDragging = false;
        mScaling = false;
    }

    private class Pointer {
        private int mId;
        private PointF mPos;
        private Vec2 mDelta;

        public Pointer(int id, PointF pos) {
            mId = id;
            mPos = pos;
            mDelta = new Vec2();
        }

        public int getId() {
            return mId;
        }

        public PointF getPos() {
            return mPos;
        }

        public Vec2 getDelta() {
            return mDelta;
        }

        public void updatePos(PointF pos) {
            mDelta = mPos.vecTowards(pos);
            mPos = pos;
        }

        public Pointer clone() {
            return new Pointer(mId, mPos.copy());
        }
    }


    private void processEvent(MotionEvent me) {
        final int action = me.getActionMasked();

        int id;
        PointF pos;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // track pointer
                id = me.getPointerId(0);
                pos = new PointF(me.getX(), me.getY());
                mActivePointer1 = new Pointer(id, pos);
                addEvent( genClickEvent(ClickEventType.DOWN) );
                break;


            case MotionEvent.ACTION_POINTER_DOWN:
                if (mActivePointer2 == INVALID_POINTER) {
                    id = me.getPointerId(me.getActionIndex());
                    pos = new PointF(me.getX(), me.getY());
                    mActivePointer2 = new Pointer(id, pos);
                }
                break;


            case MotionEvent.ACTION_MOVE:
                int count = me.getPointerCount();

                // update drag
                pos = new PointF(me.getX(0), me.getY(0));
                mActivePointer1.updatePos(pos);

                if (mActivePointer1.getDelta().norm() < DRAG_START_THRESHOLD) {
                    return;
                }

                if (!mDragging) {
                    addEvent( genClickEvent(ClickEventType.CANCEL) );
                    addEvent( genDragEvent(DragEventType.START) );
                    mDragging = true;
                } else {
                    addEvent(genDragEvent(DragEventType.UPDATE));
                }

                // multi-touch; update scale
                if (count > 1) {

                    // find id matching mActivePointer2 and update
                    for(int i=0; i<count; i++) {
                        id = me.getPointerId(i);
                        if (id == mActivePointer2.getId()) {
                            pos = new PointF(me.getX(i), me.getY(i));
                            mActivePointer2.updatePos(pos);
                        }
                    }

                    if (!mScaling) {
                        addEvent( genScaleEvent(ScaleEventType.START) );
                        mScaling = true;
                    } else {
                        addEvent( genScaleEvent(ScaleEventType.UPDATE) );
                    }
                }
                break;


            case MotionEvent.ACTION_POINTER_UP:
                id = me.getPointerId(me.getActionIndex());

                if (id == mActivePointer2.getId()) {
                    pos = new PointF(me.getX(), me.getY());
                    mActivePointer2.updatePos(pos);

                    addEvent( genScaleEvent(ScaleEventType.END) );
                    mActivePointer2 = INVALID_POINTER;
                    mScaling = false;

                // if first touch down lifts up first, switch first
                // active pointer to second one
                } else if (id == mActivePointer1.getId()) {
                    pos = new PointF(me.getX(), me.getY());
                    mActivePointer1.updatePos(pos);

                    addEvent( genScaleEvent(ScaleEventType.END) );
                    mActivePointer1 = mActivePointer2.clone();
                    mActivePointer2 = INVALID_POINTER;
                    mScaling = false;
                }
                break;


            case MotionEvent.ACTION_UP:
                pos = new PointF(me.getX(), me.getY());
                mActivePointer1.updatePos(pos);

                if (mDragging) {
                    addEvent( genDragEvent(DragEventType.END) );
                    mDragging = false;

                } else {
                    addEvent( genClickEvent(ClickEventType.UP) );
                }

                mActivePointer1 = INVALID_POINTER;
                break;
        }
    }


    // generate events from the active pointers

    private ClickEvent genClickEvent(ClickEventType type) {
        return new ClickEvent(mActivePointer1.getPos(), type);
    }

    private DragEvent genDragEvent(DragEventType type) {
        return new DragEvent(mActivePointer1.getPos(),
                             mActivePointer1.getDelta(),
                             type);
    }

    private ScaleEvent genScaleEvent(ScaleEventType type) {
        PointF focus = mActivePointer1.getPos()
                .pointBetween(mActivePointer2.getPos());
        float span = mActivePointer1.getPos()
                .distTo(mActivePointer2.getPos());

        return new ScaleEvent(focus, span, type);
    }



}
