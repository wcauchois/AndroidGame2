package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.Image;
import net.cloudhacking.androidgame2.engine.foundation.Scene;
import net.cloudhacking.androidgame2.engine.utils.InputManager;
import net.cloudhacking.androidgame2.engine.utils.Loggable;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Signal;

/**
 * Created by Andrew on 1/19/2015.
 */
public class TestClickDrawer extends Loggable {

    private Image image;

    public TestClickDrawer(final Scene scene) {

        scene.getInputManager().clickUp.connect(new Signal.Listener<InputManager.ClickEvent>() {
            public boolean onSignal(InputManager.ClickEvent e) {
                handleClickUp(scene, e.getPos());
                return true;
            }
        });
    }

    private void handleClickUp(Scene scene, PointF up) {
        image = new Image(Assets.TEST_TILESET);
        image.setPos( scene.activeCameraToScene(up) );
        image.setRotatable(true);
        image.setRotationSpeed( 360 * (float)Math.random() );
        scene.add(image);
    }
}
