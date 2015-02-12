package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.ui.widget.RootWidget;
import net.cloudhacking.androidgame2.engine.ui.widget.Widget;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/26/2015.
 */
public abstract class UI extends Group<RootWidget> implements Signal.Listener {

    private UICamera mCamera;
    private RootWidget mRoot;
    private UITouchHandler mHandler;
    private Widget mSelected;

    public UI(int rootWidth, int rootHeight) {
        mCamera = GameSkeleton.getCameraController().getUICamera();
        mRoot = add( new RootWidget(rootWidth, rootHeight) );
        mHandler = new UITouchHandler(this);
        mSelected = null;
    }

    public RootWidget getRoot() {
        return mRoot;
    }

    public Widget getSelected() {
        return mSelected;
    }

    public void clearSelection() {
        mSelected = null;
    }

    abstract public void create();


    @Override
    public boolean onSignal(Object o) {
        if (o instanceof InputManager.Event) {
            return mHandler.processEvent((InputManager.Event) o);
        }
        return false;
    }

    public UITouchHandler.WidgetController selectWidgetOnTouch(PointF touch) {
        mSelected = mRoot.getWidgetOnTouch(touch);
        return mSelected.getController();
    }


    @Override
    public void draw(BasicGLScript gls) {
        gls.useCamera(mCamera);
        super.draw(gls);
    }


}
