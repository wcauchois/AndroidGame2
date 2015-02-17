package net.cloudhacking.androidgame2.engine.utils;

import net.cloudhacking.androidgame2.engine.element.Entity;

/**
 * Created by Andrew on 2/16/2015.
 */
public abstract class TimedAction extends Entity {

    private float mPeriod;  // in seconds
    private float mThreshold;

    public TimedAction(float periodInMillis) {
        super();
        mPeriod = 1000*periodInMillis;
        setInactive();
    }

    abstract protected void doAction();

    public void start() {
        mThreshold = 0;
    }

    public void pause() {
        setInactive();
    }

    public void resume() {
        setActive();
    }

    public void stop() {
        setInactive();
        mThreshold = 0;
    }

    @Override
    public void update() {
        mThreshold += GameTime.getFrameDelta();

        if (mThreshold > mPeriod) {
            doAction();
            mThreshold -= mPeriod;
        }
    }




}
