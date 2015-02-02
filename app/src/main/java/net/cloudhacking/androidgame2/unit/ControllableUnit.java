package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

import java.util.LinkedList;

/**
 * Created by research on 1/30/15.
 */
public class ControllableUnit extends Unit {

    public enum Action {
        MOVE, ATTACK, DEFEND, STOP
    }

    private Action mCurrentAction;
    private LinkedList<Action> mActionQueue;

    public ControllableUnit(SpriteAsset asset) {
        super(asset);
        mActionQueue = new LinkedList<Action>();
        mCurrentAction = Action.STOP;
    }


    public void queueAction(Action action, boolean force) {
        mActionQueue.addLast(action);
    }

    public void doAction(Action action) {
        switch(action) {}
    }

    @Override
    public void update() {
        // update current action or switch to next action
        mCurrentAction = mActionQueue.pollFirst();
        super.update();
    }

}
