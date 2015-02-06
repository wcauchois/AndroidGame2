package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.PilotLevel;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.element.shape.PixelLines;
import net.cloudhacking.androidgame2.engine.utils.CellPathAnim;

/**
 * Created by Andrew on 1/31/2015.
 */
public class UnitController
        extends Group<ControllableUnit>
        implements Signal.Listener
{

    private Grid mGrid;
    private PilotLevel mLevel;
    private SelectableUnit mSelected;

    public UnitController(PilotLevel level) {
        mLevel = level;
        mGrid = level.grid;
        mLevel.add(mGrid.SELECTOR_ICON);
        mGrid.SELECTOR_ICON.hide();

        mSelected = null;
        mDownSelect = false;
        mPathAnim = new CellPathAnim(2.0f, new float[] {1,0,0,1});
        mLastNearest = null;
        mLevel.add(mPathAnim);
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
    private Grid.PathFinder mPathFinder;
    private Grid.CellPath mCurrentPath;
    private Grid.Cell mLastNearest;
    private CellPathAnim mPathAnim;

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
                Grid.Cell nearest = mGrid.nearestCell(mLevel.cam2scene(e.getPos()));

                for (ControllableUnit u : mEntities) {
                    if (u.getLocation() == nearest) {
                        select(u);
                        mDownSelect = true;
                        mPathFinder = mGrid.getPathFinder(u.getLocation());

                        mLevel.bringToFront(mPathAnim);
                        mLevel.bringToFront(u);
                        mGrid.SELECTOR_ICON.startAnimationAt(nearest.getCenter());
                        mLevel.bringToFront(mGrid.SELECTOR_ICON);

                        getScene().getCameraController().setDisabled(true);
                        return true;
                    }
                }
                if (mSelected != null) mSelected.unSelect();
                return false;

            case UP:
                mDownSelect = false;
                getScene().getCameraController().setDisabled(false);

        }
        return false;
    }

    public boolean handleDragEvent(InputManager.DragEvent e) {
        // if click down hits a unit in this group, an ensuing click up will select it,
        // otherwise a drag will highlight a path from the current location of the unit
        // to the target of the drag.
        switch (e.getType()) {
            case START:
                if (mDownSelect) {
                    return true;
                }
                break;

            case UPDATE:
                if (!mDownSelect) return false;

                // find nearest cell to drag point and update path as it changes
                Grid.Cell nearest = mGrid.nearestCell(mLevel.cam2scene(e.getPos()));
                if (nearest != mLastNearest) {
                    mLastNearest = nearest;
                    mCurrentPath = mPathFinder.getPathTo(nearest);
                    if (mCurrentPath != null) {
                        mPathAnim.setPath(mCurrentPath);
                    }
                }
                return true;

            case END:
                mDownSelect = false;
                getScene().getCameraController().setDisabled(false);
                return true;

            default:
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
