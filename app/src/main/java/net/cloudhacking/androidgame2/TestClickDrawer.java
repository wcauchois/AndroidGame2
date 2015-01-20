package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.CameraController;
import net.cloudhacking.androidgame2.engine.Image;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Signal;

/**
 * Created by Andrew on 1/19/2015.
 */
public class TestClickDrawer extends Loggable {

    private Image image;
    private CameraController mCameraController;

    public TestClickDrawer(InputManager inputManager) {
        mCameraController = TDGame.getInstance().getCameraController();

        inputManager.clickUp.connect(new Signal.Listener<InputManager.ClickEvent>() {
            public void onSignal(InputManager.ClickEvent e) {
                handleClickUp(e.getPos());
            }
        });
    }

    private void handleClickUp(PointF up) {
        image = new Image(Assets.TEST_TILESET);
        image.setPos( mCameraController.getActiveCamera().cameraToScene(up) );
        image.setRotatable(true);
        image.setRotationSpeed( 360 * (float)Math.random() );
        TDGame.getInstance().getScene().add(image);
    }
}
