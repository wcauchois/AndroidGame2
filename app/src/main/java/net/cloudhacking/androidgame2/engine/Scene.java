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

    private Level mLevel;
    private UI mUI;

    public void setLevel(Level level) {
        mLevel = level;
    }

    public Level getLevel() {
        return mLevel;
    }

    public void setUI(UI ui) {
        mUI = ui;
    }

    public UI getUI() {
        return mUI;
    }


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
        mLevel.update();
        mUI.update();
    }

    public void draw(BasicGLScript gls) {
        mLevel.draw(gls);
        mUI.draw(gls);
    }

}
