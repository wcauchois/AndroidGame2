package net.cloudhacking.androidgame2.engine;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.TDGame;
import net.cloudhacking.androidgame2.engine.foundation.Entity;
import net.cloudhacking.androidgame2.engine.foundation.Group;
import net.cloudhacking.androidgame2.engine.foundation.Image;
import net.cloudhacking.androidgame2.engine.foundation.TileMap;
import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Signal;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Andrew on 1/20/2015.
 */
public class Grid extends Entity {

    /**
     * Animated selection icon that appears on the grid when you select a cell
     */
    public static final SelectorIcon SELECTOR_ICON = new SelectorIcon();

    public static class SelectorIcon extends Image {
        private final float BASE_SCALE = 1 + 1f/16;
        private final float BLINK_SCALE = 1 + 1f/4;
        private final float THRESHOLD = 1f/3;
        private float mThreshold;

        public SelectorIcon() {
            super(Assets.SELECTOR_ICON);
            setScalable(true);
            setScale(BASE_SCALE);
            setVisibility(false);
            mThreshold = THRESHOLD;
        }

        public void startAnimationAt(Group group, PointF target) {
            setPos(target);
            setActive();
            setVisibility(true);
            mThreshold = THRESHOLD;
            group.addToFront(this);
        }

        public void hide() {
            setVisibility(false);
            setInactive();
        }

        @Override
        public void update() {
            mThreshold -= GameTime.getFrameDelta();
            if (mThreshold < 0) {
                float diff = BLINK_SCALE - BASE_SCALE;
                setScale( BASE_SCALE + (getScale().x - BASE_SCALE + diff) % (2*diff) );
                mThreshold += THRESHOLD;
            }
            super.update();
        }
    }


    /**********************************************************************************************/

    public class Cell {
        public int ix;
        public int iy;
        private boolean mOccupied;

        // pathfinding
        private Cell mBestNeighbor;
        private int mDistToSource;

        public Cell(int ix, int iy) {
            this.ix = ix;
            this.iy = iy;
            mOccupied = false;
            mBestNeighbor = null;
            mDistToSource = -1;
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

        public PointF getCenter() {
            return getRelativeCenter().add(mPos.toVec());
        }

        public Cell getBestNeighbor() {
            return mBestNeighbor;
        }

        public void setBestNeighbor(Cell cell) {
            mBestNeighbor = cell;
        }

        public int getDistToSource() {
            return mDistToSource;
        }

        public void setDistToSource(int dist) {
            mDistToSource = dist;
        }

        @Override
        public String toString() {
            return "Cell(ix="+ix+", iy="+iy+")";
        }
    }


    /**********************************************************************************************/

    public class CellSelector implements Signal.Listener<InputManager.ClickEvent> {

        Cell mSelected;

        @Override
        public boolean onSignal(InputManager.ClickEvent e) {
            mSelected = nearestCell(getScene().activeCameraToScene(e.getPos()));

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

    public void removeSelectorListener(CellSelectorListener listener) {
        mSelectorListeners.remove(listener);
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
        mReachable = new boolean[mColumns * mRows];

        mCellWidth = cellWidth;
        mCellHeight = cellHeight;

        mPos = new PointF();
        mGrid = new Cell[mColumns * mRows];

        for (int ix=0; ix<cols; ix++) {
            for (int iy=0; iy<rows; iy++) {
                mGrid[ coordToIndex(ix, iy) ] = new Cell(ix, iy);
            }
        }

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


    /**********************************************************************************************/
    // ref: http://www.redblobgames.com/pathfinding/tower-defense/

    private ArrayList<Cell> neighbors;
    private boolean[] mReachable;

    private ArrayList<Cell> getCellNeighbors(Cell c) {
        neighbors = new ArrayList<Cell>(4);
        neighbors.add( (c.ix-1) >= 0       ? getCell(c.ix-1, c.iy) : null );
        neighbors.add( (c.iy+1) < mRows    ? getCell(c.ix, c.iy+1) : null );
        neighbors.add( (c.ix+1) < mColumns ? getCell(c.ix+1, c.iy) : null );
        neighbors.add( (c.iy-1) >= 0       ? getCell(c.ix, c.iy-1) : null );

        return neighbors;
    }

    private void emptyReachableArray() {
        int size = mColumns * mRows;
        for (int i=0; i<size; i++) mReachable[i] = false;
    }

    public void generateFloodMap(Cell source) {
        Stack<Cell> frontier = new Stack<Cell>();

        emptyReachableArray();

        frontier.push(source);
        mReachable[ source.getIndex() ] = true;
        source.setBestNeighbor(null);
        source.setDistToSource(0);

        Cell current;
        while (!frontier.isEmpty()) {
            current = frontier.pop();

            for(Cell n : getCellNeighbors(current)) {
                if ( n != null && !mReachable[n.getIndex()] ) {
                    frontier.push(n);
                    mReachable[n.getIndex()] = true;
                    n.setBestNeighbor( current );
                    n.setDistToSource( current.getDistToSource() + 1 );
                }
            }
        }
    }

}
