package net.cloudhacking.androidgame2.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.element.NinePatch;
import net.cloudhacking.androidgame2.engine.ui.RootWidget;
import net.cloudhacking.androidgame2.engine.ui.Widget;

/**
 * Created by Andrew on 2/10/2015.
 */
public class InfoBar extends Widget {

    public InfoBar(RootWidget root, float height) {
        super( new RectF(-1,
                         root.getRootHeight()-height+1,
                         root.getRootWidth()+2,
                         root.getRootHeight()+1),
               new NinePatch(Assets.UI_WHITE)
        );
    }

}
