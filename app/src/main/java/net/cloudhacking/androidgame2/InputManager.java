package net.cloudhacking.androidgame2;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wcauchois on 1/8/15.
 */
public class InputManager {
    private static final String TAG = InputManager.class.getSimpleName();
    // How long you have to hold down for your action to be interpreted as a click and
    // not a drag.
    private static final int CLICK_DETECTION_MILLISECONDS = 200;

    private Vector2 mDragStartPos = null;
    private boolean mDragging = false;
    private boolean mPressing = false;

    // Used to execute a callback after a delay
    private static final ScheduledExecutorService sWorker =
            Executors.newSingleThreadScheduledExecutor();

    public interface ClickListener {
        void onClick(Vector2 pos);
    }

    public interface DragListener {
        void onDrag(Vector2 currentPos, Vector2 posDelta);
    }

    public interface StartDragListener {
        void onStartDrag();
    }

    public interface EndDragListener {
        void onEndDrag();
    }

    public boolean getDragging() {
        return mDragging;
    }

    private List<ClickListener> mClickListeners = new ArrayList<ClickListener>();
    private List<DragListener> mDragListeners = new ArrayList<DragListener>();
    private List<StartDragListener> mStartDragListeners = new ArrayList<StartDragListener>();
    private List<EndDragListener> mEndDragListeners = new ArrayList<EndDragListener>();

    public void addClickListener(ClickListener listener) {
        mClickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        mClickListeners.remove(listener);
    }

    public void addDragListener(DragListener listener) {
        mDragListeners.add(listener);
    }

    public void addStartDragListener(StartDragListener listener) {
        mStartDragListeners.add(listener);
    }

    public void addEndDragListener(EndDragListener listener) {
        mEndDragListeners.add(listener);
    }

    private class DelayedPressRunnable implements Runnable {
        private Vector2 mStartPos;

        public DelayedPressRunnable(Vector2 startPos) {
            mStartPos = startPos;
        }

        @Override public void run() {
            synchronized (InputManager.this) {
                if (mPressing == true) {
                    mDragging = true;
                    mDragStartPos = mStartPos;
                    triggerStartDrag();
                    triggerDrag(mStartPos);
                } else {
                    triggerClick(mStartPos);
                }
            }
        }
    }

    private void triggerClick(Vector2 pos) {
        for (ClickListener listener : mClickListeners) {
            listener.onClick(pos);
        }
    }

    private void triggerDrag(Vector2 currentPos) {
        Vector2 posDelta = currentPos.subtract(mDragStartPos);
        for (DragListener listener : mDragListeners) {
            listener.onDrag(currentPos, posDelta);
        }
    }

    private void triggerStartDrag() {
        for (StartDragListener listener : mStartDragListeners) {
            listener.onStartDrag();
        }
    }

    private void triggerEndDrag() {
        for (EndDragListener listener : mEndDragListeners) {
            listener.onEndDrag();
        }
    }

    public synchronized void handleTouchEvent(MotionEvent event) {
        Vector2 currentPos = new Vector2(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPressing = true;
                sWorker.schedule(new DelayedPressRunnable(currentPos),
                        CLICK_DETECTION_MILLISECONDS, TimeUnit.MILLISECONDS);
                break;
            case MotionEvent.ACTION_UP:
                mPressing = false;
                if (mDragging) {
                    mDragging = false;
                    mDragStartPos = null;
                    triggerEndDrag();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragging) {
                    triggerDrag(currentPos);
                }
                break;
        }
    }
}
