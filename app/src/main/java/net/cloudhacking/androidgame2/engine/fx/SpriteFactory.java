package net.cloudhacking.androidgame2.engine.fx;

import net.cloudhacking.androidgame2.engine.foundation.Animated;
import net.cloudhacking.androidgame2.engine.foundation.Entity;
import net.cloudhacking.androidgame2.engine.foundation.Sprite;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 1/21/2015.
 */
public abstract class SpriteFactory extends Entity {

    public abstract static class SpriteSpawn extends Animated {

        public SpriteSpawn(SpriteAsset asset) {
            super(asset);
        }

        public SpriteSpawn(Sprite sprite) {
            super(sprite);
        }

        abstract public Animation constructAnimation();

        public void onSpawn() {
            queueAnimation(constructAnimation(), false, false);
        }

        public void onSpawnAt(PointF target) {
            queueAnimation(constructAnimation(), false, false);
            setPos(target);
        }

        abstract public void onFinish();

        @Override
        public void update() {
            super.update();
            if (!isAnimating()) {
                onFinish();
                orphan();
                setVisibility(false);
            }
        }
    }


    abstract public SpriteSpawn constructSpawn();

    public SpriteSpawn spawn() {
        SpriteSpawn spawn = constructSpawn();
        spawn.onSpawn();

        d("spawning sprite : "+spawn.getClass().getSimpleName());
        return spawn;
    }

    public SpriteSpawn spawnAt(PointF target) {
        SpriteSpawn spawn = constructSpawn();
        spawn.onSpawnAt(target);

        d("spawning sprite @"+target+": "+spawn.getClass().getSimpleName());
        return spawn;
    }

}
