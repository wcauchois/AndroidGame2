package net.cloudhacking.androidgame2.engine.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.foundation.Image;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by wcauchois on 1/22/15.
 */
public class Label extends Widget {
    public static Label create(PointF topLeft, String msg, TextRenderer.TextProps textProps) {
        Texture tex = TextRenderer.getInstance().getTexture(msg, textProps);
        RectF bounds = new RectF(topLeft.x, topLeft.y, topLeft.x + tex.getWidth(), topLeft.y + tex.getHeight());
        return new Label(tex, bounds);
    }

    public static Label create(PointF topLeft, String msg) {
        return create(topLeft, msg, TextRenderer.newProps() /* default props */);
    }

    public Label(Texture tex, RectF bounds) {
        super(bounds);
        setBackgroundImage(new Image(tex));
    }
}
