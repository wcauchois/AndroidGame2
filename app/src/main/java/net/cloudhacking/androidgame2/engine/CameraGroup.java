package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.Group;

/**
 * Created by Andrew on 1/20/2015.
 */
public class CameraGroup extends Group<Entity> {

    /**
     * Can probably eventually replace this class with a level class, ui class etc.
     */

    private Camera mCamera;

    public CameraGroup(Camera camera) {
        mCamera = camera;
    }

    @Override
    public void draw(BasicGLScript gls) {
        gls.useCamera(mCamera);
        super.draw(gls);
    }

}
