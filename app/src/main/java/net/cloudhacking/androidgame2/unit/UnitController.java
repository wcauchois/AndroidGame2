package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Group;

/**
 * Created by Andrew on 1/31/2015.
 */
public class UnitController
        extends Group<ControllableUnit>
        implements Signal.Listener
{

    private Grid mGrid;
    private SelectableUnit mSelected;

    public UnitController(Grid grid) {
        mGrid = grid;
        mSelected = null;
    }

    public void select(ControllableUnit u) {
        if (mSelected != null) mSelected.unSelect();
        mSelected = u;
        mSelected.select();
    }

    public void clearSelection() {
        if (mSelected != null) mSelected.unSelect();
        mSelected = null;
    }

    @Override
    public boolean onSignal(Object o) {
        if (o instanceof Grid.Cell) {
            return handleCellSelection((Grid.Cell) o);

        } else if (o instanceof InputManager.DragEvent) {
            return handleDragEvent((InputManager.DragEvent) o);
        }


        return false;
    }

    public boolean handleCellSelection(Grid.Cell selected) {
        for (ControllableUnit u : mEntities) {
            if (u.getLocation() == selected) {
                select(u);
                return true;
            }
        }
        if (mSelected != null) mSelected.unSelect();
        return false;
    }

    public boolean handleDragEvent(InputManager.DragEvent e) {
        return false;
    }


    @Override
    public void update() {
        for (Unit u : mEntities) {
            u.setLocation(mGrid.nearestCell(u.getPos()));
        }
        super.update();
    }

}
