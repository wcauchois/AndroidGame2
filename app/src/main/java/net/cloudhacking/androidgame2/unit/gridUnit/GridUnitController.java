package net.cloudhacking.androidgame2.unit.gridUnit;

import net.cloudhacking.androidgame2.CDLevel;
import net.cloudhacking.androidgame2.ColonyDrop;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.ui.widget.Widget;
import net.cloudhacking.androidgame2.engine.unit.SelectionHandler;
import net.cloudhacking.androidgame2.engine.gl.GLColor;
import net.cloudhacking.androidgame2.engine.utils.CommonUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;
import net.cloudhacking.androidgame2.ui.FloatingMenu;

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
    private Grid.CellHighlighter mPathFinderAnim;
    private Grid.GridOverlay mGridOverlay;

    private Widget mFloatingMenu;


    public GridUnitController(CDLevel level) {
        mLevel = level;
        mGrid = level.getGrid();
        mPathFinder = mGrid.getPathFinder();
        mLastSelected = null;
        mLastNearest = null;
        mCurrentPath = null;

        mSelectorIcon = new Grid.SelectorIcon();
        mGridOverlay = new Grid.GridOverlay(mGrid, new GLColor(1,1,1,0.5f));
        mPathFinderAnim = new Grid.CellHighlighter(new GLColor(1,1,1,0.5f));
        mLevel.add(mSelectorIcon);
        mLevel.add(mPathFinderAnim);
        mLevel.add(mGridOverlay);
        mSelectorIcon.hide();
        mGridOverlay.hide();
        mPathFinderAnim.hide();

        mFloatingMenu = new FloatingMenu(null, CommonUtils.makeRect(new PointF(), 100, 100));
        mFloatingMenu.hide();
        ColonyDrop.getScene().getUI().getRoot().add(mFloatingMenu);
        ColonyDrop.getScene().getUI().getRoot().sendToBack(mFloatingMenu);
    }


    @Override
    protected void onClickDown(GridUnit selected, PointF scenePt) {
        mLastSelected = selected;
        mSelectorIcon.startAnimationAt(selected.getPos());
        mPathFinder.setSource(selected.getLocation());
        mLastNearest = selected.getLocation();

        mFloatingMenu.setScale(1/ColonyDrop.getActiveCamera().getZoom());
        mFloatingMenu.setPos(selected.getPos());
        mFloatingMenu.show();
    }

    @Override
    protected void onClickUp(GridUnit selected, PointF scenePt) {
        mPathFinder.clear();
    }

    @Override
    protected void onStartDrag(GridUnit selected, PointF scenePt) {
        mGridOverlay.show();
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
        if (mCurrentPath!= null) {
            selected.moveOnPath(mCurrentPath);
            Grid.Cell destination = mCurrentPath.getDestination();
            if (destination!=null) {
                selected.setLocation(destination);
            }
        }
    }


    @Override
    public void update() {
        if (mLastSelected != null && mLastSelected.isSelected()) {
            mSelectorIcon.setPos(mLastSelected.getPos());
            mSelectorIcon.show();
            mLevel.queueBringToFront(mSelectorIcon);
        } else {
            mSelectorIcon.hide();
            mFloatingMenu.hide();
        }
    }


}
