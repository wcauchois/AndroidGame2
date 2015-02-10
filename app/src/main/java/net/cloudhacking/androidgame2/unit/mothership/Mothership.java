package net.cloudhacking.androidgame2.unit.mothership;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;
import net.cloudhacking.androidgame2.unit.ControllableUnit;

/**
 * Created by Andrew on 2/6/2015.
 */
public class Mothership extends ControllableUnit {
    private static final float TARGET_REACHED_THRESHOLD = 1.5f /* pixels */ ;
    private static final float MAX_V = 10.0f;
    private static final float ROT_SPEED = 1.0f;

    private float mClickRadius;

    public float getClickRadius() {
        return mClickRadius;
    }

    public Mothership() {
        super(Assets.MOTHERSHIP);
        setScale(.3f); // temp
        mClickRadius = 1.414f*getScaledWidth()/2;
        setMoveSpeed(MAX_V);
    }

    @Override
    public boolean containsPt(PointF touch) {
        return (getPos().distTo(touch) < mClickRadius);
    }

    public void moveTo(Grid.Cell target) {
        forceAction(new Move(target));
    }


    //----------------------------------------------------------------------------------------------

    private class Move extends Action {
        private Grid.Cell mTarget;
        private float mRotGoal;
        private float mRotDir;

        public Move(Grid.Cell target) {
            super(ActionType.MOVE);
            mTarget = target;
        }

        @Override
        public void onStart() {
            if (mTarget == null) {
                mFinished = true;
                return;
            }
            Vec2 dir = getPos().vecTowards(mTarget.getCenter());
            setVelocity(dir.setNorm(getMoveSpeed()));

            float twoPI = 2*(float)Math.PI;
            float rotCur = getRotation();
            mRotGoal = (dir.angle()+(float)Math.PI/2) % twoPI;
            mRotDir = (mRotGoal-rotCur) % twoPI > Math.PI ? -1 : 1;
            d("rot cur: " + rotCur);
            d("rot goal: " + mRotGoal);
            d("rot dir: " + mRotDir);
            setRotationSpeed(mRotDir * ROT_SPEED);
        }

        @Override
        public void update() {
            if (mRotDir > 0) {
                if (mRotGoal - getRotation() < 0) setRotationSpeed(0);
            } else {
                if (mRotGoal - getRotation() > 2*(float)Math.PI) setRotationSpeed(0);
            }
            float d = getPos().distTo(mTarget.getCenter());
            if (d < TARGET_REACHED_THRESHOLD) {
                setVelocity(0, 0);
                setRotationSpeed(0);
                mFinished = true;
            }
        }

    }

}
