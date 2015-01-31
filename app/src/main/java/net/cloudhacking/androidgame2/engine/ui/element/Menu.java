package net.cloudhacking.androidgame2.engine.ui.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.ui.TouchWidget;
import net.cloudhacking.androidgame2.engine.ui.WidgetBackground;

/**
 * Created by research on 1/30/15.
 */
public class Menu extends TouchWidget {

    public Signal<MenuItem> itemSelector = new Signal<MenuItem>();


    public Menu(WidgetBackground bg, RectF bounds, BindLocation loc) {
        super(bg, bounds, loc);
    }

}
