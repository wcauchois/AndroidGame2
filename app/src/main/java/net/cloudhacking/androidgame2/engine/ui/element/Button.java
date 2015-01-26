package net.cloudhacking.androidgame2.engine.ui.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.NinePatch;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.ui.TouchWidget;
import net.cloudhacking.androidgame2.engine.ui.Widget;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.NinePatchAsset;

/**
 * Created by wcauchois on 1/21/15.
 */
public class Button extends TouchWidget {

    public Button(RectF bounds, Asset asset) {
        super(bounds, BindLocation.NULL);
        setBackgroundImage( new Image(asset) );
    }

    public Button(Asset asset, float scale) {
        Texture background = AssetCache.getInstance().getTexture(asset);
        float w = (scale*background.getWidth()) / Widget.getRootWidth();
        float h = (scale*background.getHeight()) / Widget.getRootHeight();

        setBounds(new RectF(0, 0, w, h));
        setBackgroundImage(new Image(background));
    }

    public Button(Asset asset, BindLocation loc, float scale) {
        this(asset, scale);
        setBindLocation(loc);
    }

    public Button(Asset asset, BindLocation loc) {
        this(asset, loc, 1);
    }

    public Button(Asset asset) {
        this(asset, 1);
    }

    public Button(NinePatchAsset npa, float w, float h, BindLocation loc) {
        setBindLocation(loc);
        setBounds( new RectF(0, 0, w, h) );
        float sw = w * Widget.getRootWidth();
        float sh = h * Widget.getRootHeight();
        setBackgroundImage( new NinePatch(npa, sw, sh) );
    }


    @Override
    public void onClick() {
        Widget.widgetSelector.dispatch(this);
    }

}
