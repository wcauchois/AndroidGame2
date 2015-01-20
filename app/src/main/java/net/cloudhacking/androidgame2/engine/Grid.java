package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.TDGame;
import net.cloudhacking.androidgame2.engine.foundation.Entity;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Signal;

import java.util.ArrayList;

/**
 * Created by Andrew on 1/20/2015.
 */
public class Grid extends Entity {

    public class Cell {
        public int ix;
        public int iy;
        private boolean mOccupied;

        public Cell(int ix, int iy) {
            this.ix = ix;
            this.iy = iy;
            mOccupied = false;
        }

        public int getIndex() {
            return ix + iy * mColumns;
        }

        public void setOccupation(boolean bool) {
            mOccupied = bool;
        }

        public boolean checkOccupation() {
            return mOccupied;
        }

        public PointF getRelativeCenter() {
            // relative to the scene point of the top-left corner of the grid
            return new PointF( (ix+0.5f) * mCellWidth, (iy+0.5f) * mCellHeight );
        }

    }


    /**********************************************************************************************/

    public class CellSelector implements Signal.Listener<InputManager.ClickEvent> {

        Cell mSelected;

        @Override
        public boolean onSignal(InputManager.ClickEvent e) {
            mSelected = nearestCell(e.getPos());

            if (mSelected == null) {
                return false;

            } else {
                for (CellSelectorListener l : mSelectorListeners) {
                    l.onCellSelect(mSelected);
                }
                return true;
            }
        }

        public Cell getLastSelectedCell() {
            return mSelected;
        }
    }


    public interface CellSelectorListener {
        public void onCellSelect(Cell selected);
    }

    public void addSelectorListener(CellSelectorListener listener) {
        mSelectorListeners.add(listener);
    }


    /**********************************************************************************************/

    private PointF mPos;    // typically this should be (0,0) to correspond with the
                            // top-left corner of the scene

    private Cell[] mGrid;
    private int mColumns;
    private int mRows;

    private int coordToIndex(int ix, int iy) {
        return ix + iy * mColumns;
    }

    private int mCellWidth;
    private int mCellHeight;

    private ArrayList<CellSelectorListener> mSelectorListeners;


    public Grid(int cols, int rows, int cellWidth, int cellHeight) {
        mColumns = cols;
        mRows = rows;
        mCellWidth = cellWidth;
        mCellHeight = cellHeight;

        mPos = new PointF();
        mGrid = new Cell[mColumns * mRows];

        TDGame.getInstance().getInputManager().clickUp.connect(new CellSelector());

        mSelectorListeners = new ArrayList<CellSelectorListener>();
    }

    public Grid(TileMap map) {
        this(map.getColumnsLength(),
             map.getRowsLength(),
             map.getCellWidth() * (int)map.getScale().x,
             map.getCellHeight() * (int)map.getScale().y
        );
    }


    public int getWidth() {
        return mColumns * mCellWidth;
    }

    public int getHeight() {
        return mRows * mCellHeight;
    }


    public Cell getCell(int ix, int iy) {
        return getCell( coordToIndex(ix, iy) );
    }

    public Cell getCell(int index) {
        return mGrid[ index ];
    }

    public boolean checkOccupation(int ix, int iy) {
        return checkOccupation( coordToIndex(ix, iy) );
    }

    public boolean checkOccupation(int index) {
        return mGrid[ index ].checkOccupation();
    }

    public void setOccupation(int ix, int iy, boolean bool) {
        setOccupation( coordToIndex(ix, iy), bool );
    }

    public void setOccupation(int index, boolean bool) {
        mGrid[ index ].setOccupation(bool);
    }


    public Cell nearestCell(PointF scenePoint) {

        if (scenePoint.x < mPos.x || scenePoint.x > mPos.x + getWidth() ||
            scenePoint.y < mPos.y || scenePoint.y > mPos.y + getHeight() )
        {
            return null;
        }

        PointF relativePoint = scenePoint.add(mPos.toVec().negate());
        int ix = (int)relativePoint.x / mCellWidth;
        int iy = (int)relativePoint.y / mCellHeight;
        return mGrid[ coordToIndex(ix, iy) ];
    }

    public PointF cellToScene(Cell cell) {
        return mPos.add(cell.getRelativeCenter().toVec());
    }

}
