package net.cloudhacking.androidgame2.engine.ui.element;

import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.ui.Widget;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by wcauchois on 1/22/15.
 */
public class TextLabel extends Widget {
    public static TextLabel create(PointF topLeft, String msg, TextRenderer.TextProps textProps) {
        Texture tex = TextRenderer.getInstance().getTexture(msg, textProps);

        return new TextLabel(tex, BindLocation.CENTER, ScaleType.FIXED);
    }

    public static TextLabel create(PointF topLeft, String msg) {
        return create(topLeft, msg, TextRenderer.newProps() /* default props */ );
    }

    public TextLabel(Texture tex, BindLocation loc, ScaleType stype) {
        super(new Image(tex), loc, stype);
    }

    public TextLabel(String text, BindLocation loc, ScaleType stype) {
        super(new Image(
                TextRenderer.getInstance().getTexture(text, TextRenderer.newProps())),
                loc, stype
        );
    }
}
