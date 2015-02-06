package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.gl.Camera;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/26/2015.
 */
public abstract class Level extends Group<Entity> {

    private Camera mCamera;
    public Grid grid;
    protected RectF size;

    public PointF cam2scene(PointF camPt) {
        return getScene().getActiveCamera().cameraToScene(camPt);
    }

    public Level() {
        mCamera = GameSkeleton.getInstance()
                              .getCameraController()
                              .getActiveCamera();
    }


    abstract public void create();

    public RectF getSize() {
        return size;
    }


    @Override
    public void draw(BasicGLScript gls) {
        gls.useCamera(mCamera);
        super.draw(gls);
    }

}
