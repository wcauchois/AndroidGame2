package net.cloudhacking.androidgame2.engine.ui.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;

/**
 * Created by Andrew on 1/28/2015.
 */
public class Button extends TouchPanel {

    public Button(NinePatchAsset asset, String text, float width, float height, BindLocation loc) {
        super(asset, new RectF(0, 0, width, height), loc);
        add(new TextLabel(text, BindLocation.CENTER, ScaleType.FIT_FIXED_RATIO));
    }

    public Button(NinePatchAsset asset, TextLabel label, BindLocation loc) {
        super(asset, new RectF(0, 0, 1, 1), loc);
        add(label);
    }

}
