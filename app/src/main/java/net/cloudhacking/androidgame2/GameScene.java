package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.ui.UI;
import net.cloudhacking.androidgame2.example.TestLevel;

/**
 * Created by Andrew on 1/28/2015.
 */
public class GameScene extends Scene {

    public Level level;
    public UI ui;

    @Override
    public Scene create() {

        ui    = new GameUI();
        level = new TestLevel();

        ui.create();
        level.create();

        // in render order
        add(level);
        add(ui);

        getActiveCamera().setBoundaryRect(level.getSize());
        return this;
    }

    @Override
    public void destroy() {

    }

}
