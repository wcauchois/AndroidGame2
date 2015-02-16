package net.cloudhacking.androidgame2.engine.element;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.gl.FrameBufferObject;
import net.cloudhacking.androidgame2.engine.gl.PreRenderTexture;
import net.cloudhacking.androidgame2.engine.gl.QuadDrawer;
import net.cloudhacking.androidgame2.engine.gl.Texture;
import net.cloudhacking.androidgame2.engine.utils.Asset;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.BufferUtils;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;
import net.cloudhacking.androidgame2.engine.utils.TextureFrameSet;

import java.nio.FloatBuffer;

/**
 * Created by Andrew on 1/17/2015.
 */
public class TileMap extends PreRenderable {

    /**
     * This class represents a tiled texture (like our tiled background).  It loads the
     * tile-set from the given texture or pre-existing sprite sheet.  Its vertex buffer is only
     * generated once (or if a new mapping is set).
     *
     * Note: The top-left vertex of the TileMap should be (0,0) to correspond with
     *       the games' grid.
     */

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
        this(AssetCache.getInstance().getTexture(asset), map, cellWidth, cellHeight);
    }

    public TileMap(Texture texture, Map map, int cellWidth, int cellHeight) {
        super(0, 0, 0, 0);
        mTexture = texture;
        mCellWidth = cellWidth;
        mCellHeight = cellHeight;

        mFrames = new TextureFrameSet(texture, cellWidth, cellHeight);
        setMap(map);
    }

    public TileMap(SpriteAsset asset, Map map) {
        super(0, 0, 0, 0);
        Sprite tileset = AssetCache.getInstance().getSprite(asset);
        mTexture = tileset.getTexture();
        mCellWidth = tileset.getWidth();
        mCellHeight = tileset.getHeight();
        mFrames = tileset.getFrames();
        setMap(map);
    }


    public void setMap(Map map) {
        mColumns = map.getWidth();
        mRows = map.getHeight();
        mVertexBuffer = BufferUtils.makeQuadFloatBuffer(mRows * mColumns);

        setActualWidth(mCellWidth * mColumns);
        setActualHeight(mCellHeight * mRows);

        updateVertices(map);
    }

    private void updateVertices(Map map) {

        mQuadCount = 0;
        float[] vertices = new float[16];

        RectF uv;
        float l, t, r, b;  // left, top, right, bottom

        int index;
        // weird degenerate triangle bug if you iterate
        // over columns instead of rows... no clue!
        for (int iy=0; iy<mRows; iy++) {
            for (int ix=0; ix<mColumns; ix++) {

                index = ix + iy * mColumns;
                uv = mFrames.getUVFrame( map.getTile(index) );

                if (uv==null) continue;

                mVertexBuffer.position( 16*mQuadCount );

                l = ix * mCellWidth;
                t = iy * mCellHeight;
                r = (ix+1) * mCellWidth;
                b = (iy+1) * mCellHeight;

                QuadDrawer.fillVertices(vertices, l, t, r, b);
                QuadDrawer.fillUVCoords(vertices, uv);
                mVertexBuffer.put(vertices);
                mQuadCount++;

            }
        }
    }


    public int getColumnsLength() {
        return mColumns;
    }

    public int getRowsLength() {
        return mRows;
    }

    public int getCellWidth() {
        return mCellWidth;
    }

    public int getCellHeight() {
        return mCellHeight;
    }


    @Override
    public void draw(BasicGLScript gls) {
        super.draw(gls);
        mTexture.bind();
        gls.drawQuadSet(mVertexBuffer, mQuadCount);
    }


    @Override
    protected PreRenderTexture preRender() {

        FrameBufferObject fbo = new FrameBufferObject();
        BasicGLScript gls = GameSkeleton.getGLScript();

        int w = (int) getActualWidth();
        int h = (int) getActualHeight();

        PreRenderTexture fboTex = new PreRenderTexture(w, h);
        fbo.start(fboTex);

        update();
        draw(gls);

        fbo.end();
        fbo.delete();
        return fboTex;
    }

}
