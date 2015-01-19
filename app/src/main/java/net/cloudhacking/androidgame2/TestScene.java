package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.GameSkeleton;
import net.cloudhacking.androidgame2.engine.Image;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    private Image image;

    @Override
    public void create() {
        image = new Image(Assets.TEST_TILESET);
        image.setRotatable(false);
        image.setVelocity(new Vec2(10, 10));
        add(image);
    }


    @Override
    public float getMapWidth() {
        return image.getWidth();
    }

    @Override
    public float getMapHeight() {
        return image.getHeight();
    }

    @Override
    public RectF getMapRect() {
        return image.getTexture().getRect();
    }


    @Override
    public void update() {

        super.update();  // update all member entities
    }

    @Override
    public void draw() {

        super.draw();  // draw all member entities
    }

}
