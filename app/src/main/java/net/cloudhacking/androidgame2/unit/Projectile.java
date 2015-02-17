package net.cloudhacking.androidgame2.unit;

import net.cloudhacking.androidgame2.engine.element.Entity;
import net.cloudhacking.androidgame2.engine.element.Renderable;
import net.cloudhacking.androidgame2.engine.gl.BasicGLScript;
import net.cloudhacking.androidgame2.engine.utils.PointF;
import net.cloudhacking.androidgame2.engine.utils.Vec2;

/**
 * Created by Andrew on 2/16/2015.
 */
public abstract class Projectile extends Entity {

    private static final float TARGET_HIT_THRESHOLD = 1; //pixel

    private CDUnit mTarget;
    private Renderable mRenderable;

    private PointF mPos;
    private float mSpeed;

    public Projectile(CDUnit target, Renderable renderable, PointF pos, float speed) {
        mTarget = target;
        mRenderable = renderable;
        mPos = pos;
        mSpeed = speed;  // in pixels/sec
    }

    abstract void onHit(CDUnit target);

    protected void updateMotion(PointF pos, Vec2 dir) {
        pos.move(dir.setNorm(mSpeed));
    }


    private boolean inRange(Vec2 dir) {
        return dir.norm() < TARGET_HIT_THRESHOLD;
    }

    @Override
    public void update() {
        Vec2 dir = mPos.vecTowards(mTarget.getPos());
        if (inRange(dir)) {
            onHit(mTarget);
            kill();
            orphan();
            mRenderable.hide();
            return;
        }
        updateMotion(mPos, dir);
        mRenderable.update();
    }

    @Override
    public void draw(BasicGLScript gls) {
        mRenderable.draw(gls);
    }

}
