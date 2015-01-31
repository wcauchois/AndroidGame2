package net.cloudhacking.androidgame2.engine;

import android.graphics.Rect;

import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.gl.Camera;
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


    protected InputManager inputManager;
    protected CameraController cameraController;

    private boolean mCreated;

    public Scene() {
        inputManager = GameSkeleton.getInstance().getInputManager();
        cameraController = GameSkeleton.getInstance().getCameraController();
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
        return inputManager;
    }

    public CameraController getCameraController() {
        return cameraController;
    }

    public Camera getActiveCamera() {
        return cameraController.getActiveCamera();
    }

    public PointF activeCameraToScene(PointF cameraTouch) {
        return getActiveCamera().cameraToScene(cameraTouch);
    }

    public Camera getUICamera() {
        return cameraController.getUICamera();
    }

    public Rect getViewport() {
        return GameSkeleton.getInstance().getViewport();
    }



}
