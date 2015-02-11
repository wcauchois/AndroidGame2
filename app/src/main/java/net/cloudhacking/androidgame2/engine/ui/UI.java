package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.element.Group;

/**
 * Created by Andrew on 1/26/2015.
 */
public abstract class UI extends Group<RootWidget> implements Signal.Listener {

    public Signal<Widget> widgetSelector = new Signal<Widget>();

    abstract public UITouchHandler.WidgetController getControllerFor(Widget w);


    private UICamera mCamera;
    private RootWidget mRoot;
    private UITouchHandler mHandler;

    public UI(int rootWidth, int rootHeight) {
        mCamera = GameSkeleton.getCameraController().getUICamera();
        mRoot = add( new RootWidget(rootWidth, rootHeight) );
        mHandler = new UITouchHandler(mRoot);
    }

    public RootWidget getRoot() {
        return mRoot;
    }

    public void create() {}


    @Override
    public boolean onSignal(Object o) {
        if (o instanceof InputManager.Event) {
            return mHandler.processEvent((InputManager.Event) o);
        }
        return false;
    }

    @Override
    public void draw(BasicGLScript gls) {
        gls.useCamera(mCamera);
        super.draw(gls);
    }


}
