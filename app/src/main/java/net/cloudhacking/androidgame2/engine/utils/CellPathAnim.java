package net.cloudhacking.androidgame2.engine.utils;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.element.Renderable;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/5/15.
 */
public class CellPathAnim extends Renderable {

    private Grid.CellPath mPath;

    private float mThickness;
    private FloatBuffer mVertexBuffer;
    private boolean mNeedBufferUpdate;

    public CellPathAnim(Grid.CellPath path, float thickness, float[] color) {
        super(0,0,0,0);
        mPath = path;
        mThickness = thickness;
        setColor(color);
        mNeedBufferUpdate = true;
    }


    public void setColor(float[] color) {
        setColorM(new float[] {0,0,0,0});
        setColorA(color);
    }


    /**
     * Drawing the actual line...
     */

    private int getNextDir(Grid.Cell cur, Grid.Cell next) {
        int curX = cur.ix, curY = cur.iy;
        int nextX = next.ix, nextY = next.iy;

        if (nextX < curX && nextY == curY) return 0;  // left
        if (nextX == curX && nextY < curY) return 1;  // up
        if (nextX > curX && nextY == curY) return 2;  // right
                                           return 3;  // down
    }

    private int getTurn(int lastDir, int nextDir) {
        // final int STRAIGHT=0, RIGHT=1, BACK=2, LEFT=3;
        return (-lastDir + nextDir) % 4;
    }

    private float getFancyY(int turn, float fancy) {
        switch (turn) {
            case 1:
                return +fancy;
            case 0:
                return 0;
            case 3:
                return -fancy;
        }
        return 0;
    }

    private float rotateX(float x, float y, int dir) {
        switch (dir) {
            case 0: return -y;
            case 1: return +x;
            case 2: return +y;
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

    private PointF getCellWallPt(Grid.Cell c, int lastDir) {
        float hw = c.getWidth()/2, hh = c.getHeight()/2;
        PointF center = c.getCenter();
        switch (lastDir) {
            case 0:
                return new PointF(center.x + hw, center.y);
            case 1:
                return new PointF(center.x, center.y + hh);
            case 2:
                return new PointF(center.x - hw, center.y);
            case 3:
                return new PointF(center.x, center.y -hh);
        }
        return new PointF();
    }


    private void updateVertices() throws Exception {

        final int vertexCount = 2*(mPath.length()+1);
        float[] vertices = new float[2*vertexCount];

        final float ht = mThickness/2;
        final float fancy = ht*(float)Math.tan(22.5);

        int curCellIndex=0;
        int idx = 0;
        int lastDir=-1, nextDir, lastTurn, turn=-1;

        Grid.Cell lastCell=null;
        PointF center;
        float fancyY;

        for (Grid.Cell c : mPath.getLinkedList()) {

            // on first loop...
            if (lastCell==null) {
                lastCell = c;
                continue;
            } else {
                nextDir = getNextDir(lastCell, c);
            }

            // Do triangle strip vertices in left-right order w.r.t the
            // direction of the path.

            if (curCellIndex==1) {
                // populate vertices for first part at center of source cell
                center = c.getCenter();
                vertices[idx++] = center.x + rotateX(-ht, 0, nextDir);
                vertices[idx++] = center.y + rotateY(-ht, 0, nextDir);
                vertices[idx++] = center.x + rotateX(+ht, 0, nextDir);
                vertices[idx++] = center.y + rotateY(+ht, 0, nextDir);

                turn = 0;

            } else {

                turn = getTurn(lastDir, nextDir);
                if (turn == 2) {
                    throw new Exception("invalid path for animation");
                }

                // last cell
                center = getCellWallPt(lastCell, lastDir);
                fancyY = getFancyY(turn, fancy);
                vertices[idx++] = center.x + rotateX(-ht, +fancyY, lastDir);
                vertices[idx++] = center.y + rotateY(-ht, +fancyY, lastDir);
                vertices[idx++] = center.x + rotateX(+ht, -fancyY, lastDir);
                vertices[idx++] = center.y + rotateY(+ht, -fancyY, lastDir);

                // TODO: finish this

            }

            lastCell = c;
            lastDir = nextDir;
            lastTurn = turn;
            curCellIndex++;
        }

        mVertexBuffer = BufferUtils.makeFloatBuffer(vertices);
    }







}
