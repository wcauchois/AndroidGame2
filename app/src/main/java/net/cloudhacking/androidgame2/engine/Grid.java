package net.cloudhacking.androidgame2.engine;

import android.util.SparseArray;
import android.util.SparseIntArray;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.TDGame;
import net.cloudhacking.androidgame2.engine.foundation.Animated;
import net.cloudhacking.androidgame2.engine.foundation.Entity;
import net.cloudhacking.androidgame2.engine.foundation.TileMap;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Signal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by Andrew on 1/20/2015.
 */
public class Grid extends Entity {

    /**
     * Animated selection icon that appears on the grid when you select a cell
     */
    public static final SelectorIcon SELECTOR_ICON = new SelectorIcon();

    public static class SelectorIcon extends Animated {

        private final AnimationSequence ANIM
                = new AnimationSequence(new int[] {0, 1}, 0, 2);

        public SelectorIcon() {
            super(Assets.SELECTOR_ICON);
            setVisibility(false);
        }

        public void startAnimationAt(PointF target) {
            setPos(target);
            setActive();
            setVisibility(true);
            queueAnimation(ANIM, true, true);
        }

        public void hide() {
            setVisibility(false);
            setInactive();
        }

    }


    /**********************************************************************************************/

    public class Cell {
        public int ix;
        public int iy;
        public int index;
        private boolean mOccupied;

        // from flood map
        private Cell mBestNeighbor;
        private int mDistToSource;

        public Cell(int ix, int iy) {
            this.ix = ix;
            this.iy = iy;
            this.index = ix + iy * mColumns;
            mOccupied = false;
            mBestNeighbor = null;
            mDistToSource = -1;
        }

        public void setOccupation(boolean bool) {
            mOccupied = bool;
        }

        public boolean isOccupied() {
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
                return false;  // XXX set to true after testing
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
        mFloodSource = null;

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

    public int getColumns() {
        return mColumns;
    }

    public int getHeight() {
        return mRows * mCellHeight;
    }

    public int getRows() {
        return mRows;
    }


    public Cell getCell(int ix, int iy) {
        return getCell( coordToIndex(ix, iy) );
    }

    public Cell getCell(int index) {
        return mGrid[ index ];
    }

    public boolean isOccupied(int ix, int iy) {
        return isOccupied(coordToIndex(ix, iy));
    }

    public boolean isOccupied(int index) {
        return mGrid[ index ].isOccupied();
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
    //      http://www.redblobgames.com/pathfinding/a-star/implementation.html#sec-1-4

    /**
     * Flood map
     */
    private ArrayList<Cell> neighbors;
    private boolean[] mReachable;
    private Cell mFloodSource;

    private ArrayList<Cell> getCellNeighbors(Cell c) {
        neighbors = new ArrayList<Cell>(4);
        if ( c.ix-1 >= 0       && !c.isOccupied() ) neighbors.add( getCell(c.ix-1, c.iy) );
        if ( c.ix+1 < mColumns && !c.isOccupied() ) neighbors.add( getCell(c.ix+1, c.iy) );
        if ( c.iy+1 < mRows    && !c.isOccupied() ) neighbors.add( getCell(c.ix, c.iy+1) );
        if ( c.iy-1 >= 0       && !c.isOccupied() ) neighbors.add( getCell(c.ix, c.iy-1) );

        return neighbors;
    }

    public boolean[] generateFloodMap(Cell source) {
        mFloodSource = source;
        Stack<Cell> frontier = new Stack<Cell>();

        int size = mColumns * mRows;
        for (int i=0; i<size; i++) mReachable[i] = false;

        frontier.push(source);
        mReachable[source.index] = true;
        source.setBestNeighbor(null);
        source.setDistToSource(0);

        Cell current;
        while (!frontier.isEmpty()) {
            current = frontier.pop();

            for(Cell n : getCellNeighbors(current)) {
                if ( !mReachable[n.index] ) {
                    frontier.push(n);
                    mReachable[n.index] = true;
                    n.setBestNeighbor( current );
                    n.setDistToSource( current.getDistToSource() + 1 );
                }
            }
        }
        // set unreachable cells
        for (int i=0; i<size; i++) {
            if (!mReachable[i]) getCell(i).setDistToSource(-1);
        }
        return mReachable;
    }


    /**
     * A* implementation
     */

    private static class CellSortable implements Comparable<CellSortable> {
        public Cell cell;
        public float cost;

        public CellSortable(Cell cell, float cost) {
            this.cell = cell;
            this.cost = cost;
        }

        @Override
        public int compareTo(CellSortable other) {
            float diff = this.cost - other.cost;
            if (diff<0) return -1;
            if (diff>0) return +1;
            return 0;
        }
    }

    private float heuristic(Cell c1, Cell c2) {
        return c1.getCenter().manhattanDistTo(c2.getCenter());
    }

    private LinkedList<Cell> buildPath(SparseIntArray history, int goalIndex) {
        LinkedList<Cell> path = new LinkedList<Cell>();
        path.add(getCell(goalIndex));

        int nextIndex = history.get(goalIndex);
        while (nextIndex != -1) {
            path.addFirst(getCell(nextIndex));
            nextIndex = history.get(nextIndex);
        }
        return path;
    }

    public LinkedList<Cell> getBestPath(Cell start, Cell goal) {
        PriorityQueue<CellSortable> frontier = new PriorityQueue<CellSortable>();
        HashSet<Integer> visited = new HashSet<Integer>();
        SparseArray<Float> costs = new SparseArray<Float>();
        SparseIntArray history = new SparseIntArray();

        frontier.add(new CellSortable(start, 0f));
        history.put(start.index, -1);
        costs.put(start.index, 0f);
        visited.add(start.index);

        Cell current;
        float newCost;
        while (!frontier.isEmpty()) {
            current = frontier.poll().cell;

            if (current.equals(goal)) {
                return buildPath(history, goal.index);
            }

            for (Cell n : getCellNeighbors(current)) {
                newCost = costs.get(current.index) + /* weight to next cell = */ 1 ;

                if ( !visited.contains(n.index) || newCost<costs.get(n.index) ) {
                    costs.put(n.index, newCost);
                    frontier.add( new CellSortable(n, newCost + heuristic(n, goal)) );
                    history.put(n.index, current.index);
                    visited.add(n.index);
                }
            }

        }
        return null;
    }

}
