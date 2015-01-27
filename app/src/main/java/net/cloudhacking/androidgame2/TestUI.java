package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.ui.element.Panel;
import net.cloudhacking.androidgame2.engine.ui.UI;
import net.cloudhacking.androidgame2.engine.ui.Widget.BindLocation;
import net.cloudhacking.androidgame2.engine.utils.FPSCounter;

/**
 * Created by Andrew on 1/26/2015.
 */
public class TestUI extends UI {

    @Override
    public void create() {

        //Panel panel = new Panel(Assets.UI_SIMPLE, 1f, .07f, BindLocation.CENTER_TOP);
        //root.addToFront(panel);

        root.add(new FPSCounter(BindLocation.TOP_LEFT));
    }

}
