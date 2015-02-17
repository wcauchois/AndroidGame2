package net.cloudhacking.androidgame2.engine.ui.widget;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;

/**
 * Created by Andrew on 1/24/2015.
 */
public interface WidgetBackground {

    public void setToRect(RectF rect);
    public void setScale(float s);
    public RectF getRect();
    public void update();
    public void draw(BasicGLScript gls);

}
