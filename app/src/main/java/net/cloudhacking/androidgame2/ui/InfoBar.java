package net.cloudhacking.androidgame2.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.ui.widget.Button;
import net.cloudhacking.androidgame2.engine.ui.widget.RootWidget;
import net.cloudhacking.androidgame2.engine.ui.UITouchHandler;
import net.cloudhacking.androidgame2.engine.ui.widget.Widget;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 2/10/2015.
 */
public class InfoBar extends Widget {

    private class InfoBarController extends UITouchHandler.WidgetController {
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
               Assets.UI_SIMPLE
        );
        setTouchable(true);
        setController(new InfoBarController());

        //add( new Button() );

    }

}
