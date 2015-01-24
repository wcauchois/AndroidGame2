package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;

/**
 * Created by wcauchois on 1/21/15.
 */
public class Button extends Widget {

    public Button(RectF bounds, Asset buttonTex) {
        super(bounds);
        setBackgroundImage( new Image(buttonTex) );
    }

    public Button(Asset buttonTex, float scale) {
        Texture background = AssetCache.getTexture(buttonTex);
        float w = (scale*background.getWidth()) / Widget.getRootWidth();
        float h = (scale*background.getHeight()) / Widget.getRootHeight();

        setBounds( new RectF(0, 0, w, h) );
        setBackgroundImage( new Image(background) );
    }

}
