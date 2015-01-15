package net.cloudhacking.androidgame2.engine;

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
    public void update() {
        for (Entity e : mEntities) {
            if (e.exists() && e.isActive()) {
                e.update();
            }
        }
    }

    @Override
    public void draw() {
        for (Entity e : mEntities) {
            if (e.exists() && e.isVisible()) {
                e.draw();
            }
        }
    }

}
