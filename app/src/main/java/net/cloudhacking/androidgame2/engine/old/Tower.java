package net.cloudhacking.androidgame2.engine.old;

/**
 * Created by Andrew on 1/7/2015.
 *
 * TODO: Eventually this class should probably be abstract
 */
public /*abstract*/ class Tower extends AnimatedGridItem {

    public Tower() {
    }

    @Override
    public void update() {
        updateAnimation();

        // increment rotation for debug
        setRotation((getRotation()+5) % 360);
    }

}
