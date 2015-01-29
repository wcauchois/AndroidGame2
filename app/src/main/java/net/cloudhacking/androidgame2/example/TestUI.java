package net.cloudhacking.androidgame2.example;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.ui.element.TouchPanel;
import net.cloudhacking.androidgame2.engine.ui.UI;
import net.cloudhacking.androidgame2.engine.ui.Widget.BindLocation;
import net.cloudhacking.androidgame2.engine.utils.FPSCounter;

/**
 * Created by Andrew on 1/26/2015.
 */
public class TestUI extends UI {

    @Override
    public void create() {

        TouchPanel panel = new TouchPanel(Assets.UI_SIMPLE, new RectF(0,0,1,.07f), BindLocation.NULL);
        panel.add(new FPSCounter(BindLocation.CENTER_LEFT));
        root.addToFront(panel);

    }

}
