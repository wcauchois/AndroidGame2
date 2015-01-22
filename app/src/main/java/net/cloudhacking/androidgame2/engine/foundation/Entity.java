package net.cloudhacking.androidgame2.engine.foundation;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.Scene;
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
        return true;
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

    public Scene getScene() {
        // Traverses up the parent tree and returns the top level group
        // if it's an instance of Scene, otherwise returns null.
        if (mParent != null) {
            return mParent.getScene();
        } else if (this instanceof Scene) {
            return (Scene)this;
        } else {
            return null;
        }
    }

    public <T extends Group> T getParentGroupOfClass(Class<T> groupCls) {
        // Traverses up the parent tree to get the first group of the given class
        // which contains this element.  (sorry for the reflection Bill, but it might
        // be a useful function)
        if (groupCls.isInstance(mParent)) {
            return (T)mParent;
        } else if (mParent == null) {
            return null;
        } else {
            return mParent.getParentGroupOfClass(groupCls);
        }
    }

    public void setParent(Group parent) {
        mParent = parent;
    }

    public void orphan() {
        // can't use .remove() because it will cause a concurrent
        // modification exception if called within the update loop.
        mParent.fastRemove(this);
    }


    public void update() {}

    public void draw(BasicGLScript gls) {}


    // test to make sure orphaned entities really get garbage-collected
    @Override
    public void finalize() {
        //d("Entity being deleted : " + this);
    }
}
