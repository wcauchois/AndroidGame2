package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.InputManager;
import net.cloudhacking.androidgame2.engine.Level;
import net.cloudhacking.androidgame2.engine.Signal;
import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.unit.ControllableUnit.ActionType;

import java.util.LinkedHashSet;

/**
 * Created by Andrew on 1/31/2015.
 */
public class GridUnitController extends Entity implements Signal.Listener {

    private Grid mGrid;
    private Level mLevel;
    private GridUnit mSelected;
    private Grid.SelectorIcon mSelectorIcon;

    private LinkedHashSet<GridUnit> mUnits;

    public GridUnit addUnit(GridUnit u) {
        mUnits.add(u);
        return u;
    }

    public GridUnit removeUnit(GridUnit u) {
        mUnits.remove(u);
        return u;
    }

    public boolean isControlling(GridUnit u) {
        return mUnits.contains(u);
    }


    public GridUnitController(Level level) {
        mLevel = level;
        mGrid = level.grid;

        mSelectorIcon = new Grid.SelectorIcon();
        mLevel.add(mSelectorIcon);
        mSelectorIcon.hide();

        mUnits = new LinkedHashSet<GridUnit>();
        mSelected = null;
        mDownSelect = false;
        initPathFinder();
    }

    public void select(GridUnit u) {
        if (mSelected != null) mSelected.unSelect();
        mSelected = u;
        mSelected.select();
        mSelectorIcon.show();
    }

    public void clearSelection() {
        if (mSelected != null) mSelected.unSelect();
        mSelectorIcon.hide();
        mSelected = null;
    }


    @Override
    public void update() {
        if (mSelected != null) {
            mSelectorIcon.setPos(mSelected.getPos());
        }
        for (Unit u : mUnits) {
            u.setLocation(mGrid.nearestCell(u.getPos()));
        }
    }


    //----------------------------------------------------------------------------------------------
    // Selection Handling

    private boolean mDownSelect;
    private Grid.PathFinder mPathFinder;
    private boolean mPathFinderStarted;
    private Grid.CellPath mCurrentPath;
    private Grid.Cell mLastNearest;
    private Grid.CellPathAnim mPathFinderAnim;
    private Grid.GridOverlay mGridOverlay;

    private void initPathFinder() {
        mPathFinder = mGrid.getPathFinder();
        mPathFinderStarted = false;
        mLastNearest = null;

        mGridOverlay = new Grid.GridOverlay(mGrid, new float[] {1,1,1,0.5f});
        mLevel.add(mGridOverlay);
        mGridOverlay.hide();

        mPathFinderAnim = new Grid.CellPathAnim(3.0f, new float[] {1,0,0,1});
        mLevel.add(mPathFinderAnim);
        mPathFinderAnim.hide();
    }

    private void startPathFinder() {
        mGridOverlay.show();
        mPathFinder.setSource(mSelected.getLocation());
        mPathFinderStarted = true;
    }

    private void updatePathFinder(Grid.Cell target) {
        Grid.CellPath test = mPathFinder.getPathTo(target);
        if (test != null) {
            mCurrentPath = test;
            mPathFinderAnim.setPath(mCurrentPath);
            mPathFinderAnim.show();
        }
    }

    private void endPathFinder() {
        mPathFinder.clear();
        mGridOverlay.hide();
        mPathFinderAnim.hide();
        mPathFinderStarted = false;
    }


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

                for (GridUnit u : mUnits) {
                    if (u.getLocation() == nearest) {
                        select(u);
                        mDownSelect = true;
                        mSelectorIcon.show();
                        mSelectorIcon.startAnimationAt(nearest.getCenter());
                        mLevel.bringToFront(mSelectorIcon);

                        mLevel.getScene().getCameraController().setDisabled(true);
                        return true;
                    }
                }
                return false;

            case CANCEL:
                break;

            case UP:
                if (!mDownSelect) clearSelection();
                mDownSelect = false;
                mLevel.getScene().getCameraController().setDisabled(false);
        }
        return false;
    }

    public boolean handleDragEvent(InputManager.DragEvent e) {
        // if click down hits a unit in this group, an ensuing click up will select it,
        // otherwise a drag will highlight a path from the current location of the unit
        // to the target of the drag.

        // invalid if no unit is selected or the selected unit is already moving
        if (!mDownSelect || mSelected.getCurrentAction() == ActionType.MOVE) {
            mLevel.getScene().getCameraController().setDisabled(false);
            return false;
        }

        switch (e.getType()) {
            case START:
                startPathFinder();
                return true;

            case UPDATE:
                if (!mPathFinderStarted) return false;

                // find nearest cell to drag point and update path as it changes
                Grid.Cell nearest = mGrid.nearestCell(mLevel.cam2scene(e.getPos()));
                if (nearest != mLastNearest) {
                    updatePathFinder(nearest);
                    mLastNearest = nearest;
                }
                return true;

            case END:
                mDownSelect = false;
                mLevel.getScene().getCameraController().setDisabled(false);

                if (!mPathFinderStarted) return false;
                endPathFinder();
                mSelected.moveOnPath(mCurrentPath);
                mLevel.bringToFront(mSelectorIcon);
                return true;
        }
        return true;
    }

}
