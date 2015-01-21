package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraGroup;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.TileMap;
import net.cloudhacking.androidgame2.engine.foundation.Scene;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    CameraGroup mActiveCameraGroup;
    CameraGroup mUICameraGroup;

    TileMap mTileMap;
    Grid mGrid;

    @Override
    public TestScene create() {

        mActiveCameraGroup = new CameraGroup( getActiveCamera() );
        mUICameraGroup = new CameraGroup( getUICamera() );


        mTileMap = new TileMap(
                Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE), 32, 32
        );
        mActiveCameraGroup.add(mTileMap);

        mGrid = new Grid(mTileMap);
        mGrid.addSelectorListener(new Grid.CellSelectorListener() {
            @Override
            public void onCellSelect(Grid.Cell selected) {
                Grid.SELECTOR_ICON.startAnimationAt(mActiveCameraGroup, selected.getCenter());
            }
        });
        mActiveCameraGroup.add(mGrid);


        add(mActiveCameraGroup);
        add(mUICameraGroup);

        getCameraController().setBoundaryRect(getMapRect());
        return this;
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

        super.update();
    }

    @Override
    public void draw(BasicGLScript gls) {

        super.draw(gls);
    }

}
