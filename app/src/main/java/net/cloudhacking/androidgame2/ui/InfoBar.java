package net.cloudhacking.androidgame2.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.element.NinePatch;
import net.cloudhacking.androidgame2.engine.ui.RootWidget;
import net.cloudhacking.androidgame2.engine.ui.UITouchHandler;
import net.cloudhacking.androidgame2.engine.ui.Widget;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 2/10/2015.
 */
public class InfoBar extends Widget {

    private class Controller extends UITouchHandler.WidgetController {
        @Override
        public void onClickUp(PointF touchPt) {
            d("info bar clicked!");
        }
    }


    public InfoBar(RootWidget root, float height) {
        super( new RectF(-2,
                         root.getRootHeight()-height+2,
                         root.getRootWidth()+4,
                         root.getRootHeight()+2),
               new NinePatch(Assets.UI_SIMPLE)
        );
        setTouchable(true);
        setController(new Controller());
    }

}
