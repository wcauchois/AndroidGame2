package net.cloudhacking.androidgame2.ui;

import net.cloudhacking.androidgame2.engine.ui.widget.RootWidget;
import net.cloudhacking.androidgame2.engine.ui.UI;
import net.cloudhacking.androidgame2.engine.utils.FPSCounter;

/**
 * Created by Andrew on 1/28/2015.
 */
public class GameUI extends UI {

    public GameUI(int w, int h) {
        super(w, h);
    }

    @Override
    public void create() {
        RootWidget root = getRoot();

        root.add(new FPSCounter(10, 10));
        root.add(new InfoBar(root, 100));
        
    }

}
