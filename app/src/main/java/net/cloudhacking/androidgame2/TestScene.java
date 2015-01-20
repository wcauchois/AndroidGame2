package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.BasicGLScript;
import net.cloudhacking.androidgame2.engine.CameraController;
import net.cloudhacking.androidgame2.engine.Image;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    BasicGLScript mGLScript;
    CameraController mCameraController;

    private Image image1, image2;


    @Override
    public void build() {
        image1 = new Image(Assets.TEST_TILESET);
        image1.setRotatable(false);
        image1.setScalable(false);
        add(image1);

        image2 = new Image(Assets.TEST_TILESET);
        image2.setRotatable(false);
        image2.setScalable(false);
        image2.setVelocity(new Vec2(15, 15));
        add(image2);

        TestClickDrawer testDrawer = new TestClickDrawer();
        TDGame.getInstance().getInputManager().addClickListener(testDrawer);

        mCameraController = TDGame.getInstance().getCameraController();
    }

    @Override
    public void destroy() {

    }


    @Override
    public float getMapWidth() {
        return image1.getWidth();
    }

    @Override
    public float getMapHeight() {
        return image1.getHeight();
    }

    @Override
    public RectF getMapRect() {
        return image1.getTexture().getRect();
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
