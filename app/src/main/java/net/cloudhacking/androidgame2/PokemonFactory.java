package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.engine.Grid;
import net.cloudhacking.androidgame2.engine.factory.SpriteFactory;
import net.cloudhacking.androidgame2.engine.utils.AssetCache;
import net.cloudhacking.androidgame2.engine.utils.GameTime;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

import java.util.Random;

/**
 * Created by Andrew on 1/21/2015.
 */
public class PokemonFactory extends SpriteFactory {

    private static final float SPEED = 180 /* pixels per second */ ;
    private static final int MAX_FRAME_INDEX = 492 /* number of pokemon sprites lol! */;
    private static final float TARGET_REACHED_THRESHOLD = 5 /* pixels */ ;

    private static final float SPAWN_FREQUENCY = 10f /* spawns per second */ ;


    private class PokemonSprite extends SpriteSpawn {

        private class PokemonAnimation implements Animation {

            private Grid.CellPath mPath;
            private boolean mDestinationReached;
            private boolean mCurrentlyAnimating;

            private int mFrameIndex;

            public PokemonAnimation() {
                mDestinationReached = false;
                mCurrentlyAnimating = false;
                mFrameIndex = mRandGen.nextInt(MAX_FRAME_INDEX + 1);  // generate random pokemon
                setVisibility(false);
            }

            @Override
            public void start() {
                mCurrentlyAnimating = true;

                // generate random starting and ending locations
                int startIX, startIY, goalIX, goalIY;
                startIX = mRandGen.nextInt(mGrid.getColumns());
                startIY = mRandGen.nextInt(mGrid.getRows());
                goalIX = mRandGen.nextInt(mGrid.getColumns());
                goalIY = mRandGen.nextInt(mGrid.getRows());

                // calculate best path
                mPath = mGrid.getBestPath(startIX, startIY, goalIX, goalIY);

                setPos(mPath.pop().getCenter());
                setVisibility(true);

                if (mPath.peek() == null) mDestinationReached = true;
            }

            @Override
            public void reset() {}

            @Override
            public boolean isAnimating() {
                return mCurrentlyAnimating;
            }

            private Vec2 getVelocityVecTowards(Grid.Cell next, float speed) {
                return getPos().vecTowards(next.getCenter()).setNorm(speed);
            }

            @Override
            public void update() {
                if (mDestinationReached) {
                    mCurrentlyAnimating = false;
                    return;
                }
                Grid.Cell next = mPath.peek();
                if (getPos().distTo(next.getCenter()) < TARGET_REACHED_THRESHOLD) {
                    mPath.pop();
                    next = mPath.peek();

                    if (next == null) {
                        mDestinationReached = true;
                        queueAdd( mExplosionFactory.spawnAt(getPos()) );
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


        public PokemonSprite() {
            super(Assets.POKEMON);
        }

        @Override
        public Animation constructAnimation() {
            return new PokemonAnimation();
        }

        @Override
        public void onFinish() {}

    }


    /**********************************************************************************************/

    private Grid mGrid;
    private Random mRandGen;
    private float mSpawnThreshold;

    private ExplosionFactory mExplosionFactory;

    private boolean mStarted;

    public PokemonFactory(Grid grid) {
        mGrid = grid;
        mRandGen = new Random();
        mStarted = false;

        AssetCache.getSprite(Assets.POKEMON).setMaxFrameIndex(MAX_FRAME_INDEX);

        mExplosionFactory = new ExplosionFactory();
    }

    public void start() {
        mStarted = true;
        mSpawnThreshold = 1/SPAWN_FREQUENCY;
    }

    public void stop() {
        mStarted = false;
    }


    @Override
    public void update() {
        if (mStarted) {
            mSpawnThreshold -= GameTime.getFrameDelta();

            if (mSpawnThreshold < 0) {
                spawn();
                mSpawnThreshold += 1/SPAWN_FREQUENCY;
            }
        }
        super.update();
    }

    @Override
    public SpriteSpawn constructSpawn() {
        return new PokemonSprite();
    }


}
