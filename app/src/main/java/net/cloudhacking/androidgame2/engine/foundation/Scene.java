package net.cloudhacking.androidgame2.engine.foundation;

import android.graphics.Rect;
import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.Camera;
import net.cloudhacking.androidgame2.engine.CameraController;
import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/15/2015.
 */
public abstract class Scene extends Group {
    /**
     * This class represents the most encompassing group of in-game entities and other groups. Its
     * draw() and update() methods will be called from the main game thread.
     */
    abstract public void create();
    abstract public void destroy();

    abstract public float getMapWidth();
    abstract public float getMapHeight();
    abstract public RectF getMapRect();


    private InputManager im;
    private CameraController cc;

    public Scene() {
        im  = GameSkeleton.getInstance().getInputManager();
        cc  = GameSkeleton.getInstance().getCameraController();
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
