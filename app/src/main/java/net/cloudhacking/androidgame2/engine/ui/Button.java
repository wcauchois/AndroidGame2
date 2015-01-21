package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.foundation.Image;
import net.cloudhacking.androidgame2.engine.utils.Asset;

/**
 * Created by wcauchois on 1/21/15.
 */
public class Button extends Widget {
    private Image mButtonImage;

    public Button(RectF bounds, Asset buttonTex) {
        super(bounds);
        mButtonImage = new Image(buttonTex);
        mButtonImage.setToRect(bounds);
    }

    @Override
    public void update() {
        mButtonImage.update();
    }

    @Override
    public void draw(BasicGLScript gls) {
        mButtonImage.draw(gls);
        super.draw(gls);
    }
}
