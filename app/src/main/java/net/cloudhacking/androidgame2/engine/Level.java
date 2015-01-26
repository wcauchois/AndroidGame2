package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.Group;

/**
 * Created by Andrew on 1/26/2015.
 */
public abstract class Level extends Group<Entity> {

    private Camera mCamera;

    public Level() {
        mCamera = GameSkeleton.getInstance()
                              .getCameraController()
                              .getActiveCamera();
    }


    abstract public void create();

    abstract public RectF getSize();


    @Override
    public void draw(BasicGLScript gls) {
        gls.useCamera(mCamera);
        super.draw(gls);
    }

}
