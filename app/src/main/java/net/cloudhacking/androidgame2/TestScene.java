package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraController;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.TileMap;
import net.cloudhacking.androidgame2.engine.foundation.Scene;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    TileMap mTileMap;
    Grid mGrid;

    @Override
    public void create() {

        mTileMap = new TileMap(
                Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE), 32, 32
        );
        add(mTileMap);

        mGrid = new Grid(mTileMap);
        mGrid.addSelectorListener(new Grid.CellSelectorListener() {
            @Override
            public void onCellSelect(Grid.Cell selected) {
                Grid.SELECTOR_ICON.startAnimationAt(TestScene.this, selected.getCenter());
            }
        });
        add(mGrid);

        getCameraController().setBoundaryRect( getMapRect() );
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

        // draw level
        gls.useCamera( getActiveCamera() );

        // Block camera movement while drawing the level in order to prevent drawing
        // artifacts.  I'm not actually sure if we need to do this or if its the
        // best way to implement it.
        // TODO: Not yet implemented in input manager or camera controller
        super.draw(gls);

        // draw ui
        gls.useCamera( getUICamera() );
        // mUIGroup.draw() or something

    }

}
