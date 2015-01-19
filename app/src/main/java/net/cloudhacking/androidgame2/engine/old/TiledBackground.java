package net.cloudhacking.androidgame2.engine.old;

import net.cloudhacking.androidgame2.engine.utils.JsonMap;

/**
 * Created by Andrew on 1/7/2015.
 */
public class TiledBackground implements RenderableOld {
    private static final String TAG = TiledBackground.class.getSimpleName();

    private TileSet mTileSet;
    private JsonMap mJsonMap;

    // TODO: Not sure if this is necessary since map is now automatically scaled.
    private static final float OVERALL_SCALE = 1.0f;

    public TiledBackground(TileSet tileSet, JsonMap jsonMap) {
        mTileSet = tileSet;
        mJsonMap = jsonMap;
    }

    public int[] getSize() {
        int[] size = new int[2];
        size[0] = mJsonMap.getWidth()  * mTileSet.getTileWidth()  * (int)OVERALL_SCALE;
        size[1] = mJsonMap.getHeight() * mTileSet.getTileHeight() * (int)OVERALL_SCALE;
        return size;
    }

    public int[] getGridSize() {
        int[] gridSize = new int[2];
        gridSize[0] = mJsonMap.getWidth();
        gridSize[1] = mJsonMap.getHeight();
        return gridSize;
    }


    public void draw(QuadDrawerOld quadDrawer) {
        mTileSet.prepareTexture(quadDrawer);
        for (int row = 0; row < mJsonMap.getHeight(); row++) {
            for (int col = 0; col < mJsonMap.getWidth(); col++) {
                mTileSet.drawTile(quadDrawer, mJsonMap.getTile(col, row),
                        (col+0.5f) * mTileSet.getTileWidth()  * OVERALL_SCALE,
                        (row+0.5f) * mTileSet.getTileHeight() * OVERALL_SCALE,
                        0,  // rotate by 0 degrees
                        OVERALL_SCALE,
                        OVERALL_SCALE);
            }
        }
    }
}
