package net.cloudhacking.androidgame2;

import android.graphics.Rect;

import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.ui.GameUI;

/**
 * Created by Andrew on 1/28/2015.
 */
public class GameScene extends Scene {

    @Override
    public Scene create() {
        Rect viewport = ColonyDrop.getViewport();

        // init UI
        setUI( new GameUI(viewport.width(), viewport.height()) );
        getUI().create();
        ColonyDrop.getInputManager().clickUI.connect( getUI() );
        ColonyDrop.getInputManager().dragUI.connect( getUI() );

        // init level
        setLevel( new CDLevel() );
        getLevel().create();
        ColonyDrop.getActiveCamera().setBoundaryRect( getLevel().getSize() );

        return this;
    }

    @Override
    public void destroy() {}

}
