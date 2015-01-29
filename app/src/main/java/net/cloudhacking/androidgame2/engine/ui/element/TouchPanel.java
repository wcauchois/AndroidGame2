package net.cloudhacking.androidgame2.engine.ui.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.NinePatch;
import net.cloudhacking.androidgame2.engine.ui.TouchWidget;
import net.cloudhacking.androidgame2.engine.ui.Widget;
import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;

/**
 * Created by wcauchois on 1/21/15.
 */
public class TouchPanel extends TouchWidget {

    public TouchPanel(NinePatchAsset npa, RectF bounds, BindLocation loc) {
        super( new NinePatch(npa), bounds, loc );
    }

}
