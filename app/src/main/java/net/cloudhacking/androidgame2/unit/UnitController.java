package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.utils.Loggable;

/**
 * Created by Andrew on 1/31/2015.
 */
public class UnitController
        extends Loggable
        implements Signal.Listener<InputManager.ClickEvent>
{



    @Override
    public boolean onSignal(InputManager.ClickEvent event) {
        return false;
    }

}
