package net.cloudhacking.androidgame2;

/**
 * Created by Andrew on 1/7/2015.
 */
public class TiledBackground implements Renderable {
    private TileSet mTileSet;
    private JsonMap mJsonMap;

    // TODO: Not sure if this is necessary since map is now automatically scaled.
    private static final float OVERALL_SCALE = 1.0f;

    public TiledBackground(TileSet tileSet, JsonMap jsonMap) {
        mTileSet = tileSet;
        mJsonMap = jsonMap;
    }

    public int[] getSize() {
        int[] levelSize = new int[2];
        levelSize[0] = mJsonMap.getWidth()  * mTileSet.getTileWidth()  * (int)OVERALL_SCALE;
        levelSize[1] = mJsonMap.getHeight() * mTileSet.getTileHeight() * (int)OVERALL_SCALE;
        return levelSize;
    }


    public void draw(QuadDrawer quadDrawer) {
        mTileSet.prepareTexture(quadDrawer);
        for (int row = 0; row < mJsonMap.getHeight(); row++) {
            for (int col = 0; col < mJsonMap.getWidth(); col++) {
                mTileSet.drawTile(quadDrawer, mJsonMap.getTile(col, row),
                        col * mTileSet.getTileWidth() * OVERALL_SCALE,
                        row * mTileSet.getTileHeight() * OVERALL_SCALE,
                        0,  // rotate by 0 degrees
                        OVERALL_SCALE,
                        OVERALL_SCALE);
            }
        }
    }
}
