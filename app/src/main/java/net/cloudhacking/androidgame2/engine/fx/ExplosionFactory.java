package net.cloudhacking.androidgame2.engine.fx;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.foundation.Animated;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Andrew on 1/21/2015.
 */
public class ExplosionFactory extends FXSpriteFactory {

    private class ExplosionSprite extends FXSprite {

        public ExplosionSprite() {
            super(Assets.EXPLOSIONS);
            setScale(SCALE);
        }

        @Override
        public Animation constructAnimation() {
            int row = mGenerator.nextInt(8);
            return new AnimationSequence(mSequenceCache.get(row), 0, SPEED);
        }

    }


    private final float SPEED = 10;
    private final float SCALE = 4;

    private Random mGenerator;
    private HashMap<Integer, int[]> mSequenceCache;

    public ExplosionFactory() {
        mGenerator = new Random();
        mSequenceCache = new HashMap<Integer, int[]>();

        for (int i=0; i<8; i++) {
            int[] seq = new int[16];
            for (int j=0; j<16; j++) {
                seq[j] = i+j;
            }
            mSequenceCache.put(i, seq);
        }
    }

    @Override
    public FXSprite constructFXSprite() {
        return new ExplosionSprite();
    }

}
