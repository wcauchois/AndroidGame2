package net.cloudhacking.androidgame2.engine.element;

import net.cloudhacking.androidgame2.engine.ui.widget.WidgetBackground;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.CommonUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by research on 2/11/15.
 */
public class ScrollingImage extends Image implements WidgetBackground {

    public ScrollingImage(Asset asset, int frameW, int frameH, PointF uvPos) {
        super( asset, CommonUtils.makeRect(uvPos, frameW, frameH) );
    }

}
