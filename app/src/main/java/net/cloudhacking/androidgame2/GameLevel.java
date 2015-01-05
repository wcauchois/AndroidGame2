package net.cloudhacking.androidgame2;

import android.content.Context;

/**
 * Created by wcauchois on 1/4/15.
 */
public class GameLevel extends Component {
    private SimpleRenderService mRenderService;
    private JsonMap mMap;
    private TileSet mTileSet;

    public GameLevel(SimpleRenderService renderService) {
        mRenderService = renderService;
        mMap = new JsonMap();
        mTileSet = new TileSet(32, 32);
    }

    public void prepareResources(Context context) {
        mMap.loadFromResource(context, R.raw.simple_map);
        mTileSet.loadTexture(context, R.drawable.simple_tileset);
    }

    //private static final float OVERALL_SCALE = 2.0f;
    private static final float OVERALL_SCALE = 1.0f;

    public void draw() {
        QuadDrawer quadDrawer = mRenderService.getQuadDrawer();
        quadDrawer.beginDraw();
        for (int row = 0; row < mMap.getHeight(); row++) {
            for (int col = 0; col < mMap.getWidth(); col++) {
                mTileSet.drawTile(quadDrawer, mMap.getTile(col, row),
                        col * mTileSet.getTileWidth() * OVERALL_SCALE,
                        row * mTileSet.getTileHeight() * OVERALL_SCALE,
                        OVERALL_SCALE,
                        OVERALL_SCALE);
            }
        }
        quadDrawer.endDraw();
    }
}
