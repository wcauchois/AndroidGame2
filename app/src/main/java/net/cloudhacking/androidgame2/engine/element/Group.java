package net.cloudhacking.androidgame2.engine.element;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/15/2015.
 */
public class Group<E extends Entity> extends Entity {

    /*
     * This is the most general class that represents a group of in-game entities.
     */

    protected ArrayList<E> mEntities;
    protected int length;

    private ArrayList<E> mAddQueue;  // If an entity which is an existing member of this group
                                     // needs to add a new entity to this group during it's
                                     // update, then it will need to queue the add using
                                     // queueAdd(E e) in order to prevent a concurrent
                                     // modification exception.
    private ArrayList<E> mBTFQueue;     // bring-to-front queue
    private ArrayList<E> mRemQueue;     // remove-queue

    public Group() {
        mEntities = new ArrayList<E>();
        length = 0;

        mAddQueue = new ArrayList<E>();
        mBTFQueue = new ArrayList<E>();
        mRemQueue = new ArrayList<E>();
    }

    @Override
    public void update() {

        for (Entity e : mEntities) {
            if (e != null && e.exists() && e.isActive()) {
                e.update();
            }
        }

        // add items queued for action during update
        for (E e : mAddQueue) {
            add(e);
            e.update();
        }
        for (E e : mBTFQueue) bringToFront(e);
        for (E e : mRemQueue) fastRemove(e);

        mAddQueue.clear();
        mBTFQueue.clear();
        mRemQueue.clear();
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

                e.onAdd();
                return e;
            }
        }
        mEntities.add(e);
        e.setParent(this);
        length++;

        e.onAdd();
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

        e.onAdd();
        return e;
    }


    // use this if trying to add an element to a group
    // within the update loop (see above for more info)
    public E queueAdd(E e) {
        mAddQueue.add(e);
        return e;
    }


    public E fastRemove(E e) {
        int index = mEntities.indexOf(e);
        if (index != -1) {
            mEntities.set(index, null);

            e.onRemove();
            e.setParent(null);
            return e;
        } else {
            return null;
        }
    }

    public E remove(E e) {
        if (mEntities.remove(e)) {
            length--;

            e.onRemove();
            e.setParent(null);
            return e;
        } else {
            return null;
        }
    }

    public E queueRemove(E e) {
        mRemQueue.add(e);
        return e;
    }


    public void clear() {
        E e;
        for (int i=0; i<length; i++) {
            e = mEntities.get(i);
            if (e != null) {

                e.onRemove();
                e.setParent(null);
            }
        }
        mEntities.clear();
        length = 0;
    }


    public E bringToFront(E e) {
        if (mEntities.contains(e)) {
            mEntities.remove(e);
            mEntities.add(e);
            return e;
        } else {
            return null;
        }
    }

    public E queueBringToFront(E e) {
        mBTFQueue.add(e);
        return e;
    }

    public E sentToBack(E e) {
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
