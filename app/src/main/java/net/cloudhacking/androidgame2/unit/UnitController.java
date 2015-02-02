package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Group;

/**
 * Created by Andrew on 1/31/2015.
 */
public class UnitController
        extends Group<SelectableUnit>
        implements Signal.Listener<Grid.Cell>
{
    private SelectableUnit mSelected;

    public UnitController() {
        mSelected = null;
    }

    @Override
    public boolean onSignal(Grid.Cell selected) {
        for (SelectableUnit u : mEntities) {
            if (u.getLocation() == selected) {
                mSelected.unSelect();
                mSelected = u;
                mSelected.select();
                return true;
            }
        }
        return false;
    }

}
