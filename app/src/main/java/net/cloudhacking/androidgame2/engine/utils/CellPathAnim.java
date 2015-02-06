package net.cloudhacking.androidgame2.engine.utils;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;

import java.nio.FloatBuffer;

/**
 * Created by research on 2/5/15.
 */
public class CellPathAnim extends Renderable {

    private Grid.CellPath mPath;

    private float mThickness;
    private FloatBuffer mVertexBuffer;
    private int mVertexCount;
    private boolean mNeedBufferUpdate;

    public CellPathAnim(float thickness, float[] color) {
        this(null, thickness, color);
        setInactive();
        setVisibility(false);
    }

    public CellPathAnim(Grid.CellPath path, float thickness, float[] color) {
        super(0,0,0,0);
        mPath = path;
        mThickness = thickness;
        setColor(color);
        mNeedBufferUpdate = true;
    }

    public void setPath(Grid.CellPath path) {
        setActive();
        setVisibility(true);
        mPath = path;
        mNeedBufferUpdate = true;
    }

    public void setThickness(float thickness) {
        mThickness = thickness;
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


    private void updateVertices() {

        final float ht = mThickness/2;
        mVertexCount = 4*(mPath.length()-1);
        float[] vertices = new float[2*mVertexCount];

        int idx=0;
        int curCellIndex=0;
        Grid.Cell last = mPath.peek();
        PointF cenLast, cenCur;
        int dir;

        for (Grid.Cell cur : mPath.getLinkedList()) {

            if (curCellIndex>0) {
                dir = getNextDir(last, cur);

                cenLast = last.getCenter();
                cenCur  = cur.getCenter();

                // rotate these vertices based

                // bottom-left
                vertices[idx++] = cenLast.x + rotateX(-ht, 0, dir);
                vertices[idx++] = cenLast.y + rotateY(-ht, 0, dir);

                // bottom-right
                vertices[idx++] = cenLast.x + rotateX(+ht, 0, dir);
                vertices[idx++] = cenLast.y + rotateY(+ht, 0, dir);

                // top-left
                vertices[idx++] = cenCur.x + rotateX(-ht, 0, dir);
                vertices[idx++] = cenCur.y + rotateY(-ht, 0, dir);

                // top-right
                vertices[idx++] = cenCur.x + rotateX(+ht, 0, dir);
                vertices[idx++] = cenCur.y + rotateY(+ht, 0, dir);
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
