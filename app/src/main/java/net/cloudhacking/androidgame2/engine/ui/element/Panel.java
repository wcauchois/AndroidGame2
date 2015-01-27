package net.cloudhacking.androidgame2.engine.ui.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.NinePatch;
import net.cloudhacking.androidgame2.engine.ui.TouchWidget;
import net.cloudhacking.androidgame2.engine.ui.Widget;
import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;

/**
 * Created by wcauchois on 1/21/15.
 */
public class Panel extends TouchWidget {

    public Panel(NinePatchAsset npa, float w, float h, BindLocation loc) {
        super( new NinePatch(npa), new RectF(0, 0, w, h), loc );
    }


    @Override
    public void onClick() {
        Widget.widgetSelector.dispatch(this);
    }

}
