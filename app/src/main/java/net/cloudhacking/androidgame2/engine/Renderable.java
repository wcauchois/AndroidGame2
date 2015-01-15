package net.cloudhacking.androidgame2.engine;

/**
 * Created by Andrew on 1/5/2015.
 */
public interface Renderable {

    /*
     * Represents anything that can be rendered.
     *
     * TODO: Implement comparability so that the render layer knows in which order to draw renderables.
     */

    public void draw(QuadDrawer quadDrawer);

}
