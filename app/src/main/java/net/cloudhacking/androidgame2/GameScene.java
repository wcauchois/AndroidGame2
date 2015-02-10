package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.Scene;

/**
 * Created by Andrew on 1/28/2015.
 */
public class GameScene extends Scene {

    @Override
    public Scene create() {

        // init UI
        ui = new GameUI();
        ui.create();
        ColonyDrop.getInputManager().clickUI.connect(ui.root);

        // init level
        level = new PilotLevel();
        level.create();

        ColonyDrop.getActiveCamera().setBoundaryRect(level.getSize());
        return this;
    }

    @Override
    public void destroy() {

    }

}
