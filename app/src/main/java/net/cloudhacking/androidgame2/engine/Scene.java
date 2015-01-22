package net.cloudhacking.androidgame2.engine;

import android.graphics.Rect;

import net.cloudhacking.androidgame2.engine.foundation.Entity;
import net.cloudhacking.androidgame2.engine.foundation.Group;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/15/2015.
 */
public abstract class Scene extends Group<Entity> {

    /**
     * This class represents the most encompassing group of in-game entities and other groups. Its
     * draw() and update() methods will be called directly from the main game thread.
     */

    abstract public Scene create();
    abstract public void destroy();


    private InputManager im;
    private CameraController cc;

    private boolean mCreated;

    public Scene() {
        im  = GameSkeleton.getInstance().getInputManager();
        cc  = GameSkeleton.getInstance().getCameraController();
        mCreated = false;
    }

    // called from main game thread
    public void start() {
        create();
        mCreated = true;
    }

    public boolean isCreated() {
        return mCreated;
    }


    /**
     * Some convenience functions
     */

    public InputManager getInputManager() {
        return im;
    }

    public CameraController getCameraController() {
        return cc;
    }

    public Camera getActiveCamera() {
        return cc.getActiveCamera();
    }

    public PointF activeCameraToScene(PointF cameraTouch) {
        return getActiveCamera().cameraToScene(cameraTouch);
    }

    public Camera getUICamera() {
        return cc.getUICamera();
    }

    public Rect getViewport() {
        return GameSkeleton.getInstance().getViewport();
    }



}
