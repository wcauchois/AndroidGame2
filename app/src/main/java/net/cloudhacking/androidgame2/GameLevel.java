package net.cloudhacking.androidgame2;


/**
 * Created by wcauchois on 1/4/15.
 */
public class GameLevel {
    private static final String TAG = GameLevel.class.getSimpleName();

    private SceneInfo mSceneInfo;
    private RenderLayer mBackgroundRenderLayer;
    private RenderLayer mTowerRenderLayer;
    private final int BACKGROUND_RENDER_LAYER_PRIORITY=1;
    private final int TOWER_RENDER_LAYER_PRIORITY=2;

    private TiledBackground mTiledBackground;

    private AnimatedSprite mTestSprite;


    public GameLevel(SceneInfo sceneInfo) {
        mSceneInfo = sceneInfo;

        // add tiled background render layer
        mBackgroundRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                                 BACKGROUND_RENDER_LAYER_PRIORITY);

        mTiledBackground = new TiledBackground(new TileSet(R.drawable.simple_tileset, 32, 32),
                                               new JsonMap(R.raw.simple_map));

        mBackgroundRenderLayer.addMember(mTiledBackground);

        // add tower render layer and test sprite
        mTowerRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                            TOWER_RENDER_LAYER_PRIORITY);

        mTestSprite = new AnimatedSprite(new TileSet(R.drawable.tower_animation_test, 32, 32));
        mTestSprite.setGridPos(3, 6);

        mTowerRenderLayer.addMember(mTestSprite);
    }


    public int[] getLevelSize() {
        return mTiledBackground.getSize();
    }


    /*
     * Update level here; creeps, towers, projectiles, etc.
     */
    public void update() {
        mTestSprite.update();
    }


}
