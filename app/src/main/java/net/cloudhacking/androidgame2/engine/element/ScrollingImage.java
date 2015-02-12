package net.cloudhacking.androidgame2.engine.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.ui.WidgetBackground;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by research on 2/11/15.
 */
public class ScrollingImage extends Image implements WidgetBackground {

    public ScrollingImage(Asset asset, int frameW, int frameH, PointF uvPos) {
        super(asset,
                new RectF(uvPos.x, uvPos.y, uvPos.x + frameW, uvPos.y + frameH)
        );
    }

}
