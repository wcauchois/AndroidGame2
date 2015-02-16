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

        // TODO: do pop-up menus for units that appear over their heads, for example you could
        // TODO: click "attack" and then drag a red attack reticle to an enemy unit (the menu
        // TODO: would disappear during the drag).
        
    }

}
