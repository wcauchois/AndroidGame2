package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.foundation.Image;
import net.cloudhacking.androidgame2.engine.utils.Asset;

/**
 * Created by wcauchois on 1/21/15.
 */
public class Button extends Widget {

    public Button(RectF bounds, Asset buttonTex) {
        super(bounds);
        setBackgroundImage( new Image(buttonTex) );
    }

}
