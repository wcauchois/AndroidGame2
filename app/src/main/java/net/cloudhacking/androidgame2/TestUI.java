package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.ui.element.Button;
import net.cloudhacking.androidgame2.engine.ui.UI;
import net.cloudhacking.androidgame2.engine.ui.Widget;
import net.cloudhacking.androidgame2.engine.utils.FPSCounter;

/**
 * Created by Andrew on 1/26/2015.
 */
public class TestUI extends UI {

    @Override
    public void create() {

        Button btn = new Button(Assets.UI_SIMPLE, 1, .07f, Widget.BindLocation.CENTER_TOP);

        btn.add(new FPSCounter(Widget.BindLocation.CENTER_RIGHT) );

        root.addToFront(btn);

    }

}
