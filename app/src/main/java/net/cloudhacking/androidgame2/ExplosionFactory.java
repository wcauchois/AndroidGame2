package net.cloudhacking.androidgame2;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.factory.SpriteFactory;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Andrew on 1/21/2015.
 */
public class ExplosionFactory extends SpriteFactory {

    private class ExplosionSprite extends SpriteSpawn {

        public ExplosionSprite() {
            super(Assets.EXPLOSIONS);
            setScale(SCALE);
        }

        @Override
        public void onFinish() {}

        @Override
        public Animation constructAnimation() {
            int row = mRandGen.nextInt(8);
            return new AnimationSequence(sSequenceCache.get(row), 0, SPEED);
        }

    }


    private final float SPEED = 10;
    private final float SCALE = 4;

    private Random mRandGen;
    private static HashMap<Integer, int[]> sSequenceCache;
    static {
        sSequenceCache = new HashMap<Integer, int[]>();

        for (int i=0; i<8; i++) {
            int[] seq = new int[16];
            for (int j=0; j<16; j++) {
                seq[j] = i*16 + j;
            }
            sSequenceCache.put(i, seq);
        }
    }


    public ExplosionFactory() {
        mRandGen = new Random();
    }

    @Override
    public SpriteSpawn constructSpawn() {
        return new ExplosionSprite();
    }

}
