package net.cloudhacking.androidgame2.example;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.Scene;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    private TestLevel mLevel;
    private TestUI mUI;


    @Override
    public TestScene create() {

        mLevel = new TestLevel();
        mUI = new TestUI();

        add(mLevel);
        add(mUI);

        mLevel.create();
        mUI.create();

        getActiveCamera().setBoundaryRect(mLevel.getSize());
        return this;
    }

    @Override
    public void destroy() {

    }


    @Override
    public void update() {

        super.update();
    }

    @Override
    public void draw(BasicGLScript gls) {

        super.draw(gls);
    }

}
