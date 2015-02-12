package net.cloudhacking.androidgame2.engine.ui.widget;

import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.ui.UIEvent;
import net.cloudhacking.androidgame2.engine.ui.UITouchHandler;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by research on 2/12/15.
 */
public class Button extends Widget {

    private class ButtonController extends UITouchHandler.WidgetController {
        @Override public void onClickDown(PointF touchPt) {
            onTouchDown();
        }
        @Override public void onClickUp(PointF touchPt) {
            onTouchUp();
        }
        @Override public void onEndDrag(PointF touchPt) {
            onTouchUp();
        }
    }


    public Signal<UIEvent> click;

    public Button(float x, float y, WidgetBackground bg) {
        super(x, y, bg);
        setTouchable(true);
        setController(new ButtonController());
        click = new Signal<UIEvent>();
    }


    protected void onTouchDown() {}

    protected void onTouchUp() {
        dispatchEvent();
    }

    protected void dispatchEvent() {
        click.dispatch(new UIEvent(this));
    }


}
