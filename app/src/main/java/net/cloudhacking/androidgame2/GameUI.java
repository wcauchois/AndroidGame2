package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.ui.UI;
import net.cloudhacking.androidgame2.engine.utils.FPSCounter;
import net.cloudhacking.androidgame2.ui.InfoBar;

/**
 * Created by Andrew on 1/28/2015.
 */
public class GameUI extends UI {

    public GameUI(int w, int h) {
        super(w, h);
    }

    @Override
    public void create() {
        getRoot().add(new FPSCounter(10, 10));
        getRoot().add(new InfoBar(getRoot(), 20));
    }

}
