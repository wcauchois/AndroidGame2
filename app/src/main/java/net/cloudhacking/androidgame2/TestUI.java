package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.ui.element.Button;
import net.cloudhacking.androidgame2.engine.ui.UI;
import net.cloudhacking.androidgame2.engine.ui.Widget;

/**
 * Created by Andrew on 1/26/2015.
 */
public class TestUI extends UI {

    @Override
    public void create() {

        root.addToFront(
                new Button(Assets.UI_SIMPLE, 1, .07f, Widget.BindLocation.CENTER_TOP)
        );

    }

}
