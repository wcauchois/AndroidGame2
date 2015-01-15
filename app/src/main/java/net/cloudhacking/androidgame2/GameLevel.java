package net.cloudhacking.androidgame2;


import net.cloudhacking.androidgame2.engine.AnimatedGridItem.AnimationCache;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;
import net.cloudhacking.androidgame2.engine.LevelGrid;
import net.cloudhacking.androidgame2.engine.RenderLayer;
import net.cloudhacking.androidgame2.engine.SceneInfo;
import net.cloudhacking.androidgame2.engine.SimpleRenderService;
import net.cloudhacking.androidgame2.engine.SpriteGroup;
import net.cloudhacking.androidgame2.engine.TileSet;
import net.cloudhacking.androidgame2.engine.TiledBackground;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

import java.util.ArrayList;

/**
 * Created by wcauchois on 1/4/15.
 */
public class GameLevel extends Loggable {

    private SceneInfo mSceneInfo;
    private LevelGrid mLevelGrid;
    private static final int NODE_PIXEL_WIDTH=32;   // probably should ensure that all game maps
    private static final int NODE_PIXEL_HEIGHT=32;  // and tile sets correspond to these variables
    private static final int GRID_WIDTH=30;
    private static final int GRID_HEIGHT=20;

    private RenderLayer mBackgroundRenderLayer;
    private RenderLayer mTowerRenderLayer;
    private RenderLayer mCreepRenderLayer;
    private RenderLayer mProjectileRenderLayer;
    private final int BACKGROUND_RENDER_LAYER_PRIORITY=1;
    private final int TOWER_RENDER_LAYER_PRIORITY=2;
    private final int CREEP_RENDER_LAYER_PRIORITY=3;
    private final int PROJECTILE_RENDER_LAYER_PRIORITY=4;

    private TiledBackground mTiledBackground;
    private SpriteGroup mBasicTowerGroup;

    private Tower[] mTestTowers;

    private ArrayList<LevelGrid.GridItem> mGridItems = LevelGrid.getGridItems();


    public GameLevel(SceneInfo sceneInfo) {
        mSceneInfo = sceneInfo;

        // use this for handling grid and pixel coordinates
        mLevelGrid = new LevelGrid(GRID_WIDTH, GRID_HEIGHT, NODE_PIXEL_WIDTH, NODE_PIXEL_HEIGHT);

        // add tiled background render layer
        mBackgroundRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                                 BACKGROUND_RENDER_LAYER_PRIORITY);

        mTiledBackground = new TiledBackground(new TileSet(R.drawable.simple_tileset,
                                                           NODE_PIXEL_WIDTH,
                                                           NODE_PIXEL_HEIGHT),
                                               new JsonMap(R.raw.simple_map));

        mBackgroundRenderLayer.addMember(mTiledBackground);  // add the background to the background render layer


        // add tower render layer and init Tower Class
        mTowerRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                            TOWER_RENDER_LAYER_PRIORITY);

        mBasicTowerGroup = new SpriteGroup(
                new TileSet(R.drawable.tower_animation_test, NODE_PIXEL_WIDTH, NODE_PIXEL_HEIGHT)
        );

        mTowerRenderLayer.addMember(mBasicTowerGroup);  // add this sprite group to the tower render layer

        // bind animation sequence to Tower class; this sequence will alternate between first and
        // second tile indexes of the mBasicTowerGroup's tile set.
        AnimationCache.addAnimation(Tower.class, "fire", new int[]{0, 1});

        mTestTowers = new Tower[5];
        for (int i=0; i<5; i++) {
            mTestTowers[i] = new Tower();
            mTestTowers[i].setToGridNode(mLevelGrid, 4+i, 5);
            mTestTowers[i].queueAnimation("fire", 750, true); // switch frames every 750ms and loop for forever
            mBasicTowerGroup.addGridItem(mTestTowers[i]);  // add to tower sprite group
        }


        // add creep render layer
        mCreepRenderLayer = new RenderLayer(new SimpleRenderService(mSceneInfo),
                                            CREEP_RENDER_LAYER_PRIORITY);

        // TODO: create sprite group, creeps, and set waypoints
    }


    public Vec2 getLevelSize() {
        return mLevelGrid.getSize();
    }


    /*
     * Update level here; creeps, towers, projectiles, etc.
     */
    public void update() {

        // update creeps and towers and anything that resides on the grid
        for (LevelGrid.GridItem gridItem : mGridItems) {
            gridItem.update();
        }
        // update projectiles here?

    }


}
