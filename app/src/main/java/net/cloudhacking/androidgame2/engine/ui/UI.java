package net.cloudhacking.androidgame2.engine.ui;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.element.Group;

/**
 * Created by Andrew on 1/26/2015.
 */
public class UI extends Group<RootWidget> {

    private UICamera mCamera;
    public RootWidget root;

    public UI() {
        mCamera = GameSkeleton.getInstance()
                              .getCameraController()
                              .getUICamera();

        root = new RootWidget();

        add(root);
    }


    public void create() {}


    @Override
    public void draw(BasicGLScript gls) {
        gls.useCamera(mCamera);
        super.draw(gls);
    }


}
