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
    private static final float TARGET_REACHED_THRESHOLD = 1 /* pixels */ ;

    private final static float MAX_V = 10.0f;
    private final static float ACCEL = 1.0f;

    private final static float STOP_DISTANCE
            = (float)( 0.5f*(-ACCEL) * Math.pow(MAX_V/ACCEL, 2) + Math.pow(MAX_V, 2)/ACCEL );


    private float mClickRadius;

    public float getClickRadius() {
        return mClickRadius;
    }

    public Mothership() {
        super(Assets.MOTHERSHIP);
        setScale(.3f); // temp
        mClickRadius = 1.414f*getScaledWidth()/2;
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
        private Vec2 mAccelVec;

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

            mAccelVec = getPos().vecTowards(mTarget.getCenter());
            setAcceleration(mAccelVec);
        }

        @Override
        public void update() {
            if (getVelocity().norm() > MAX_V) {
                setAcceleration(0, 0);
            }

            float d = getPos().distTo(mTarget.getCenter());
            if (d < STOP_DISTANCE) {
                setAcceleration(mAccelVec.negate());
            }
            if (d < TARGET_REACHED_THRESHOLD) {
                setAcceleration(0, 0);
                setVelocity(0, 0);
                mFinished = true;
            }
        }

    }

}
