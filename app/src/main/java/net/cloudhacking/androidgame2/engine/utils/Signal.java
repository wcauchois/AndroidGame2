package net.cloudhacking.androidgame2.engine.utils;

import java.util.LinkedList;

/**
 * Created by wcauchois on 1/20/15.
 */
public class Signal<T> {
    public static interface Listener<T> {
        public void onSignal(T t);
    }

    private boolean mCancelled = false;
    private LinkedList<Listener<T>> mListeners = new LinkedList<Listener<T>>();

    public void connect(Listener<T> listener) {
        mListeners.addLast(listener);
    }

    public void remove(Listener<T> listener) {
        mListeners.remove(listener);
    }

    public void dispatch(T t) {
        Listener<T>[] listeners = mListeners.toArray(new Listener[0]);

        mCancelled = false;
        for (int i = 0; i < listeners.length; i++) {
            Listener<T> listener = listeners[i];
            if (mListeners.contains(listener)) {
                listener.onSignal(t);
                if (mCancelled) {
                    break;
                }
            }
        }
    }

    public void cancel() {
        mCancelled = true;
    }
}
