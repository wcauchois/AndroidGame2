package net.cloudhacking.androidgame2;


import android.util.Log;

import java.util.ArrayList;

/**
 * Created by wcauchois on 1/4/15.
 */
public class GameLevel {
    private static final String TAG = GameLevel.class.getSimpleName();

    private SceneInfo mSceneInfo;
    private LevelGrid mLevelGrid;
    private static final int NODE_PIXEL_WIDTH=32;
    private static final int NODE_PIXEL_HEIGHT=32;
    private static final int GRID_WIDTH=30;
    private static final int GRID_HEIGHT=20;

    private RenderLayer mBackgroundRenderLayer;
    private RenderLayer mTowerRenderLayer;
    private RenderLayer mCreepRenderLayer;
    private final int BACKGROUND_RENDER_LAYER_PRIORITY=1;
    private final int TOWER_RENDER_LAYER_PRIORITY=2;
    private final int CREEP_RENDER_LAYER_PRIORITY=3;

    private TiledBackground mTiledBackground;
    private SpriteGroup<Tower> mBasicTowerGroup;

    private Tower mTestTower;

    private ArrayList<LevelGrid.GridItem> mGridItems = LevelGrid.getGridItems();


    public GameLevel(SceneInfo sceneInfo) {
        mSceneInfo = sceneInfo;

        mLevelGrid = new LevelGrid(GRID_WIDTH, GRID_HEIGHT, NODE_PIXEL_WIDTH, NODE_PIXEL_HEIGHT);


        // add tiled background render layer
        mBackgroundRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                                 BACKGROUND_RENDER_LAYER_PRIORITY);

        mTiledBackground = new TiledBackground(new TileSet(R.drawable.simple_tileset,
                                                           NODE_PIXEL_WIDTH,
                                                           NODE_PIXEL_HEIGHT),
                                               new JsonMap(R.raw.simple_map));

        mBackgroundRenderLayer.addMember(mTiledBackground);


        // add tower render layer
        mTowerRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                            TOWER_RENDER_LAYER_PRIORITY);

        mBasicTowerGroup = new SpriteGroup<Tower>(
                new TileSet(R.drawable.tower_animation_test, NODE_PIXEL_WIDTH, NODE_PIXEL_HEIGHT)
        );

        mTowerRenderLayer.addMember(mBasicTowerGroup);

        mTestTower = new Tower();
        mTestTower.setToGridNode(mLevelGrid, 3, 6);

        mBasicTowerGroup.addGridItem(mTestTower);


        // add creep render layer
        mCreepRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                CREEP_RENDER_LAYER_PRIORITY);

        Log.d(TAG, "tower render layer members: "+mTowerRenderLayer.getLayerMembers());
        Log.d(TAG, "basic tower group members: "+mBasicTowerGroup.getGridItemList());
    }


    public int[] getLevelSize() {
        return mLevelGrid.getSize();
    }


    /*
     * Update level here; creeps, towers, projectiles, etc.
     */
    public void update() {

        for (LevelGrid.GridItem gridItem : mGridItems) {
            gridItem.update();
        }

    }


}
