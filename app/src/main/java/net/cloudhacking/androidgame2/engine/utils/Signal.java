package net.cloudhacking.androidgame2.engine.utils;

import java.util.LinkedList;

/**
 * Created by wcauchois on 1/20/15.
 */
public class Signal<T> {
    public static interface Listener<T> {
        public boolean onSignal(T t);
    }

    private LinkedList<Listener<T>> mListeners = new LinkedList<Listener<T>>();

    public void connect(Listener<T> listener) {
        mListeners.addLast(listener);
    }

    public void remove(Listener<T> listener) {
        mListeners.remove(listener);
    }

    public void dispatch(T t) {
        // TODO: whats the point of doing all this instead of iterating over the linked list?
        Listener<T>[] listeners = mListeners.toArray(new Listener[0]);

        Listener<T> listener;
        for (int i = 0; i < listeners.length; i++) {
            listener = listeners[i];
            if (mListeners.contains(listener)) {
                /**
                 * Instead of having to call back to this signal and cancel, onSignal(t) could
                 * just return true if you want to handle the signal and cancel, or false if
                 * you don't.  TODO: LMK if you like this idea Bill because I think its way easier.
                 */
                if (listener.onSignal(t)) {
                    break;
                }
            }
        }
    }
}
