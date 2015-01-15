package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.engine.utils.Loggable;

/**
 * Created by Andrew on 1/15/2015.
 */
public class Entity extends Loggable {
    /**
     * This is the most general class that represents an in-game item.
     */

    private boolean mVisible;
    private boolean mActive;
    private boolean mExists;

    private Group mParent;

    public Entity () {
        mVisible = true;
        mActive  = true;
        mExists  = true;
    }


    public boolean isVisible() {
        if (mParent != null) {
            return mParent.isVisible() && mVisible;
        }
        return mVisible;
    }

    public boolean isActive() {
        if (mParent != null) {
            return mParent.isActive() && mActive;
        }
        return mActive;
    }

    public boolean exists() {
        return mExists;
    }

    public Group getParent() {
        return mParent;
    }

    public void setVisibility(boolean bool) {
        mVisible = bool;
    }

    public void setActive(boolean bool) {
        mActive = bool;
    }

    public void setExistence(boolean bool) {
        mExists = bool;
    }

    public void setParent(Group parent) {
        mParent = parent;
    }

    public void update() {}

    public void draw() {}
}
