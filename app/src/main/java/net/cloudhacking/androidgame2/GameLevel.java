package net.cloudhacking.androidgame2;

import android.content.Context;

/**
 * Created by wcauchois on 1/4/15.
 */
public class GameLevel extends Component implements Renderable {
    private static final String TAG = GameLevel.class.getSimpleName();

    private SceneInfo mSceneInfo;
    private RenderLayer mBackgroundRenderLayer;
    private RenderLayer mTowerRenderLayer;
    private final int BACKGROUND_RENDER_LAYER_PRIORITY=1;
    private final int TOWER_RENDER_LAYER_PRIORITY=2;

    private JsonMap mMap;
    private TileSet mTileSet;

    private AnimatedSprite mTestSprite;


    // TODO: Not sure if this is necessary since map is now automatically scaled.
    private static final float OVERALL_SCALE = 1.0f;


    public GameLevel(SceneInfo sceneInfo) {
        mMap = new JsonMap();
        mTileSet = new TileSet(32, 32);

        mSceneInfo = sceneInfo;

        mBackgroundRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                                 BACKGROUND_RENDER_LAYER_PRIORITY);

        mTowerRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                            TOWER_RENDER_LAYER_PRIORITY);

        mTestSprite = new AnimatedSprite(R.drawable.tower_animation_test);

        mBackgroundRenderLayer.addMember(this);
        mTowerRenderLayer.addMember(mTestSprite);
    }

    @Override
    public void prepareResources(Context context) {
        mMap.loadFromResource(context, R.raw.simple_map);
        mTileSet.loadTexture(context, R.drawable.simple_tileset);

        mTestSprite.setGridPos(3, 6);

        mResourcesPrepared = true;
    }


    public int[] getLevelSize() {
        checkResourcesPrepared(TAG);

        int[] levelSize = new int[2];
        levelSize[0] = mMap.getWidth()  * mTileSet.getTileWidth()  * (int)OVERALL_SCALE;
        levelSize[1] = mMap.getHeight() * mTileSet.getTileHeight() * (int)OVERALL_SCALE;
        return levelSize;
    }


    /*
     * Update level here; creeps, towers, projectiles, etc.
     */
    public void update() {
        checkResourcesPrepared(TAG);

        mTestSprite.update();
    }


    /*
     * Draw the tiled background here.
     * TODO: Put this into its own class or something.
     */
    public void draw(QuadDrawer quadDrawer) {
        checkResourcesPrepared(TAG);

        mTileSet.prepareTexture(quadDrawer);
        for (int row = 0; row < mMap.getHeight(); row++) {
            for (int col = 0; col < mMap.getWidth(); col++) {
                mTileSet.drawTile(quadDrawer, mMap.getTile(col, row),
                        col * mTileSet.getTileWidth() * OVERALL_SCALE,
                        row * mTileSet.getTileHeight() * OVERALL_SCALE,
                        0,  // rotate by 0 degrees
                        OVERALL_SCALE,
                        OVERALL_SCALE);
            }
        }
    }
}
