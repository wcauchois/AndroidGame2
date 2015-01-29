package net.cloudhacking.androidgame2.example;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.element.Image;
import net.cloudhacking.androidgame2.engine.element.Sprite;
import net.cloudhacking.androidgame2.engine.element.TileMap;
import net.cloudhacking.androidgame2.engine.factory.SpriteFactory;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.CommonUtils;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by Andrew on 1/27/2015.
 */
public class TestCreepFactory extends SpriteFactory {

    private static final float SPEED = 80 /* pixels per second */ ;
    private static final float SCALE = .6f;
    private static final float TARGET_REACHED_THRESHOLD = 2 /* pixels */ ;

    private Grid mGrid;
    private Sprite mSprite;

    private class TestCreep extends SpriteSpawn {

        TileMap mPathMap;
        //Image mPathMap;
        private Grid.Cell mStart;

        private class TestCreepAnimation implements Animation {

            Grid.CellPath mCellPath;

            private boolean mDestinationReached;
            private boolean mCurrentlyAnimating;

            private int mFrameIndex;

            private class PathMap implements TileMap.Map {
                @Override
                public int getTile(int index) {
                    return mCellPath.containsCell(index) ? 1 : 0;
                }
                @Override
                public int getTile(int ix, int iy) {
                    return mCellPath.containsCell(ix, iy) ? 1 : 0;
                }
                @Override
                public int getWidth() {
                    return mGrid.getColumns();
                }
                @Override
                public int getHeight() {
                    return mGrid.getRows();
                }
            }

            public TestCreepAnimation() {
                mDestinationReached = false;
                mCurrentlyAnimating = false;
                mFrameIndex = CommonUtils.getRandom().nextInt(100);
                setVisibility(false);
                setScale(SCALE);
            }

            @Override
            public void reset() {}

            @Override
            public void start() {
                mCurrentlyAnimating = true;
                setVisibility(true);

                mCellPath = mGrid.getBestPath(mStart, mGrid.getCell(21,19));
                if (mCellPath == null) {
                    mCurrentlyAnimating = false;
                    return;
                }
                mPathMap = new TileMap(Assets.HIGHLIGHTER_8PX, new PathMap(),
                        mGrid.getCellWidth(), mGrid.getCellHeight());

                mPathMap.update();
                /*mPathMap = new Image( pathMap.getPreRendered() );
                mPathMap.moveToOrigin();
                mPathMap.setAlpha(.8f);
                mPathMap.update();
                mPathMap.setInactive();*/

                if (mCellPath.peek() == null) mDestinationReached = true;
            }

            @Override
            public boolean isAnimating() {
                return mCurrentlyAnimating;
            }

            private Vec2 getVelocityVecTowards(Grid.Cell next, float speed) {
                Vec2 dir = getPos().vecTowards(next.getCenter()).setNorm(speed);
                setRotation(dir.angle());
                return dir;
            }

            @Override
            public void update() {
                if (mDestinationReached) {
                    mCurrentlyAnimating = false;
                    return;
                }
                Grid.Cell next = mCellPath.peek();
                if (getPos().distTo(next.getCenter()) < TARGET_REACHED_THRESHOLD) {
                    mCellPath.pop();
                    next = mCellPath.peek();

                    if (next == null) {
                        mDestinationReached = true;
                    } else {
                        setVelocity(getVelocityVecTowards(next, SPEED));
                    }
                } else {
                    setVelocity(getVelocityVecTowards(next, SPEED));
                }
            }

            @Override
            public int getCurrentFrameIndex() {
                return mFrameIndex;
            }
        }


        public TestCreep() {
            super(mSprite);
        }

        @Override
        public TestCreepAnimation constructAnimation() {
            return new TestCreepAnimation();
        }

        @Override
        public void onSpawnAt(PointF target) {
            mStart = mGrid.nearestCell(target);
            super.onSpawnAt(target);
        }

        @Override
        public void onFinish() {}

        @Override
        public void draw(BasicGLScript gls) {
            mPathMap.draw(gls);
            super.draw(gls);
        }

    }


    public TestCreepFactory(Grid grid) {
        mGrid = grid;
        mSprite = AssetCache.getInstance().getSprite(Assets.POKEMON2);
    }

    @Override
    public TestCreep constructSpawn() {
        return new TestCreep();
    }



}
