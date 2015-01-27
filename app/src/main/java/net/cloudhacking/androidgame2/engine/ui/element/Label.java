package net.cloudhacking.androidgame2.engine.ui.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.ui.Widget;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by wcauchois on 1/22/15.
 */
public class Label extends Widget {
    public static Label create(PointF topLeft, String msg, TextRenderer.TextProps textProps) {
        Texture tex = TextRenderer.getInstance().getTexture(msg, textProps);

        return new Label(tex, BindLocation.CENTER, ScaleType.FIXED);
    }

    public static Label create(PointF topLeft, String msg) {
        return create(topLeft, msg, TextRenderer.newProps() /* default props */ );
    }

    public Label(Texture tex, BindLocation loc, ScaleType stype) {
        super(new Image(tex), loc, stype);
    }
}
