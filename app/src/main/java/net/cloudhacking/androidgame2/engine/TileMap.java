package net.cloudhacking.androidgame2.engine;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Resource;
import net.cloudhacking.androidgame2.engine.utils.TextureCache;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 1/17/2015.
 */
public class TileMap extends Renderable {

    public static interface Map {
        public int getTile(int ix, int iy);
        public int getTile(int index);
        public int getWidth();
        public int getHeight();
    }


    private Texture mTexture;
    private TextureFrameSet mFrames;

    private int mCellWidth;
    private int mCellHeight;

    private int mColumns;
    private int mRows;
    private int mQuadCount;

    private FloatBuffer mVertexBuffer;


    public TileMap(Asset asset, Map map, int cellWidth, int cellHeight) {
        this(TextureCache.get(asset), map, cellWidth, cellHeight);
    }

    public TileMap(Texture texture, Map map, int cellWidth, int cellHeight) {
        super(0, 0, 0, 0);
        mTexture = texture;
        mCellWidth = cellWidth;
        mCellHeight = cellHeight;

        mFrames = new TextureFrameSet(texture, cellWidth, cellHeight);
        setMap(map);
    }


    public void setMap(Map map) {
        mColumns = map.getWidth();
        mRows = map.getHeight();
        mQuadCount = mRows * mColumns;
        mVertexBuffer = BufferUtils.makeEmptyFloatBuffer(mQuadCount);

        setWidth(mCellWidth * mColumns);
        setHeight(mCellHeight * mRows);

        updateVertices(map);
    }

    private void updateVertices(Map map) {

        float[] vertices = new float[16];

        RectF uv;
        float l, t, r, b;  // left, top, right, bottom

        int index;
        for (int ix=0; ix<mColumns; ix++) {
            for (int iy=0; iy<mRows; iy++) {

                index = ix + iy * mColumns;

                mVertexBuffer.position( 16*index );

                uv = mFrames.getUVFrame( map.getTile(index) );

                l = ix * mCellWidth;
                t = iy * mCellHeight;
                r = (ix+1) * mCellWidth;
                b = (iy+1) * mCellHeight;

                // top left
                vertices[0]  = l;
                vertices[1]  = t;
                vertices[2]  = uv.left;
                vertices[3]  = uv.top;

                // top right
                vertices[4]  = r;
                vertices[5]  = t;
                vertices[6]  = uv.right;
                vertices[7]  = uv.top;

                // bottom right
                vertices[8]  = r;
                vertices[9]  = b;
                vertices[10] = uv.right;
                vertices[11] = uv.bottom;

                // bottom left
                vertices[12] = l;
                vertices[13] = b;
                vertices[14] = uv.left;
                vertices[15] = uv.bottom;

                mVertexBuffer.put(vertices);
            }
        }
    }


    public int getColumnsLength() {
        return mColumns;
    }

    public int getRowLength() {
        return mRows;
    }

    public int getCellWidth() {
        return mCellWidth;
    }

    public int getCellHeight() {
        return mCellHeight;
    }


    public void draw() {
        super.draw();
        BasicGLScript script = BasicGLScript.get();

        mTexture.bind();

        script.setLighting(getColorM(), getColorA());

        script.uModel.setValueM4(getModelMatrix());

        script.drawQuadSet(mVertexBuffer, mQuadCount);
    }

}
