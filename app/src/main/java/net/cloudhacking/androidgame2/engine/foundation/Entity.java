package net.cloudhacking.androidgame2.engine.foundation;

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
        mVisible  = true;
        mActive   = true;
        mExists   = true;
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

    public boolean isOnScreen() {
        return false;
    }

    public boolean exists() {
        return mExists;
    }


    public void setVisibility(boolean bool) {
        mVisible = bool;
    }

    public void setActive() {
        mActive = true;
    }

    public void setInactive() {
        mActive = false;
    }

    public void kill() {
        setInactive();
        mExists = false;
    }

    public void revive() {
        mExists = true;
        setActive();
    }


    public Group getParent() {
        return mParent;
    }

    public void setParent(Group parent) {
        mParent = parent;
    }

    public void orphan() {
        mParent.remove(this);
        mParent = null;
    }


    public void update() {}

    public void draw() {}
}