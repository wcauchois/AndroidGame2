package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.PilotLevel;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Group;
import net.cloudhacking.androidgame2.engine.utils.CellPathAnim;

/**
 * Created by Andrew on 1/31/2015.
 */
public class GridUnitController
        extends Group<GridUnit>
        implements Signal.Listener
{

    private Grid mGrid;
    private PilotLevel mLevel;
    private GridUnit mSelected;

    public GridUnitController(PilotLevel level) {
        mLevel = level;
        mGrid = level.grid;
        mLevel.add(mGrid.SELECTOR_ICON);
        mGrid.SELECTOR_ICON.hide();

        mSelected = null;
        mDownSelect = false;

        initPathFinder();
    }

    public void select(GridUnit u) {
        if (mSelected != null) mSelected.unSelect();
        mSelected = u;
        mSelected.select();
        mGrid.SELECTOR_ICON.unhide();
    }

    public void clearSelection() {
        if (mSelected != null) mSelected.unSelect();
        mGrid.SELECTOR_ICON.hide();
        mSelected = null;
    }


    //----------------------------------------------------------------------------------------------
    // Selection Handling

    private void initPathFinder() {
        mPathFinder = mGrid.getPathFinder();
        mPathFinderAnim = new CellPathAnim(3.0f, new float[] {1,0,0,1});
        mPathFinderAnim.hide();
        mLastNearest = null;
        mLevel.add(mPathFinderAnim);
    }

    private boolean mDownSelect;
    private Grid.PathFinder mPathFinder;
    private Grid.CellPath mCurrentPath;
    private Grid.Cell mLastNearest;
    private CellPathAnim mPathFinderAnim;

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

                for (GridUnit u : mEntities) {
                    if (u.getLocation() == nearest) {
                        select(u);
                        mDownSelect = true;
                        mPathFinder.setSource(mSelected.getLocation());

                        //mLevel.bringToFront(mPathFinderAnim);
                        mGrid.SELECTOR_ICON.unhide();
                        mGrid.SELECTOR_ICON.startAnimationAt(nearest.getCenter());
                        mLevel.bringToFront(mGrid.SELECTOR_ICON);
                        bringToFront(mSelected);

                        getScene().getCameraController().setDisabled(true);
                        return true;
                    }
                }
                clearSelection();
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
        if (!mDownSelect) return false;

        switch (e.getType()) {
            case START:
                return true;

            case UPDATE:
                // find nearest cell to drag point and update path as it changes
                Grid.Cell nearest = mGrid.nearestCell(mLevel.cam2scene(e.getPos()));
                if (nearest != mLastNearest) {
                    mLastNearest = nearest;
                    Grid.CellPath test = mPathFinder.getPathTo(nearest);
                    if (test != null) {
                        mCurrentPath = test;
                        mPathFinderAnim.unhide();
                        mPathFinderAnim.setPath(mCurrentPath);
                    }
                }
                return true;

            case END:
                mDownSelect = false;
                mPathFinder.clear();
                mPathFinderAnim.hide();
                mGrid.SELECTOR_ICON.hide();
                getScene().getCameraController().setDisabled(false);

                mSelected.moveOnPath(mCurrentPath);
                return true;

        }
        return true;
    }


    @Override
    public void update() {
        for (Unit u : mEntities) {
            u.setLocation(mGrid.nearestCell(u.getPos()));
        }
        super.update();
    }

}
