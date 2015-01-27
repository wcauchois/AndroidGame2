package net.cloudhacking.androidgame2.engine;

import android.util.SparseArray;
import android.util.SparseIntArray;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.element.Animated;
import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.utils.PointF;

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
     * This class represents a grid in the game that can be used for path-finding and
     * placement of in-game entities.  It also contains a signal that will dispatch upon
     * the detected selection of a Cell.
     *
     * Pathfinding Functionality:
     *      -generateFloodMap(Cell source); will find all reachable cells from
     *       the given source.  It returns a boolean array the size of the grid
     *       where the i-th index indicates whether or not that cell index is
     *       reachable.  In addition each cell has a distanceToSource member variable
     *       which is set to -1 is that cell is unreachable (since the last time
     *       generateFloodMap() was run).
     *
     *      -getBestPath(Cell start, Cell goal); will find the optimal path from a
     *       specific start and finish point.  It returns a CellPath object which
     *       provides utility for iteration and for checking if a given Cell is on
     *       the path or not.
     */



    /**
     * Animated selection icon that appears on the grid when you select a cell
     */
    public final SelectorIcon SELECTOR_ICON = new SelectorIcon();

    public static class SelectorIcon extends Animated {

        private final float BLINK_FREQ = 2;

        public SelectorIcon() {
            super(Assets.SELECTOR_8PX);
            setVisibility(false);
        }

        public void startAnimationAt(PointF target) {
            setPos(target);
            setActive();
            setVisibility(true);
            queueAnimation(
                    new AnimationSequence(new int[] {1, 0}, 1, BLINK_FREQ),
                    true, true
            );
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
        private Cell mReachableNeighbor;  // towards the flood source
        private int mDistToSource;  // -1 if this Cell is unreachable

        public Cell(int ix, int iy) {
            this.ix = ix;
            this.iy = iy;
            this.index = ix + iy * mColumns;
            mOccupied = false;
            mReachableNeighbor = null;
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

        public Cell getReachableNeighbor() {
            return mReachableNeighbor;
        }

        public void setReachableNeighbor(Cell cell) {
            mReachableNeighbor = cell;
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
                cellSelector.dispatch(mSelected);
                return false;  // XXX set to true after testing
            }
        }

        public Cell getLastSelected() {
            return mSelected;
        }
    }

    public Signal<Cell> cellSelector = new Signal<Cell>();

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

        GameSkeleton.getInstance().getInputManager().click.connect(new CellSelector());
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

    public int getCellWidth() {
        return mCellWidth;
    }

    public int getColumns() {
        return mColumns;
    }

    public int getHeight() {
        return mRows * mCellHeight;
    }

    public int getCellHeight() {
        return mCellHeight;
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

    public void setOccupation(TileMap.Map collisionMap) {
        if (collisionMap.getWidth() != mColumns || collisionMap.getHeight() != mRows) {
            return;
        }
        for (int i=0; i<(mColumns*mRows); i++) {
            if (collisionMap.getTile(i) == 0) {
                setOccupation(i, true);
            } else {
                setOccupation(i, false);
            }
        }
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
        source.setReachableNeighbor(null);
        source.setDistToSource(0);

        Cell current;
        while (!frontier.isEmpty()) {
            current = frontier.pop();

            for(Cell n : getCellNeighbors(current)) {
                if ( !mReachable[n.index] ) {
                    frontier.push(n);
                    mReachable[n.index] = true;
                    n.setReachableNeighbor(current);
                    n.setDistToSource( current.getDistToSource() + 1 );
                }
            }
        }
        // set unreachable cells
        for (int i=0; i<size; i++) {
            if (!mReachable[i]) {
                getCell(i).setDistToSource(-1);
                getCell(i).setReachableNeighbor(null);
            }
        }
        return mReachable;
    }



    /**
     * A* implementation
     */

    public class CellPath {

        private LinkedList<Cell> mPath;
        private HashSet<Integer> mMembers;  // by cell index

        public CellPath(LinkedList<Cell> path, HashSet<Integer> members) {
            mPath = path;
            mMembers = members;
        }

        public boolean containsCell(Cell cell) {
            return mMembers.contains( cell.index );
        }

        public boolean containsCell(int ix, int iy) {
            return mMembers.contains( coordToIndex(ix, iy) );
        }

        public boolean containsCell(int index) {
            return mMembers.contains(index);
        }

        public boolean isEmpty() {
            return mPath.isEmpty();
        }

        public Cell pop() {
            Cell popped = mPath.pollFirst();
            mMembers.remove(popped.index);
            return popped;
        }

        public Cell peek() {
            return mPath.peek();
        }

        public Cell getDestination() {
            return mPath.getLast();
        }

        public int length() {
            return mPath.size();
        }

        @Override
        public String toString() {
            return mMembers.toString();
        }
    }


    public CellPath getBestPath(int startX, int startY, int goalX, int goalY) {
        return getBestPath( getCell(startX, startY), getCell(goalX, goalY) );
    }

    public CellPath getBestPath(Cell start, Cell goal) {
        return buildCellPath(findPath(start, goal), goal.index);
    }

    public LinkedList<PointF> getBestPathAsPoints(int startX, int startY, int goalX, int goalY) {
        return getBestPathAsPoints( getCell(startX, startY), getCell(goalX, goalY) );
    }

    public LinkedList<PointF> getBestPathAsPoints(Cell start, Cell goal) {
        return buildPointPath(findPath(start, goal), goal.index);
    }

    private CellPath buildCellPath(SparseIntArray history, int goalIndex) {
        if (history == null) return null;

        LinkedList<Cell> path = new LinkedList<Cell>();
        HashSet<Integer> members = new HashSet<Integer>();
        path.add(getCell(goalIndex));
        members.add(goalIndex);

        int nextIndex = history.get(goalIndex);
        while (nextIndex != -1) {
            path.addFirst(getCell(nextIndex));
            members.add(nextIndex);
            nextIndex = history.get(nextIndex);
        }

        return new CellPath(path, members);
    }

    private LinkedList<PointF> buildPointPath(SparseIntArray history, int goalIndex) {
        if (history == null) return null;

        LinkedList<PointF> path = new LinkedList<PointF>();
        path.add(getCell(goalIndex).getCenter());

        int nextIndex = history.get(goalIndex);
        while (nextIndex != -1) {
            path.addFirst(getCell(nextIndex).getCenter());
            nextIndex = history.get(nextIndex);
        }
        return path;
    }


    private static class CellSortable implements Comparable<CellSortable> {
        public int cellIndex;
        public float cost;

        public CellSortable(int cellIndex, float cost) {
            this.cellIndex = cellIndex;
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

    // this fudge factor is supposed to break cost ties:
    //    http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#breaking-ties
    private final float EXPECTED_MAX_PATH_LENGTH = 100;
    private final float FUDGE = 1 + 1/EXPECTED_MAX_PATH_LENGTH;

    private float heuristic(Cell c1, Cell c2) {
        return FUDGE * c1.getCenter().manhattanDistTo(c2.getCenter());
    }

    private SparseIntArray findPath(Cell start, Cell goal) {
        PriorityQueue<CellSortable> frontier = new PriorityQueue<CellSortable>();
        HashSet<Integer> visited = new HashSet<Integer>();
        SparseArray<Float> costs = new SparseArray<Float>();
        SparseIntArray history = new SparseIntArray();

        frontier.add(new CellSortable(start.index, 0f));
        history.put(start.index, -1);
        costs.put(start.index, 0f);
        visited.add(start.index);

        Cell current;
        float newCost;
        while (!frontier.isEmpty()) {
            current = getCell( frontier.poll().cellIndex );

            if (current.equals(goal)) {
                return history;
            }

            for (Cell n : getCellNeighbors(current)) {
                newCost = costs.get(current.index) + /* cost to next cell = */ 1 ;

                if ( !visited.contains(n.index) || newCost<costs.get(n.index) ) {
                    costs.put(n.index, newCost);
                    frontier.add( new CellSortable(n.index, newCost + heuristic(n, goal)) );
                    history.put(n.index, current.index);
                    visited.add(n.index);
                }
            }

        }
        return null;
    }

}
