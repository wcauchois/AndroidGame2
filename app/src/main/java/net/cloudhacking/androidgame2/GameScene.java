package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.ui.UI;

/**
 * Created by Andrew on 1/28/2015.
 */
public class GameScene extends Scene {

    public Level level;
    public UI ui;

    @Override
    public Scene create() {

        ui    = new GameUI();
        level = new PilotLevel();

        // in render order
        add(level);
        add(ui);

        ui.create();
        level.create();


        // TODO: connect all listeners to input manager here
        // TODO: in order to organize hit test order
        inputManager.click.connect(0, ui.root);
        //inputManager.click.connect(1, level.grid.getClickListener());


        getActiveCamera().setBoundaryRect(level.getSize());
        return this;
    }

    @Override
    public void destroy() {

    }

}
