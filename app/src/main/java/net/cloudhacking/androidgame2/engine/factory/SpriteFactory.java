package net.cloudhacking.androidgame2.engine.factory;

import net.cloudhacking.androidgame2.engine.foundation.Animated;
import net.cloudhacking.androidgame2.engine.foundation.Group;
import net.cloudhacking.androidgame2.engine.foundation.Sprite;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 1/21/2015.
 */
public abstract class SpriteFactory extends Group<Animated> {

    /**
     * This is a framework for creating a lot of sprites systematically (like a creep wave).
     * It works by first calling spawn() or spawnAt(target) which automatically calls
     * constructSpawn().  This returns and adds an instance of SpriteSpawn, which will automatically
     * call and start the Animation provided by constructAnimation().  The SpriteSpawn will
     * stay alive until its animation returns isAnimating() == false, after which
     * SpriteSpawn.onFinish() will be called, and then the sprite will be deleted from the group.
     *
     * See PokemonFactory for an implementation of SpriteFactory.
     */

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
            }
        }
    }


    abstract public SpriteSpawn constructSpawn();

    public SpriteSpawn spawn() {
        SpriteSpawn spawn = constructSpawn();
        spawn.onSpawn();

        addToFront(spawn);

        d("spawning sprite : "+spawn.getClass().getSimpleName());
        return spawn;
    }

    public SpriteSpawn spawnAt(PointF target) {
        SpriteSpawn spawn = constructSpawn();
        spawn.onSpawnAt(target);

        addToFront(spawn);

        d("spawning sprite @"+target+": "+spawn.getClass().getSimpleName());
        return spawn;
    }

}
