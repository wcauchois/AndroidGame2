package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.CameraController;
import net.cloudhacking.androidgame2.engine.Image;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/19/2015.
 */
public class TestClickDrawer extends Loggable implements InputManager.ClickListener {

    private Image image;
    private CameraController mCameraController;

    public TestClickDrawer() {
        mCameraController = TDGame.getInstance().getCameraController();
    }

    @Override
    public void onClickDown(PointF down) {}

    @Override
    public void onClickUp(PointF up) {
        image = new Image(Assets.TEST_TILESET);
        image.setPos( mCameraController.getActiveCamera().cameraToScene(up) );
        image.setRotatable(true);
        image.setRotationSpeed( 360 * (float)Math.random() );
        TDGame.getInstance().getScene().add(image);
    }

    @Override
    public void onClickCancel() {}
}
