package net.cloudhacking.androidgame2.engine.ui.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;

/**
 * Created by Andrew on 1/28/2015.
 */
public class TopBar extends TouchPanel {

    public TopBar(NinePatchAsset asset, float height) {
        super(asset, new RectF(0,0,1,height), BindLocation.NULL);
    }

}
