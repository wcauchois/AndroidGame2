package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

import java.util.LinkedList;

/**
 * Created by research on 1/30/15.
 */
public class ControllableUnit extends CDUnit {

    // can add more to this
    public static enum ActionType {
        MOVE, BUILD, ATTACK, DEFEND, STOP, NULL
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

        protected void onStart() {}

        public ActionType getType() {
            return mType;
        }

        public void interrupt() {
            mFinished = true;
            onInterrupt();
        }

        protected void onInterrupt() {}

        public void update() {}

        public boolean isFinished() {
            return mFinished;
        }

        public static final Action NULL = new Action(ActionType.NULL) {
            @Override public boolean isFinished() { return true; }
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
        mCurrentAction.interrupt();
        mCurrentAction = action;
        mCurrentAction.start();
        mActionQueue.clear();
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
        }
        mCurrentAction.update();
        super.update();
    }

}
