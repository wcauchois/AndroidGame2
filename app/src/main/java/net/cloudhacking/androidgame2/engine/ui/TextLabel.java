package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.gl.TextRenderer;

/**
 * Created by wcauchois on 1/22/15.
 */
public class TextLabel extends Widget {

    public TextLabel(String text) {
        super( 0, 0,
               new Image(TextRenderer.getInstance().getTexture(text, TextRenderer.newProps()))
        );
    }
}
