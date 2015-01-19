package net.cloudhacking.androidgame2;

import android.graphics.RectF;

import net.cloudhacking.androidgame2.engine.Image;
import net.cloudhacking.androidgame2.engine.Scene;
import net.cloudhacking.androidgame2.engine.utils.MatrixUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;

/**
 * Created by Andrew on 1/15/2015.
 */
public class TestScene extends Scene {

    private Image image;

    @Override
    public void create() {
        image = new Image(Assets.TEST_TILESET);
        image.setPos(new PointF(10, 10));
        image.setScale(10);
        image.updateMatrix();
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

        super.update();
    }

    @Override
    public void draw() {

        super.draw();
    }

}
