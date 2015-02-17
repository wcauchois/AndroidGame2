package net.cloudhacking.androidgame2.unit.mothership;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;
import net.cloudhacking.androidgame2.unit.CDUnit;
import net.cloudhacking.androidgame2.unit.ControllableUnit;

/**
 * Created by Andrew on 2/6/2015.
 */
public class Mothership extends ControllableUnit {
    private static final float TARGET_REACHED_THRESHOLD = 1.5f /* pixels */ ;
    private static final float MAX_V = 10.0f;

    private float mClickRadius;

    public float getClickRadius() {
        return mClickRadius;
    }

    public Mothership() {
        super(Assets.MOTHERSHIP3);
        mClickRadius = 1.414f* getWidth()/2;
        setAlpha(0.6f);
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
        }

        @Override
        public void update() {
            float d = getPos().distTo(mTarget.getCenter());
            if (d < TARGET_REACHED_THRESHOLD) {
                setVelocity(0, 0);
                mFinished = true;
            }
        }
    }


    private class DeployUnit extends Action {
        private CDUnit mUnit;

        public DeployUnit(CDUnit unit) {
            super(ActionType.DEFEND);
            mUnit = unit;
        }

    }

}
