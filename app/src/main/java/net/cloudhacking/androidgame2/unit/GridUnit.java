package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by Andrew on 2/6/2015.
 */
public class GridUnit extends ControllableUnit {

    public GridUnit(SpriteAsset asset) {
        super(asset);
    }

    public void moveOnPath(Grid.CellPath path) {
        forceAction(new GridMove(path));
    }

    private static final float TARGET_REACHED_THRESHOLD = 1 /* pixels */ ;

    public class GridMove extends Action {
        private Grid.CellPath mPath;
        private boolean mDestinationReached;

        public GridMove(Grid.CellPath path) {
            super(ActionType.MOVE);
            mPath = path;
            mDestinationReached = false;
        }

        private Vec2 getVelocityVecTowards(Grid.Cell next) {
            return getPos().vecTowards(next.getCenter()).setNorm(getMoveSpeed());
        }

        @Override
        public void onStart() {
            Grid.Cell next = mPath.pop();
            if (next == null) {
                mDestinationReached = true;
                return;
            }
            setPos(next.getCenter());

            next = mPath.peek();
            if (next == null) {
                mDestinationReached = true;
            } else {
                setVelocity(getVelocityVecTowards(next));
            }
        }

        @Override
        public void update() {
            if (mDestinationReached) {
                setVelocity(0,0);
                mFinished = true;
                return;
            }
            Grid.Cell next = mPath.peek();
            if (getPos().distTo(next.getCenter()) < TARGET_REACHED_THRESHOLD) {
                mPath.pop();
                next = mPath.peek();

                if (next == null) {
                    mDestinationReached = true;
                } else {
                    setVelocity(getVelocityVecTowards(next));
                }
            }
        }

    }

}
