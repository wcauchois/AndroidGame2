package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.ui.UI;

/**
 * Created by Andrew on 1/15/2015.
 */
public abstract class Scene extends Group<Entity> {

    abstract public Scene create();
    abstract public void destroy();

    public Level level;
    public UI ui;
    private boolean mCreated;

    public Scene() {
        mCreated = false;
    }

    // called from main game thread
    public void start() {
        create();
        mCreated = true;
    }

    public boolean isCreated() {
        return mCreated;
    }


    public void update() {
        level.update();
        ui.update();
    }

    public void draw(BasicGLScript gls) {
        level.draw(gls);
        ui.draw(gls);
    }

}
