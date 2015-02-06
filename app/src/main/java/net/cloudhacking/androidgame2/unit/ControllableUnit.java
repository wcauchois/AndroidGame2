package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

import java.util.LinkedList;

/**
 * Created by research on 1/30/15.
 */
public class ControllableUnit extends SelectableUnit {

    // can add more to this
    public static enum ActionType {
        MOVE,
        ATTACK,
        DEFEND,
        STOP
    }


    /**
     * Use this class to move, target, queue animations, etc
     */
    public static class Action {
        protected ActionType mType;
        protected boolean mFinished;

        public Action(ActionType type) {
            mType = type;
            mFinished = true;
        }

        public void start() {
            mFinished = false;
            onStart();
        }

        public void onStart() {}

        public ActionType getType() {
            return mType;
        }

        public void interrupt() {
            mFinished = true;
            onInterrupt();
        }

        public void onInterrupt() {}

        public void update() {}

        public boolean isFinished() {
            return mFinished;
        }

        public static final Action NULL = new Action(ActionType.STOP) {
            @Override
            public boolean isFinished() { return true; }
        };
    }


    private Action mCurrentAction;
    private Action mDefaultAction;
    private LinkedList<Action> mActionQueue;

    public ControllableUnit(SpriteAsset asset) {
        super(asset);
        mActionQueue = new LinkedList<Action>();
        mCurrentAction = Action.NULL;
        mDefaultAction = Action.NULL;
    }

    public ActionType getCurrentAction() {
        return mCurrentAction.getType();
    }

    public void setDefaultAction(Action action) {
        mDefaultAction = action;
    }

    public void queueAction(Action action, boolean interrupt) {
        if (interrupt) {
            mCurrentAction.interrupt();
            mActionQueue.addFirst(action);
            return;
        }
        mActionQueue.addLast(action);
    }

    public void queueAction(Action action) {
        queueAction(action, false);
    }

    public void forceAction(Action action) {
        mCurrentAction = action;
        mCurrentAction.start();
    }

    private void doNextAction() {
        mCurrentAction = mActionQueue.pollFirst();
        if (mCurrentAction == null) {
            mCurrentAction = mDefaultAction;
        }
        mCurrentAction.start();
    }

    @Override
    public void update() {
        if (mCurrentAction.isFinished()) {
            doNextAction();
        } else {
            mCurrentAction.update();
        }
        super.update();
    }

}
