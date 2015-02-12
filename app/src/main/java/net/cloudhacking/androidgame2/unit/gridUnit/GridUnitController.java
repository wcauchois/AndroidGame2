package net.cloudhacking.androidgame2.unit.gridUnit;

import net.cloudhacking.androidgame2.CDLevel;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.unit.SelectionHandler;
import net.cloudhacking.androidgame2.engine.gl.GLColor;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/31/2015.
 */
public class GridUnitController extends SelectionHandler.SelectionController<GridUnit> {

    private Grid mGrid;
    private CDLevel mLevel;
    private GridUnit mLastSelected;

    private Grid.SelectorIcon mSelectorIcon;
    private Grid.PathFinder mPathFinder;
    private Grid.CellPath mCurrentPath;
    private Grid.Cell mLastNearest;
    private Grid.CellPathAnim mPathFinderAnim;
    private Grid.GridOverlay mGridOverlay;


    public GridUnitController(CDLevel level) {
        mLevel = level;
        mGrid = level.getGrid();
        mPathFinder = mGrid.getPathFinder();
        mLastSelected = null;
        mLastNearest = null;

        mSelectorIcon = new Grid.SelectorIcon();
        mGridOverlay = new Grid.GridOverlay(mGrid, new GLColor(1,1,1,0.5f));
        mPathFinderAnim = new Grid.CellPathAnim(3.0f, GLColor.RED);
        mLevel.add(mSelectorIcon);
        mLevel.add(mPathFinderAnim);
        mLevel.add(mGridOverlay);
        mSelectorIcon.hide();
        mGridOverlay.hide();
        mPathFinderAnim.hide();
    }


    @Override
    protected void onClickDown(GridUnit selected, PointF scenePt) {
        mLastSelected = selected;
        mSelectorIcon.startAnimationAt(selected.getPos());
    }

    @Override
    protected void onStartDrag(GridUnit selected, PointF scenePt) {
        mGridOverlay.show();
        mPathFinder.setSource(selected.getLocation());
    }

    @Override
    protected void onUpdateDrag(GridUnit selected, PointF scenePt) {
        Grid.Cell nearestCell = mGrid.nearestCell(scenePt);
        if (nearestCell != mLastNearest) {
            Grid.CellPath testPath = mPathFinder.getPathTo(nearestCell);
            if (testPath != null) {
                mCurrentPath = testPath;
                mPathFinderAnim.setPath(mCurrentPath);
                mPathFinderAnim.show();
            }
            mLastNearest = nearestCell;
        }
    }

    @Override
    protected void onEndDrag(GridUnit selected, PointF scenePt) {
        mPathFinder.clear();
        mGridOverlay.hide();
        mPathFinderAnim.hide();
        selected.moveOnPath(mCurrentPath);
        selected.setLocation(mCurrentPath.getDestination());
    }


    @Override
    public void update() {
        if (mLastSelected != null && mLastSelected.isSelected()) {
            mSelectorIcon.setPos(mLastSelected.getPos());
            mSelectorIcon.show();
            mLevel.queueBringToFront(mSelectorIcon);
        } else {
            mSelectorIcon.hide();
        }
    }


}
