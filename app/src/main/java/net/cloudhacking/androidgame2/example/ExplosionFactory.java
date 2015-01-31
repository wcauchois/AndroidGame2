package net.cloudhacking.androidgame2.example;

import net.cloudhacking.androidgame2.Assets;
import net.cloudhacking.androidgame2.engine.factory.SpriteFactory;
import net.cloudhacking.androidgame2.engine.utils.CommonUtils;

import java.util.HashMap;

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
            int row = CommonUtils.getRandom().nextInt(8);
            return new AnimationSequence(sSequenceCache.get(row), 0, SPEED);
        }

    }


    private static final float SPEED = 10;
    private static final float SCALE = 4;

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

    @Override
    public SpriteSpawn constructSpawn() {
        return new ExplosionSprite();
    }

}
