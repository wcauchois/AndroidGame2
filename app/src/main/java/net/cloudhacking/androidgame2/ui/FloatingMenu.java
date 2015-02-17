package net.cloudhacking.androidgame2.ui;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.ColonyDrop;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.unit.CDUnit;

/**
 * Created by Andrew on 2/16/2015.
 */
public class FloatingMenu extends FloatingWidget {

    private CDUnit mTarget;

    public FloatingMenu(CDUnit target, RectF bounds) {
        super(bounds);
        mTarget = target;
    }


    @Override
    public void draw(BasicGLScript gls) {
        gls.useCamera(ColonyDrop.getActiveCamera());
        super.draw(gls);
        gls.useCamera(ColonyDrop.getCameraController().getUICamera());
    }

}
