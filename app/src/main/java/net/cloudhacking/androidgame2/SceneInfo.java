package net.cloudhacking.androidgame2;

/**
 * Created by wcauchois on 1/4/15.
 */
public interface SceneInfo {

    public float getSceneScale();

    public float[] getProjection();

    public float[] getProjectionCamera();

    public int getViewportWidth();

    public int getViewportHeight();

    public void update();

    public void reset(int viewportWidth, int viewportHeight, float sceneScale);
}
