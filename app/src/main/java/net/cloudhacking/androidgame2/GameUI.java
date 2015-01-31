package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.ui.UI;
import net.cloudhacking.androidgame2.engine.ui.Widget;
import net.cloudhacking.androidgame2.engine.ui.Widget.BindLocation;
import net.cloudhacking.androidgame2.engine.ui.Widget.ScaleType;
import net.cloudhacking.androidgame2.engine.ui.element.Button;
import net.cloudhacking.androidgame2.engine.ui.element.TextLabel;
import net.cloudhacking.androidgame2.engine.utils.FPSCounter;

/**
 * Created by Andrew on 1/28/2015.
 */
public class GameUI extends UI {

    @Override
    public void create() {

        root.add(new FPSCounter(Widget.BindLocation.TOP_LEFT));

        TextLabel btnLabel = new TextLabel("BUILD", BindLocation.CENTER, ScaleType.FIXED);
        Button btn = new Button(Assets.UI_PURPLE, btnLabel, BindLocation.CENTER_BOTTOM);
        root.add(btn);
        btn.scaleToWidget(btnLabel);



    }

}
