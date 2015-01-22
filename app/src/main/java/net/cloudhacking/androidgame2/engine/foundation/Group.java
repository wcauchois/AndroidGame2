package net.cloudhacking.androidgame2.engine.foundation;

import net.cloudhacking.androidgame2.engine.BasicGLScript;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/15/2015.
 */
public class Group<E extends Entity> extends Entity {
    /*
     * This is the most general class that represent a group of in-game entities.
     */
    protected ArrayList<E> mEntities;
    protected int length;

    public Group() {
        mEntities = new ArrayList<E>();
        length = 0;
    }

    @Override
    public void update() {
        for (Entity e : mEntities) {
            if (e != null && e.exists() && e.isActive()) {
                e.update();
            }
        }
    }

    @Override
    public void draw(BasicGLScript gls) {
        for (Entity e : mEntities) {
            if (e != null && e.exists() && e.isVisible() && e.isOnScreen()) {
                e.draw(gls);
            }
        }
    }


    public E add(E e) {
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

    
    public E addToFront(E e) {
        if (e.getParent()==this) {
            return e;
        }
        if (e.getParent()!=null) {
            e.getParent().remove(e);
        }
        mEntities.add(e);
        e.setParent(this);
        length++;
        return e;
    }


    public E fastRemove(E e) {
        int index = mEntities.indexOf(e);
        if (index != -1) {
            mEntities.set(index, null);
            e.setParent(null);
            return e;
        } else {
            return null;
        }
    }

    public E remove(E e) {
        if (mEntities.remove(e)) {
            length--;
            e.setParent(null);
            return e;
        } else {
            return null;
        }
    }


    public void clear() {
        E e;
        for (int i=0; i<length; i++) {
            e = mEntities.get(i);
            if (e != null) {
                e.setParent(null);
            }
        }
        mEntities.clear();
        length = 0;
    }


    public Entity bringToFront(E e) {
        if (mEntities.contains(e)) {
            mEntities.remove(e);
            mEntities.add(e);
            return e;
        } else {
            return null;
        }
    }

    public Entity sentToBack(E e) {
        if (mEntities.contains(e)) {
            mEntities.remove(e);
            mEntities.add(0, e);
            return e;
        } else {
            return null;
        }
    }

    public ArrayList<E> getEntities() {
        return mEntities;
    }

}
