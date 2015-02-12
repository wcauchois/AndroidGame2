package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.ui.widget.Widget;

/**
* Created by research on 2/12/15.
*/
public class UIEvent {
    private Widget mDispatcher;

    public UIEvent(Widget dispatcher) {
        mDispatcher = dispatcher;
    }

    public Widget getDispatcher() {
        return mDispatcher;
    }
}
