package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Level;
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
    private Level mLevel;
    private SelectableUnit mSelected;

    public UnitController(Level level) {
        mLevel = level;
        mGrid = level.grid;
        mLevel.addToFront(mGrid.SELECTOR_ICON);
        mGrid.SELECTOR_ICON.hide();
        mSelected = null;
        mDownSelect = false;
    }

    public void select(ControllableUnit u) {
        if (mSelected != null) mSelected.unSelect();
        mSelected = u;
        mSelected.select();

    }

    public void clearSelection() {
        if (mSelected != null) mSelected.unSelect();
        mGrid.SELECTOR_ICON.hide();
        mSelected = null;
    }


    //----------------------------------------------------------------------------------------------
    // Selection Handling

    private boolean mDownSelect;

    @Override
    public boolean onSignal(Object o) {
        if (o instanceof InputManager.ClickEvent) {
            return handleClickEvent((InputManager.ClickEvent) o);

        } else if (o instanceof InputManager.DragEvent) {
            return handleDragEvent((InputManager.DragEvent) o);
        }


        return false;
    }

    public boolean handleClickEvent(InputManager.ClickEvent e) {
        switch(e.getType()) {
            case DOWN:
                Grid.Cell nearest = mGrid.nearestCell(getScene().activeCameraToScene(e.getPos()));

                for (ControllableUnit u : mEntities) {
                    if (u.getLocation() == nearest) {
                        select(u);
                        mGrid.SELECTOR_ICON.startAnimationAt(nearest.getCenter());
                        mLevel.bringToFront(mGrid.SELECTOR_ICON);
                        mDownSelect = true;
                        return true;
                    }
                }
                if (mSelected != null) mSelected.unSelect();
                return false;

            case UP:
                mDownSelect = false;

            default:
                return false;
        }
    }

    public boolean handleDragEvent(InputManager.DragEvent e) {
        // if click down hits a unit in this group, an ensuing click up will select it,
        // otherwise a drag will highlight a path from the current location of the unit
        // to the target of the drag.
        switch (e.getType()) {
            case START:
                if (mDownSelect) return true;
                break;
            case UPDATE:
                // check paths
                break;
            case END:
                mDownSelect = false;
                break;
        }
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
