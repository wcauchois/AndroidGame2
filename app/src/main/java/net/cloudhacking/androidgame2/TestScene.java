package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraGroup;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.TileMap;
import net.cloudhacking.androidgame2.engine.foundation.Group;
import net.cloudhacking.androidgame2.engine.foundation.Scene;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    CameraGroup mLevelGroup;
    CameraGroup mUIGroup;

    TileMap mTileMap;
    Grid mGrid;

    @Override
    public void create() {

        mLevelGroup = new CameraGroup( getActiveCamera() );
        mUIGroup = new CameraGroup( getUICamera() );

        mTileMap = new TileMap(
                Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE), 32, 32
        );
        mLevelGroup.add(mTileMap);

        mGrid = new Grid(mTileMap);
        mGrid.addSelectorListener(new Grid.CellSelectorListener() {
            @Override
            public void onCellSelect(Grid.Cell selected) {
                Grid.SELECTOR_ICON.startAnimationAt(mLevelGroup, selected.getCenter());
            }
        });
        mLevelGroup.add(mGrid);

        add(mLevelGroup);
        add(mUIGroup);

        getCameraController().setBoundaryRect(getMapRect());
    }

    @Override
    public void destroy() {

    }


    @Override
    public float getMapWidth() {
        return mTileMap.getWidth();
    }

    @Override
    public float getMapHeight() {
        return mTileMap.getHeight();
    }

    @Override
    public RectF getMapRect() {
        return mTileMap.getRect();
    }


    @Override
    public void update() {

        super.update();  // update all member entities
    }

    @Override
    public void draw(BasicGLScript gls) {

        super.draw(gls);
    }

}
