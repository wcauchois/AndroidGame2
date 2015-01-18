package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

/**
 * Created by Andrew on 1/15/2015.
 */
public abstract class Scene extends Group {
    /**
     * This class represents the most encompassing group of in-game entities and other groups. Its
     * draw() and update() methods will be called from the main game thread.
     */

    public Scene() {};

    abstract public float getMapWidth();
    abstract public float getMapHeight();
    abstract public RectF getMapRect();

    abstract public void create();

}
