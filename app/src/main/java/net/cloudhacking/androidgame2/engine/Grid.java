package net.cloudhacking.androidgame2.engine;

import android.util.SparseArray;
import android.util.SparseIntArray;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.element.Animated;
import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.element.shape.PixelLines;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.Color;
import net.cloudhacking.androidgame2.engine.utils.PointF;

import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Created by Andrew on 1/20/2015.
 */
public class Grid extends Entity {

    // TODO: do grid overlay using glDrawLines()

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


    public static enum CellState {
        // If we end up having different terrain types we'll need to
        // implement a terrain movement cost function for path-finding.
        EMPTY, OCCUPIED
    }

    public class Cell {
        public int ix;
        public int iy;
        public int index;
        private PointF mCenter;
        private CellState mState;

        // from flood map
        private Cell mReachableNeighbor;  // towards the flood source
        private int mDistToSource;  // -1 if this Cell is unreachable

        public Cell(int ix, int iy) {
            this.ix = ix;
            this.iy = iy;
            this.index = ix + iy * mColumns;
            mCenter = getRelativeCenter().add(mPos.toVec());
            mState = CellState.EMPTY;
            mReachableNeighbor = null;
            mDistToSource = -1;
        }

        public void setState(CellState state) {
            mState = state;
        }

        public CellState getState() {
            return mState;
        }

        public boolean isOccupied() {
            return mState == CellState.OCCUPIED;
        }

        public PointF getRelativeCenter() {
            // relative to the scene point of the top-left corner of the grid
            return new PointF( (ix+0.5f) * mCellWidth, (iy+0.5f) * mCellHeight );
        }

        public PointF getCenter() {
            return mCenter.copy();
        }

        public float getWidth() {
            return mCellWidth;
        }

        public float getHeight() {
            return mCellHeight;
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


    //----------------------------------------------------------------------------------------------

    public class CellSelector implements Signal.Listener<InputManager.ClickEvent> {

        Cell mSelected;

        @Override
        public boolean onSignal(InputManager.ClickEvent e) {
            switch(e.getType()) {
                case UP:
                    mSelected = nearestCell(getScene().activeCameraToScene(e.getPos()));

                    if (mSelected == null) {
                        return false;

                    } else {
                        cellSelector.dispatch(mSelected);
                        return false;  // XXX set to true after testing
                    }

                default:
                    return false;
            }
        }

        public Cell getLastSelected() {
            return mSelected;
        }
    }

    public CellSelector getClickListener() {
        return mClickListener;
    }

    public Signal<Cell> cellSelector = new Signal<Cell>();


    //----------------------------------------------------------------------------------------------

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

    private CellSelector mClickListener;


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

        mClickListener = new CellSelector();
    }

    public static Grid createFromTileMap(TileMap map) {
        return new Grid(map.getColumnsLength(),
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
        return getCell(coordToIndex(ix, iy));
    }

    public Cell getCell(int index) {
        return mGrid[ index ];
    }

    public boolean isOccupied(int ix, int iy) {
        return isOccupied( coordToIndex(ix, iy) );
    }

    public boolean isOccupied(int index) {
        return mGrid[ index ].isOccupied();
    }

    public void setState(int ix, int iy, CellState state) {
        setState(coordToIndex(ix, iy), state);
    }

    public void setState(int index, CellState state) {
        mGrid[ index ].setState(state);
    }

    public CellState getState(int ix, int iy) {
        return getState( coordToIndex(ix, iy) );
    }

    public CellState getState(int index) {
        return mGrid[ index ].getState();
    }

    public void mapToState(TileMap.Map map, CellState trueState, CellState falseState) {
        if (map.getWidth() != mColumns || map.getHeight() != mRows) {
            return;
        }
        for (int i=0; i<(mColumns*mRows); i++) {
            if (map.getTile(i) != 0) {
                setState(i, trueState);
            } else {
                setState(i, falseState);
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


    //----------------------------------------------------------------------------------------------
    // path-finding
    //
    // ref: http://www.redblobgames.com/pathfinding/tower-defense/
    //      http://www.redblobgames.com/pathfinding/a-star/implementation.html#sec-1-4

    private Cell[] neighbors = new Cell[4];
    private Cell[] getCellNeighbors(Cell c) {
        if ( c.ix-1 >= 0 && !c.isOccupied() ) {
            neighbors[0] = getCell(c.ix-1, c.iy);
        } else {
            neighbors[0] = null;
        }
        if ( c.ix+1 < mColumns && !c.isOccupied() ) {
            neighbors[1] = getCell(c.ix+1, c.iy);
        } else {
            neighbors[1] = null;
        }
        if ( c.iy+1 < mRows && !c.isOccupied() ) {
            neighbors[2] = getCell(c.ix, c.iy+1);
        } else {
            neighbors[2] = null;
        }
        if ( c.iy-1 >= 0 && !c.isOccupied() ) {
            neighbors[3] = getCell(c.ix, c.iy-1);
        } else {
            neighbors[3] = null;
        }

        return neighbors;
    }


    /**
     * Flood map to check for all reachable cells
     */
    private boolean[] mReachable;
    private Cell mFloodSource;

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

        protected LinkedList<Cell> mPath;

        public CellPath(LinkedList<Cell> path) {
            mPath = path;
        }

        public boolean isEmpty() {
            return mPath.isEmpty();
        }

        public LinkedList<Cell> getLinkedList() {
            return mPath;
        }

        public Cell pop() {
            return mPath.pollFirst();
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

        public float[] getPathVertices() {
            float[] vertices = new float[2*mPath.size()];

            int i=0;
            PointF p;
            for (Cell c : mPath) {
                p = c.getCenter();
                vertices[i++] = p.x;
                vertices[i++] = p.y;
            }
            return vertices;
        }

        @Override
        public String toString() {
            return mPath.toString();
        }
    }


    // use this if you need to check if cells are on the given path or not
    public class CellPathSet extends CellPath {

        private HashSet<Integer> mMembers;  // by cell index

        public CellPathSet(LinkedList<Cell> path, HashSet<Integer> members) {
            super(path);
            mMembers = members;
        }

        @Override
        public Cell pop() {
            Cell popped = mPath.pollFirst();
            mMembers.remove(popped.index);
            return popped;
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
    }


    private CellPathSet buildCellPathSet(SparseIntArray history, int goalIndex) {
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

        return new CellPathSet(path, members);
    }

    private CellPath buildCellPath(SparseIntArray history, int goalIndex) {
        if (history == null) return null;

        LinkedList<Cell> path = new LinkedList<Cell>();
        path.add(getCell(goalIndex));

        int nextIndex = history.get(goalIndex);
        while (nextIndex != -1) {
            path.addFirst(getCell(nextIndex));
            nextIndex = history.get(nextIndex);
        }

        return new CellPath(path);
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


    public CellPath getBestPath(int startX, int startY, int goalX, int goalY) {
        return getBestPath( getCell(startX, startY), getCell(goalX, goalY) );
    }

    public CellPath getBestPath(Cell start, Cell goal) {
        return buildCellPath(findPath(start, goal), goal.index);
    }

    public CellPathSet getBestPathSet(int startX, int startY, int goalX, int goalY) {
        return getBestPathSet(getCell(startX, startY), getCell(goalX, goalY));
    }

    public CellPathSet getBestPathSet(Cell start, Cell goal) {
        return buildCellPathSet(findPath(start, goal), goal.index);
    }

    public LinkedList<PointF> getBestPathAsPoints(int startX, int startY, int goalX, int goalY) {
        return getBestPathAsPoints( getCell(startX, startY), getCell(goalX, goalY) );
    }

    public LinkedList<PointF> getBestPathAsPoints(Cell start, Cell goal) {
        return buildPointPath(findPath(start, goal), goal.index);
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
                if (n==null) continue;

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


    //----------------------------------------------------------------------------------------------

    /**
     * Class used for active path-finding.  It caches the shortest paths to points progressively
     * as the user drags their finger across the screen so that we can animated the path
     * that a unit would potentially take across the map.
     */

    public PathFinder getPathFinder() {
        return new PathFinder();
    }
    public PathFinder getPathFinder(Cell source) {
        return new PathFinder(source);
    }

    public class PathFinder {

        private Cell mSource;

        private PriorityQueue<CellSortable> mFrontier;
        private HashSet<Integer> mVisited;
        private SparseArray<Float> mCosts;
        private SparseIntArray mHistory;

        private SparseArray<CellPath> mPathCache;

        public PathFinder() {
            mFrontier = new PriorityQueue<CellSortable>();
            mVisited = new HashSet<Integer>();
            mCosts = new SparseArray<Float>();
            mHistory = new SparseIntArray();
            mPathCache = new SparseArray<CellPath>();
        }

        public PathFinder(Cell source) {
            this();
            setSource(source);
        }

        public void clear() {
            mPathCache.clear();
            mFrontier.clear();
            mHistory.clear();
            mCosts.clear();
            mVisited.clear();
        }

        public void setSource(Cell source) {
            clear();
            mSource = source;
            mFrontier.add(new CellSortable(source.index, 0f));
            mHistory.put(source.index, -1);
            mCosts.put(source.index, 0f);
            mVisited.add(source.index);
        }

        public Cell getSource() {
            return mSource;
        }

        public CellPath getPathTo(Cell target) {
            if (mSource == null || target.isOccupied()) return null;

            // try cache
            CellPath tmp = mPathCache.get(target.index);
            if (tmp != null) {
                return tmp;
            }

            Cell current;
            float cost;
            while (!mFrontier.isEmpty()) {
                // get cell with shortest length in priority queue,
                // then build path for this cell and cache the path
                current = getCell( mFrontier.poll().cellIndex );

                tmp = buildCellPath(mHistory, current.index);
                mPathCache.put(current.index, tmp);

                for (Cell n : getCellNeighbors(current)) {
                    if (n==null) continue;

                    cost = mCosts.get(current.index) + /* cost to next cell = */ 1 ;

                    if ( !mVisited.contains(n.index) || cost< mCosts.get(n.index) ) {
                        mCosts.put(n.index, cost);
                        // not using heuristic cost here since we have a changing target
                        mFrontier.add( new CellSortable(n.index, cost) );
                        mHistory.put(n.index, current.index);
                        mVisited.add(n.index);
                    }
                }

                // need to update neighbors before returning path for target
                if (current.equals(target)) {
                    return tmp;
                }
            }
            return null;
        }

    }


    //----------------------------------------------------------------------------------------------
    // helpful visual elements for grids

    /**
     * Animated selection icon that appears on the grid when you select a cell
     */
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

    }


    /**
     * GL lined grid to overlay on
     */
    public static class GridOverlay extends PixelLines {

        public GridOverlay(Grid grid, Color color) {
            super(color);
            int cw = grid.getCellWidth(), ch = grid.getCellHeight();
            int cols = grid.getColumns(), rows = grid.getRows();

            int vertexCount = 2*(cols-1) + 2*(rows-1);
            float[] vertices  = new float[2*vertexCount];

            int idx = 0;

            // columns
            int h = rows*ch;
            for (int i=1; i<cols; i++) {
                // top
                vertices[idx++] = i*cw;
                vertices[idx++] = 0;
                // bottom
                vertices[idx++] = i*cw;
                vertices[idx++] = h;
            }

            // rows
            int w = cols*cw;
            for (int i=1; i<rows; i++) {
                // left
                vertices[idx++] = 0;
                vertices[idx++] = i*ch;
                // right
                vertices[idx++] = w;
                vertices[idx++] = i*ch;
            }

            setVertices(vertices);
        }

    }


    /**
     * Animates a CellPath
     */
    public static class CellPathAnim extends Renderable {

        private CellPath mPath;

        private float mThickness;
        private FloatBuffer mVertexBuffer;
        private int mVertexCount;
        private boolean mNeedBufferUpdate;

        public CellPathAnim(float thickness, Color color) {
            this(null, thickness, color);
            setInactive();
            setVisibility(false);
        }

        public CellPathAnim(CellPath path, float thickness, Color color) {
            super(0,0,0,0);
            mPath = path;
            mThickness = thickness;
            setColor(color);
            mNeedBufferUpdate = true;
        }

        public void setPath(CellPath path) {
            setActive();
            setVisibility(true);
            mPath = path;
            mNeedBufferUpdate = true;
        }

        public void setThickness(float thickness) {
            mThickness = thickness;
            mNeedBufferUpdate = true;
        }

        public void setColor(Color c) {
            setColorM(Color.CLEAR);
            setColorA(c);
        }


        /**
         * Drawing the actual path...
         */

        private int getNextDir(Cell cur, Cell next) {
            int curX = cur.ix, curY = cur.iy;
            int nextX = next.ix, nextY = next.iy;

            if (nextX < curX && nextY == curY) return 0;  // left
            if (nextX == curX && nextY < curY) return 1;  // up
            if (nextX > curX && nextY == curY) return 2;  // right
                                               return 3;  // down
        }

        // rotates these vertices based on direction of path
        private float rotateX(float x, float y, int dir) {
            switch (dir) {
                case 0: return +y;
                case 1: return +x;
                case 2: return -y;
                case 3: return -x;
            }
            return 0;
        }
        private float rotateY(float x, float y, int dir) {
            switch (dir) {
                case 0: return +x;
                case 1: return +y;
                case 2: return -x;
                case 3: return -y;
            }
            return 0;
        }


        private void updateVertices() {
            if (mPath == null) return;

            final float ht = mThickness/2;
            mVertexCount = 4*(mPath.length()-1);
            float[] vertices = new float[2*mVertexCount];

            int idx=0;
            int curCellIndex=0;
            Cell last = mPath.peek();
            PointF cenLast, cenCur;
            int dir;

            for (Cell cur : mPath.getLinkedList()) {

                if (curCellIndex>0) {
                    dir = getNextDir(last, cur);

                    cenLast = last.getCenter();
                    cenCur  = cur.getCenter();

                    // bottom-left
                    vertices[idx++] = cenLast.x + rotateX(-ht, +ht, dir);
                    vertices[idx++] = cenLast.y + rotateY(-ht, +ht, dir);
                    // bottom-right
                    vertices[idx++] = cenLast.x + rotateX(+ht, +ht, dir);
                    vertices[idx++] = cenLast.y + rotateY(+ht, +ht, dir);
                    // top-left
                    vertices[idx++] = cenCur.x + rotateX(-ht, -ht, dir);
                    vertices[idx++] = cenCur.y + rotateY(-ht, -ht, dir);
                    // top-right
                    vertices[idx++] = cenCur.x + rotateX(+ht, -ht, dir);
                    vertices[idx++] = cenCur.y + rotateY(+ht, -ht, dir);
                }

                last = cur;
                curCellIndex++;
            }

            mVertexBuffer = BufferUtils.makeFloatBuffer(vertices);
        }

        @Override
        public void update() {
            if (mNeedBufferUpdate) {
                updateVertices();
                mNeedBufferUpdate = false;
            }
            super.update();
        }

        @Override
        public void draw(BasicGLScript gls) {
            super.draw(gls);
            gls.drawTriangleStrip(mVertexBuffer, 0, mVertexCount);
        }

    }
}
