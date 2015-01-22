package net.cloudhacking.androidgame2.engine.fx;

import net.cloudhacking.androidgame2.engine.foundation.Animated;
import net.cloudhacking.androidgame2.engine.foundation.Entity;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.SpriteAsset;

/**
 * Created by Andrew on 1/21/2015.
 */
public abstract class FXSpriteFactory extends Entity {

    public abstract static class FXSprite extends Animated {
        public FXSprite(SpriteAsset asset) {
            super(asset);
        }

        abstract public Animation constructAnimation();

        public void onSpawn(PointF target) {
            queueAnimation(constructAnimation(), false, false);
            setPos(target);
        }

        public void update() {
            super.update();
            if (!isAnimating()) {
                orphan();
            }
        }
    }

    abstract public FXSprite constructFXSprite();

    public FXSprite spawnAt(PointF target) {
        FXSprite fx = constructFXSprite();
        fx.onSpawn( target );
        // d("spawning fx sprite @"+target+": "+fx.getClass().getSimpleName());
        return fx;
    }

}
