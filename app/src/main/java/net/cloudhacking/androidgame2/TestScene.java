package net.cloudhacking.androidgame2;

import android.graphics.RectF;
import android.os.ConditionVariable;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraController;
import net.cloudhacking.androidgame2.engine.foundation.Scene;
import net.cloudhacking.androidgame2.engine.TileMap;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    BasicGLScript mGLScript;
    ConditionVariable mInputSyncObj;
    CameraController mCameraController;

    TileMap mTileMap;

    @Override
    public void create() {

        TDGame instance = (TDGame)TDGame.getInstance();

        mTileMap = new TileMap(
                Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE), 32, 32
        );
        add(mTileMap);

        TestClickDrawer testDrawer = new TestClickDrawer();
        instance.getInputManager().addClickListener(testDrawer);

        mCameraController = instance.getCameraController();
        mCameraController.setBoundaryRect( getMapRect() );

        mInputSyncObj = instance.getInputSyncObj();
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
    public void draw() {

        mGLScript = BasicGLScript.get();

        // draw level
        mGLScript.useCamera(mCameraController.getActiveCamera());

        mInputSyncObj.close();  // block camera movement while drawing  TODO: Not yet implemented
        super.draw();
        mInputSyncObj.open();

        // draw ui
        mGLScript.useCamera(mCameraController.getUICamera());
        // mUIGroup.draw() or something;

    }

}
