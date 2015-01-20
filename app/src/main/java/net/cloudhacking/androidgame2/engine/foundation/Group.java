package net.cloudhacking.androidgame2.engine.foundation;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/15/2015.
 */
public class Group extends Entity {
    /*
     * This is the most general class that represent a group of in-game entities.
     */
    protected ArrayList<Entity> mEntities;
    protected int length;

    public Group() {
        mEntities = new ArrayList<Entity>();
        length = 0;
    }

    @Override
    public synchronized void update() {
        for (Entity e : mEntities) {
            if (e.exists() && e.isActive()) {
                e.update();
            }
        }
    }

    @Override
    public synchronized void draw() {
        for (Entity e : mEntities) {
            if (e.exists() && e.isOnScreen() && e.isVisible()) {
                e.draw();
            }
        }
    }


    public synchronized Entity add(Entity e) {

        if (e.getParent()==this) {
            return e;
        }
        if (e.getParent()!=null) {
            e.getParent().remove(e);
        }
        for (int i=0; i<length; i++) {
            if (mEntities.get(i)==null) {
                mEntities.set(i, e);
                e.setParent(this);
                return e;
            }
        }
        mEntities.add(e);
        e.setParent(this);
        length++;
        return e;
    }


    public synchronized Entity fastRemove(Entity e) {
        int index = mEntities.indexOf(e);
        if (index != -1) {
            mEntities.set(index, null);
            e.setParent(null);
            return e;
        } else {
            return null;
        }
    }

    public synchronized Entity remove(Entity e) {
        if (mEntities.remove(e)) {
            length--;
            e.setParent(null);
            return e;
        } else {
            return null;
        }
    }


    public synchronized void clear() {
        Entity e;
        for (int i=0; i<length; i++) {
            e = mEntities.get(i);
            if (e != null) {
                e.setParent(null);
            }
        }
        mEntities.clear();
        length = 0;
    }


    public synchronized Entity bringToFront(Entity e) {
        if (mEntities.contains(e)) {
            mEntities.remove(e);
            mEntities.add(e);
            return e;
        } else {
            return null;
        }
    }

    public synchronized Entity sentToBack(Entity e) {
        if (mEntities.contains(e)) {
            mEntities.remove(e);
            mEntities.add(0, e);
            return e;
        } else {
            return null;
        }
    }

    public synchronized ArrayList<Entity> getEntities() {
        return mEntities;
    }

}
