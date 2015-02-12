package net.cloudhacking.androidgame2.unit.gridUnit;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.element.shape.Circle;
import net.cloudhacking.androidgame2.engine.gl.GLColor;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;
import net.cloudhacking.androidgame2.engine.utils.Vec2;
import net.cloudhacking.androidgame2.unit.ControllableUnit;

/**
 * Created by Andrew on 2/6/2015.
 */
public class GridUnit extends ControllableUnit {
    private static final float TARGET_REACHED_THRESHOLD = 1.5f /* pixels */ ;

    private Grid.Cell mLocation;

    public GridUnit(SpriteAsset asset) {
        super(asset);
        mLocation = null;
    }

    public void moveOnPath(Grid.CellPath path) {
        forceAction(new GridMove(path));
    }

    public Grid.Cell getLocation() {
        return mLocation;
    }

    public void setLocation(Grid.Cell loc) {
        mLocation = loc;
    }

    public void setToCell(Grid.Cell loc) {
        mLocation = loc;
        setPos(loc.getCenter());
    }


    //----------------------------------------------------------------------------------------------

    public class GridMove extends Action {
        private Grid.CellPath mPath;
        private boolean mDestinationReached;
        private Circle mDestinationAnim;

        public GridMove(Grid.CellPath path) {
            super(ActionType.MOVE);
            mPath = path;
            mDestinationAnim = new Circle(4, 1, GLColor.GREEN);
            mDestinationAnim.hide();
            getParent().add(mDestinationAnim);
            getParent().bringToFront(GridUnit.this);
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
                mDestinationAnim.setPos(mPath.getDestination().getCenter());
                mDestinationAnim.show();
            }

            GridUnit.this.setSelectable(false);
        }

        @Override
        public void update() {
            if (mDestinationReached) {
                setVelocity(0, 0);
                setPos(getLocation().getCenter()); // fix for: sometimes units end up offset by 1px
                mFinished = true;
                mDestinationAnim.hide();
                GridUnit.this.setSelectable(true);
                return;
            }

            if (GridUnit.this.isSelected()) {
                mDestinationAnim.show();
            } else {
                mDestinationAnim.hide();
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

        @Override
        public void onInterrupt() {
            getParent().remove(mDestinationAnim);
            GridUnit.this.setSelectable(true);
        }

    }

}
