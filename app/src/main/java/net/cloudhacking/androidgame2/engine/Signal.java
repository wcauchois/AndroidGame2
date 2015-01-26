package net.cloudhacking.androidgame2.engine;

import java.util.LinkedList;

/**
 * Created by wcauchois on 1/20/15.
 */
public class Signal<T> {

    /**
     * This class represents a generic event messenger.  On a signal from the input manager,
     * this will call onSignal(T t) for each listener.  If a listener wishes to handle this
     * signal and cancel the iteration then it can return true.
     *
     * @param <T> Class of event.
     */

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
        Listener<T>[] listeners = mListeners.toArray(new Listener[0]);

        Listener<T> listener;
        for (int i = 0; i < listeners.length; i++) {
            listener = listeners[i];
            if (mListeners.contains(listener)) {
                /**
                 * Instead of having to call back to this signal and cancel, return true for
                 * onSignal(t) if you want to handle the signal and cancel the search, or false if
                 * you don't.
                 */
                if (listener.onSignal(t)) {
                    break;
                }
            }
        }
    }
}
