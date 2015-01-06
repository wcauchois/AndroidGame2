package net.cloudhacking.androidgame2;

/**
 * Created by Andrew on 1/5/2015.
 */
public interface Renderable {

    /*
     * Represents anything that can be rendered.
     *
     * TODO: Implement comparability so that the render layer knows in which order to draw renderables.
     */

    // private int mRenderPriority;

    public void draw(QuadDrawer quadDrawer);

}
