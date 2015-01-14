package net.cloudhacking.androidgame2;

import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 * Created by wcauchois on 1/8/15.
 *
 * Actions needed to be
 */
public class InputManager {
    private static final String TAG = InputManager.class.getSimpleName();
    private static final boolean LOG_INPUT = false;
    private static final boolean LOG_TRIGGERS = true;


    public static HashMap<Integer, Pointer> pointers = new HashMap<Integer, Pointer>();

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
            return "Pointer(id="+mId+", start="+mStartPos+", current="+mCurrentPos+", dragging="+mDragging+")";
        }
    }


    public interface ClickListener {
        void onClick(Pointer pointer);
    }

    public interface DragListener {
        void onStartDrag(Pointer pointer);
        void onEndDrag(Pointer pointer);
    }

    public interface MultiTouchListener {

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



    private void triggerClick(Pointer pointer) {
        for (ClickListener listener : mClickListeners) {
            listener.onClick(pointer);
        }
        if (LOG_TRIGGERS) Log.d(TAG, "Click Triggered: pointer=" + pointer);
    }

    private void triggerStartDrag(Pointer pointer) {
        for (DragListener listener : mDragListeners) {
            listener.onStartDrag(pointer);
        }
        if (LOG_TRIGGERS) Log.d(TAG, "StartDrag Triggered: pointer=" + pointer);
    }

    private void triggerEndDrag(Pointer pointer) {
        for (DragListener listener : mDragListeners) {
            listener.onEndDrag(pointer);
        }
        if (LOG_TRIGGERS) Log.d(TAG, "EndDrag Triggered: pointer=" + pointer);
        if (LOG_TRIGGERS) Log.d(TAG, "---> total dist: " + pointer.getDelta().dist());
    }

    private void triggerMultiTouch(Pointer[] pointers) {

    }



    public void handleTouchEvents(ArrayList<MotionEvent> events) {

        int size = events.size();
        for (int i=0; i < size; i++) {

            MotionEvent e = events.get( i );
            Pointer pointer;

            switch (e.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    pointer = new Pointer(e, 0);
                    pointers.put(pointer.getId(), pointer);

                    if (LOG_INPUT) Log.d(TAG, "ACTION_DOWN: " + pointer);
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    pointer = new Pointer(e, e.getActionIndex() );
                    pointers.put(pointer.getId(), pointer);

                    // end drag and trigger multi touch listeners here

                    if (LOG_INPUT) Log.d(TAG, "ACTION_POINTER_DOWN: " + pointer);
                    break;

                case MotionEvent.ACTION_MOVE:
                    int count = e.getPointerCount();
                    for (int j=0; j < count; j++) {
                        pointer = pointers.get( e.getPointerId(j) );
                        pointer.update(e, j);

                        if (LOG_INPUT) Log.d(TAG, "ACTION_MOVE: " + pointer);

                        if (!pointer.checkDragging() && pointer.getDelta().dist()>0) {
                            pointer.setDragging(true);
                            triggerStartDrag(pointer);
                        }
                    }
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    pointer = pointers.remove( e.getPointerId( e.getActionIndex() ) ).up();

                    if (LOG_INPUT) Log.d(TAG, "ACTION_POINTER_UP: " + pointer);

                    if (pointer.checkDragging()) {
                        triggerEndDrag(pointer);
                    } else {
                        triggerClick(pointer);
                    }

                    break;

                case MotionEvent.ACTION_UP:
                    pointer = pointers.remove( e.getPointerId( 0 ) ).up();

                    if (LOG_INPUT) Log.d(TAG, "ACTION_UP: " + pointer);

                    if (pointer.checkDragging()) {
                        triggerEndDrag(pointer);
                    } else {
                        triggerClick(pointer);
                    }

                    break;

            }

            e.recycle();
        }
    }
}
