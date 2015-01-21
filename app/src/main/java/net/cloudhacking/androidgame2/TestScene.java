package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraGroup;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.foundation.Image;
import net.cloudhacking.androidgame2.engine.foundation.TileMap;
import net.cloudhacking.androidgame2.engine.ui.Button;
import net.cloudhacking.androidgame2.engine.ui.RootWidget;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    CameraGroup mActiveCameraGroup;
    CameraGroup mUICameraGroup;

    TileMap mTileMap;
    Grid mGrid;
    TestClickDrawer mTestDrawer;

    @Override
    public TestScene create() {

        mActiveCameraGroup = new CameraGroup( getActiveCamera() );
        mUICameraGroup = new CameraGroup( getUICamera() );

        add(mActiveCameraGroup);
        add(mUICameraGroup);


        mTileMap = new TileMap(
                Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE), 32, 32
        );
        mActiveCameraGroup.add(mTileMap);

        mGrid = new Grid(mTileMap);
        // d("best path: " + mGrid.getBestPath(mGrid.getCell(1,1), mGrid.getCell(5,10)) );

        mGrid.addSelectorListener(new Grid.CellSelectorListener() {
            @Override
            public void onCellSelect(Grid.Cell selected) {
                Grid.SELECTOR_ICON.startAnimationAt(mActiveCameraGroup, selected.getCenter());
            }
        });
        mActiveCameraGroup.add(mGrid);


        // UI

        RootWidget rootWidget = new RootWidget( getInputManager() );
        // example of stretching with rect
        rootWidget.addToFront(new Button(new RectF(0.0f, 0.0f, 500.0f, 200.0f), Assets.OK_BUTTON));
        mUICameraGroup.add(rootWidget);


        return this;
    }

    @Override
    public void destroy() {

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
