package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraController;
import net.cloudhacking.androidgame2.engine.Image;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.TileMap;
import net.cloudhacking.androidgame2.engine.utils.JsonMap;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    BasicGLScript mGLScript;
    CameraController mCameraController;

    TileMap mTileMap;

    @Override
    public void create() {

        mTileMap = new TileMap(
                Assets.TEST_TILESET, new JsonMap(Resources.JSON_MAP_SIMPLE), 32, 32
        );
        add(mTileMap);

        TestClickDrawer testDrawer = new TestClickDrawer();
        TDGame.getInstance().getInputManager().addClickListener(testDrawer);

        mCameraController = TDGame.getInstance().getCameraController();
        mCameraController.setBoundaryRect( getMapRect() );
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
        super.draw(); // don't call super.draw() here, do like mGameLevel.draw() or something

        // draw ui
        mGLScript.useCamera(mCameraController.getUICamera());
        // mUIGroup.draw() or something;

    }

}
