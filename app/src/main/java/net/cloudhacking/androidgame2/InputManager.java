package net.cloudhacking.androidgame2;

import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcauchois on 1/8/15.
 *
 *
 * Actions needed to be account for:
 *
 *      -Click
 *      -Drag
 *      -Pinch Zoom
 *      -others?
 *
 */
public class InputManager {
    private static final String TAG = InputManager.class.getSimpleName();
    private static final boolean LOG_INPUT = false;
    private static final boolean LOG_TRIGGERS = true;

    private SparseArray<Pointer> mPointers = new SparseArray<Pointer>();
    private MultiTouch mMultiTouch = null;


    /*
     * Represents a single finger on the touch screen
     */
    public static class Pointer {
        private int mId;
        private Vec2 mStartPos;
        private Vec2 mCurrentPos;
        private boolean mDown;
        private boolean mDragging=false;

        public Pointer( MotionEvent e, int index ) {
            mId = e.getPointerId(index);

            float x = e.getX( index );
            float y = e.getY( index );

            mStartPos = new Vec2( x, y );
            mCurrentPos = new Vec2( x, y );

            mDown = true;
        }

        public int getId() { return mId; }
        public boolean checkDown() { return mDown; }
        public boolean checkDragging() { return mDragging; }
        public void setDragging(boolean bool) { mDragging = bool; }

        public Vec2 getStartPos() {
            return mStartPos;
        }
        public Vec2 getCurrentPos() {
            return mCurrentPos;
        }
        public Vec2 getDelta() {
            return mCurrentPos.subtract(mStartPos);
        }

        public void update( MotionEvent e, int index ) {
            mCurrentPos.set(e.getX(index), e.getY(index));
        }

        public Pointer up() {
            mDown = false;
            return this;
        }

        @Override
        public String toString() {
            return "Pointer(id="+mId+", start="+mStartPos+
                    ", current="+mCurrentPos+", dragging="+mDragging+")";
        }
    }


    /*
     * Represents two fingers on the touch screen
     */
    public static class MultiTouch {
        private Pointer p1;
        private Pointer p2;

        public MultiTouch(Pointer p1, Pointer p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        public boolean checkDown() {
            return p1.checkDown() && p2.checkDown();
        }

        public float getDistBtwn() {
            return p1.getCurrentPos().subtract(p2.getCurrentPos()).dist();
        }

        @Override
        public String toString() {
            return "{"+p1+", "+p2+"}";
        }
    }


    /*
     * Listener Interfaces
     */
    public interface ClickListener {
        void onClick(Pointer pointer);
    }

    public interface DragListener {
        void onStart(Pointer pointer);
        void onEnd(Pointer pointer);
        void update();
    }

    public interface MultiTouchListener {
        void onStart(MultiTouch multiTouch);
        void onEnd(MultiTouch multiTouch);
        void update();
    }


    private List<ClickListener> mClickListeners = new ArrayList<ClickListener>();
    private List<DragListener> mDragListeners = new ArrayList<DragListener>();
    private List<MultiTouchListener> mMultiTouchListeners = new ArrayList<MultiTouchListener>();

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

    public void addMultiTouchListener(MultiTouchListener listener) {
        mMultiTouchListeners.add(listener);
    }

    public void removeMultiTouchListeners(MultiTouchListener listener) {
        mMultiTouchListeners.remove(listener);
    }


    /*
     * Input Triggers
     */
    private void triggerClick(Pointer pointer) {
        for (ClickListener listener : mClickListeners) {
            listener.onClick(pointer);
        }
        if (LOG_TRIGGERS) Log.d(TAG, "Click Triggered: pointer=" + pointer);
    }

    private void triggerStartDrag(Pointer pointer) {
        for (DragListener listener : mDragListeners) {
            listener.onStart(pointer);
        }
        if (LOG_TRIGGERS) Log.d(TAG, "StartDrag Triggered: pointer=" + pointer);
    }

    private void triggerUpdateDrag() {
        for (DragListener listener : mDragListeners) {
            listener.update();
        }
    }

    private void triggerEndDrag(Pointer pointer) {
        for (DragListener listener : mDragListeners) {
            listener.onEnd(pointer);
        }
        if (LOG_TRIGGERS) Log.d(TAG, "EndDrag Triggered: pointer=" + pointer);
    }

    private void triggerStartMultiTouch(MultiTouch multiTouch) {
        for (MultiTouchListener listener : mMultiTouchListeners) {
            listener.onStart(multiTouch);
        }
        if (LOG_TRIGGERS) Log.d(TAG, "StartMultiTouch Triggered: multiTouch=" + multiTouch);
    }

    private void triggerUpdateMultiTouch() {
        for (MultiTouchListener listener : mMultiTouchListeners) {
            listener.update();
        }
    }

    private void triggerEndMultiTouch(MultiTouch multiTouch) {
        for (MultiTouchListener listener : mMultiTouchListeners) {
            listener.onEnd(multiTouch);
        }
        if (LOG_TRIGGERS) Log.d(TAG, "EndMultiTouch Triggered: multiTouch=" + multiTouch);
    }


    /*
     * Accumulated Touch Events Handler (called each frame)
     */
    public void handleTouchEvents(ArrayList<MotionEvent> events) {

        int size = events.size();
        for (int i=0; i < size; i++) {

            MotionEvent e = events.get( i );
            int count;
            Pointer pointerTmp, pointerMT0, pointerMT1;

            switch (e.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    pointerTmp = new Pointer(e, 0);
                    mPointers.put(pointerTmp.getId(), pointerTmp);

                    if (LOG_INPUT) Log.d(TAG, "ACTION_DOWN: " + pointerTmp);
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    pointerTmp = new Pointer(e, e.getActionIndex() );
                    mPointers.put(pointerTmp.getId(), pointerTmp);

                    if (LOG_INPUT) Log.d(TAG, "ACTION_POINTER_DOWN: " + pointerTmp);
                    break;

                case MotionEvent.ACTION_MOVE:
                    count = e.getPointerCount();
                    for (int j=0; j < count; j++) {
                        pointerTmp = mPointers.get( e.getPointerId(j) );
                        pointerTmp.update(e, j);

                        if (LOG_INPUT) Log.d(TAG, "ACTION_MOVE: " + pointerTmp);
                    }

                    if (count==1) {
                        // only trigger drag for first finger down
                        pointerMT0 = mPointers.get( e.getPointerId(0) );
                        if (!pointerMT0.checkDragging() && pointerMT0.getDelta().dist()>0) {
                            pointerMT0.setDragging(true);
                            triggerStartDrag(pointerMT0);
                        }

                    } else if (count==2) {
                        // for two fingers down automatically trigger multitouch and
                        // drag for both fingers
                        pointerMT0 = mPointers.get( e.getPointerId(0) );
                        pointerMT1 = mPointers.get( e.getPointerId(1) );
                        if (!pointerMT0.checkDragging() && pointerMT0.getDelta().dist()>0) {
                            pointerMT0.setDragging(true);
                            triggerStartDrag(pointerMT0);
                        }

                        if (!pointerMT1.checkDragging() && pointerMT1.getDelta().dist()>0) {
                            pointerMT1.setDragging(true);
                            triggerStartDrag(pointerMT1);
                            mMultiTouch = new MultiTouch(pointerMT0, pointerMT1);
                            triggerStartMultiTouch(mMultiTouch);
                        }
                    }

                    if (count>0) triggerUpdateDrag();
                    if (count>1) triggerUpdateMultiTouch();

                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    pointerTmp = mPointers.get( e.getPointerId( e.getActionIndex() ) ).up();
                    mPointers.remove( pointerTmp.getId() );

                    if (LOG_INPUT) Log.d(TAG, "ACTION_POINTER_UP: " + pointerTmp);

                    if (mMultiTouch != null && !mMultiTouch.checkDown()) {
                        triggerEndMultiTouch(mMultiTouch);
                        mMultiTouch = null;
                    }

                    if (pointerTmp.checkDragging()) {
                        triggerEndDrag(pointerTmp);
                    } else {
                        triggerClick(pointerTmp);
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    pointerTmp = mPointers.get( e.getPointerId( 0 ) ).up();
                    mPointers.remove( pointerTmp.getId() );

                    if (LOG_INPUT) Log.d(TAG, "ACTION_UP: " + pointerTmp);

                    if (pointerTmp.checkDragging()) {
                        triggerEndDrag(pointerTmp);
                    } else {
                        triggerClick(pointerTmp);
                    }

                    break;

            }

            e.recycle();
        }
    }
}
